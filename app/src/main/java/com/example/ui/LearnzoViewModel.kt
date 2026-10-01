package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.LearnzoDatabase
import com.example.data.entity.DailyPlanEntity
import com.example.data.entity.MistakeEntity
import com.example.data.entity.RewardEntity
import com.example.data.entity.UserProfile
import com.example.data.repository.LearnzoRepository
import com.example.model.Lesson
import com.example.model.LessonCatalog
import com.example.model.QuizCatalog
import com.example.model.QuizQuestion
import com.example.model.Subject
import com.example.model.SubjectCatalog
import com.example.model.SurpriseReward
import com.example.service.GeminiAiService
import com.example.util.SpeechManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class Screen {
    WELCOME,
    ONBOARDING,
    HOME,
    LESSON,
    QUIZ,
    PARENT_DASHBOARD,
    STUDY_ALARM,
    LEADERBOARD,
    AI_STUDIO,
    LANGUAGE_SETTINGS
}

class LearnzoViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: LearnzoRepository
    val speechManager: SpeechManager = SpeechManager(application)
    val aiService: GeminiAiService = GeminiAiService(application)

    init {
        val db = LearnzoDatabase.getDatabase(application)
        repository = LearnzoRepository(db.learnzoDao())
        viewModelScope.launch {
            repository.ensureInitialized()
            val current = repository.getUserProfileOnce()
            if (current != null) {
                com.example.util.AppStrings.setGlobalLanguage(current.preferredLanguage)
            }
        }
    }

    val userProfile: StateFlow<UserProfile?> = repository.userProfile
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val dailyPlan: StateFlow<List<DailyPlanEntity>> = repository.dailyPlan
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val activeMistakes: StateFlow<List<MistakeEntity>> = repository.activeMistakes
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val unlockedRewards: StateFlow<List<RewardEntity>> = repository.unlockedRewards
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    // Navigation and UI state
    private val _currentScreen = MutableStateFlow(Screen.WELCOME)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    private val _selectedSubject = MutableStateFlow(SubjectCatalog.subjects.first())
    val selectedSubject: StateFlow<Subject> = _selectedSubject.asStateFlow()

    private val _selectedLesson = MutableStateFlow<Lesson?>(null)
    val selectedLesson: StateFlow<Lesson?> = _selectedLesson.asStateFlow()

    private val _quizQuestions = MutableStateFlow<List<QuizQuestion>>(emptyList())
    val quizQuestions: StateFlow<List<QuizQuestion>> = _quizQuestions.asStateFlow()

    private val _consecutiveCorrect = MutableStateFlow(7)
    val consecutiveCorrect: StateFlow<Int> = _consecutiveCorrect.asStateFlow()

    private val _showSurpriseBoxDialog = MutableStateFlow(false)
    val showSurpriseBoxDialog: StateFlow<Boolean> = _showSurpriseBoxDialog.asStateFlow()

    private val _currentReward = MutableStateFlow<SurpriseReward?>(null)
    val currentReward: StateFlow<SurpriseReward?> = _currentReward.asStateFlow()

    private val _isAudioMuted = MutableStateFlow(false)
    val isAudioMuted: StateFlow<Boolean> = _isAudioMuted.asStateFlow()

    fun navigateTo(screen: Screen) {
        _currentScreen.value = screen
    }

    fun onWelcomeContinue() {
        val profile = userProfile.value
        if (profile == null || profile.childName.isBlank()) {
            _currentScreen.value = Screen.ONBOARDING
        } else {
            _currentScreen.value = Screen.HOME
        }
    }

    fun saveChildProfile(
        name: String,
        gender: String,
        nickname: String,
        language: String = "en",
        board: String = "CBSE",
        studentClass: String = "Class 6",
        goals: String = "Score 95%+, Build Strong Concepts",
        userType: String = "Parent with Student"
    ) {
        viewModelScope.launch {
            com.example.util.AppStrings.setGlobalLanguage(language)
            val current = userProfile.value ?: UserProfile()
            repository.saveProfile(
                current.copy(
                    childName = name,
                    characterGender = gender,
                    characterNickname = nickname,
                    preferredLanguage = language,
                    board = board,
                    studentClass = studentClass,
                    goals = goals,
                    userType = userType
                )
            )
            _currentScreen.value = Screen.HOME
        }
    }

    fun startLesson(subject: Subject) {
        _selectedSubject.value = subject
        val subjectLessons = LessonCatalog.getBySubject(subject.id)
        _selectedLesson.value = subjectLessons.firstOrNull() ?: LessonCatalog.lessons.first()
        _currentScreen.value = Screen.LESSON
    }

    fun finishLesson(minutes: Int) {
        viewModelScope.launch {
            val boxEarned = repository.addStudySeconds(minutes * 60L)
            if (boxEarned) {
                _showSurpriseBoxDialog.value = true
            }
            _currentScreen.value = Screen.HOME
        }
    }

    fun startQuiz(subject: Subject, topicFilter: String? = null) {
        _selectedSubject.value = subject
        val questions = if (topicFilter != null) {
            val filtered = QuizCatalog.getQuestionsByTopic(topicFilter)
            if (filtered.isNotEmpty()) filtered else QuizCatalog.getQuestionsBySubject(subject.id)
        } else {
            QuizCatalog.getQuestionsBySubject(subject.id)
        }
        _quizQuestions.value = questions.ifEmpty { QuizCatalog.questions }
        _currentScreen.value = Screen.QUIZ
    }

    fun onAnswerSubmitted(question: QuizQuestion, selectedIndex: Int, isCorrect: Boolean) {
        viewModelScope.launch {
            if (isCorrect) {
                _consecutiveCorrect.value += 1
                val boxEarned = repository.recordCorrectAnswer(question.subjectId, question.topicName)
                if (boxEarned) {
                    _showSurpriseBoxDialog.value = true
                }
            } else {
                val isHindi = userProfile.value?.preferredLanguage != "en"
                val wrongAns = if (isHindi) question.optionsHi.getOrNull(selectedIndex) ?: "" else question.optionsEn.getOrNull(selectedIndex) ?: ""
                val correctAns = if (isHindi) question.optionsHi.getOrNull(question.correctIndex) ?: "" else question.optionsEn.getOrNull(question.correctIndex) ?: ""
                repository.recordMistake(
                    subjectId = question.subjectId,
                    topicName = question.topicName,
                    questionText = question.questionHi,
                    wrongAnswer = wrongAns,
                    correctAnswer = correctAns
                )
            }
        }
    }

    fun triggerSurpriseBoxModal() {
        _showSurpriseBoxDialog.value = true
    }

    fun openSurpriseBox() {
        viewModelScope.launch {
            val reward = repository.openSurpriseBox()
            _currentReward.value = reward
            val isHindi = userProfile.value?.preferredLanguage != "en"
            if (reward != null) {
                val text = if (isHindi)
                    "वाह! बधाई हो! आपको मिला ${reward.titleHi}! ${reward.descriptionHi}"
                else
                    "Hooray! Congratulations! You won ${reward.titleEn}! ${reward.descriptionEn}"
                speak(text)
            }
        }
    }

    fun claimSurpriseReward() {
        _showSurpriseBoxDialog.value = false
        _currentReward.value = null
    }

    fun dismissSurpriseBox() {
        _showSurpriseBoxDialog.value = false
        _currentReward.value = null
    }

    fun setLanguage(lang: String) {
        viewModelScope.launch {
            com.example.util.AppStrings.setGlobalLanguage(lang)
            repository.setLanguage(lang)
        }
    }

    fun toggleLanguage() {
        viewModelScope.launch {
            repository.toggleLanguage()
        }
    }

    fun toggleAudioMute() {
        _isAudioMuted.value = speechManager.toggleMute()
    }

    fun speak(text: String, lang: String? = null) {
        val targetLang = lang ?: (userProfile.value?.preferredLanguage ?: "hi")
        speechManager.speak(text, targetLang)
    }

    fun speakWelcome() {
        val lang = userProfile.value?.preferredLanguage ?: "hi"
        val sample = com.example.model.SupportedLanguages.getByCode(lang).sampleGreeting
        speak(sample, lang)
    }

    fun speakAlarm() {
        val lang = userProfile.value?.preferredLanguage ?: "hi"
        val name = userProfile.value?.childName ?: "Riya"
        val text = com.example.util.AppStrings.studyAlarmTeacherVoice(lang, name)
        speak(text, lang)
    }

    fun updateAlarm(hour: Int, minute: Int) {
        viewModelScope.launch {
            repository.updateAlarm(hour, minute, true)
        }
    }

    override fun onCleared() {
        super.onCleared()
        speechManager.shutdown()
    }
}
