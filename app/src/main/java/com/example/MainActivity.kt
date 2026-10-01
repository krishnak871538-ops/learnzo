package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.entity.UserProfile
import com.example.ui.LearnzoViewModel
import com.example.ui.Screen
import com.example.ui.components.SurpriseBoxDialog
import com.example.ui.screens.AiStudioScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LanguageSettingsScreen
import com.example.ui.screens.LessonScreen
import com.example.ui.screens.OnboardingScreen
import com.example.ui.screens.ParentDashboardScreen
import com.example.ui.screens.QuizScreen
import com.example.ui.screens.StudyAlarmScreen
import com.example.ui.screens.WeeklyChallengeScreen
import com.example.ui.screens.WelcomeScreen
import com.example.ui.theme.LearnzoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LearnzoTheme {
                val viewModel: LearnzoViewModel = viewModel()
                LearnzoApp(viewModel)
            }
        }
    }
}

@Composable
fun LearnzoApp(viewModel: LearnzoViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val profile by viewModel.userProfile.collectAsStateWithLifecycle()
    val dailyPlan by viewModel.dailyPlan.collectAsStateWithLifecycle()
    val activeMistakes by viewModel.activeMistakes.collectAsStateWithLifecycle()
    val selectedSubject by viewModel.selectedSubject.collectAsStateWithLifecycle()
    val selectedLesson by viewModel.selectedLesson.collectAsStateWithLifecycle()
    val quizQuestions by viewModel.quizQuestions.collectAsStateWithLifecycle()
    val consecutiveCorrect by viewModel.consecutiveCorrect.collectAsStateWithLifecycle()
    val showSurpriseBox by viewModel.showSurpriseBoxDialog.collectAsStateWithLifecycle()
    val currentReward by viewModel.currentReward.collectAsStateWithLifecycle()
    val isAudioMuted by viewModel.isAudioMuted.collectAsStateWithLifecycle()

    val currentProfile = profile ?: UserProfile()
    val isHindi = currentProfile.preferredLanguage != "en"

    LaunchedEffect(currentProfile.preferredLanguage) {
        com.example.util.AppStrings.setGlobalLanguage(currentProfile.preferredLanguage)
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
        ) {
            when (currentScreen) {
                Screen.WELCOME -> {
                    WelcomeScreen(
                        isHindi = isHindi,
                        onContinue = { viewModel.onWelcomeContinue() },
                        onSpeakGreeting = { viewModel.speakWelcome() }
                    )
                }

                Screen.ONBOARDING -> {
                    OnboardingScreen(
                        initialName = currentProfile.childName,
                        initialGender = currentProfile.characterGender,
                        initialNickname = currentProfile.characterNickname,
                        initialLanguage = currentProfile.preferredLanguage,
                        initialBoard = currentProfile.board,
                        initialClass = currentProfile.studentClass,
                        initialGoals = currentProfile.goals,
                        initialUserType = currentProfile.userType,
                        isHindi = isHindi,
                        onBack = { viewModel.navigateTo(Screen.WELCOME) },
                        onSaveProfile = { name, gender, nickname, lang, board, studentClass, goals, userType ->
                            viewModel.saveChildProfile(name, gender, nickname, lang, board, studentClass, goals, userType)
                        }
                    )
                }

                Screen.HOME -> {
                    HomeScreen(
                        profile = currentProfile,
                        dailyPlan = dailyPlan,
                        isHindi = isHindi,
                        isAudioMuted = isAudioMuted,
                        onToggleLanguage = { viewModel.toggleLanguage() },
                        onToggleMute = { viewModel.toggleAudioMute() },
                        onOpenSurpriseBox = { viewModel.triggerSurpriseBoxModal() },
                        onSelectSubjectLesson = { subject -> viewModel.startLesson(subject) },
                        onSelectSubjectQuiz = { subject -> viewModel.startQuiz(subject) },
                        onStartPracticeTopic = { topic, subjectId ->
                            val subj = com.example.model.SubjectCatalog.getById(subjectId)
                            viewModel.startQuiz(subj, topic)
                        },
                        onOpenParentDashboard = { viewModel.navigateTo(Screen.PARENT_DASHBOARD) },
                        onOpenStudyAlarm = { viewModel.navigateTo(Screen.STUDY_ALARM) },
                        onOpenLeaderboard = { viewModel.navigateTo(Screen.LEADERBOARD) },
                        onOpenAiStudio = { viewModel.navigateTo(Screen.AI_STUDIO) }
                    )
                }

                Screen.AI_STUDIO -> {
                    AiStudioScreen(
                        aiService = viewModel.aiService,
                        isHindi = isHindi,
                        onBack = { viewModel.navigateTo(Screen.HOME) }
                    )
                }

                Screen.LESSON -> {
                    val lesson = selectedLesson ?: com.example.model.LessonCatalog.lessons.first()
                    LessonScreen(
                        lesson = lesson,
                        subject = selectedSubject,
                        isHindi = isHindi,
                        onBack = { viewModel.navigateTo(Screen.HOME) },
                        onLessonComplete = { minutes -> viewModel.finishLesson(minutes) },
                        onSpeakText = { text -> viewModel.speak(text) }
                    )
                }

                Screen.QUIZ -> {
                    QuizScreen(
                        questions = quizQuestions,
                        subject = selectedSubject,
                        isHindi = isHindi,
                        consecutiveCorrect = consecutiveCorrect,
                        board = currentProfile.board,
                        studentClass = currentProfile.studentClass,
                        onBack = { viewModel.navigateTo(Screen.HOME) },
                        onAnswerSubmitted = { question, selectedIndex, isCorrect ->
                            viewModel.onAnswerSubmitted(question, selectedIndex, isCorrect)
                        },
                        onSpeakText = { text -> viewModel.speak(text) }
                    )
                }

                Screen.PARENT_DASHBOARD -> {
                    ParentDashboardScreen(
                        profile = currentProfile,
                        activeMistakes = activeMistakes,
                        isHindi = isHindi,
                        onBack = { viewModel.navigateTo(Screen.HOME) },
                        onUpdateAlarm = { h, m -> viewModel.updateAlarm(h, m) },
                        onSpeakTeacherReport = { report -> viewModel.speak(report) },
                        onSelectLanguage = { lang -> viewModel.setLanguage(lang) },
                        onOpenMoreLanguages = { viewModel.navigateTo(Screen.LANGUAGE_SETTINGS) }
                    )
                }

                Screen.STUDY_ALARM -> {
                    StudyAlarmScreen(
                        childName = currentProfile.childName,
                        isHindi = isHindi,
                        onStartStudy = { viewModel.navigateTo(Screen.HOME) },
                        onDismissAlarm = { viewModel.navigateTo(Screen.HOME) },
                        onSpeakAlarm = { viewModel.speakAlarm() }
                    )
                }

                Screen.LEADERBOARD -> {
                    WeeklyChallengeScreen(
                        profile = currentProfile,
                        isHindi = isHindi,
                        onBack = { viewModel.navigateTo(Screen.HOME) }
                    )
                }

                Screen.LANGUAGE_SETTINGS -> {
                    LanguageSettingsScreen(
                        currentLangCode = currentProfile.preferredLanguage,
                        onBack = { viewModel.navigateTo(Screen.PARENT_DASHBOARD) },
                        onSelectLanguage = { lang -> viewModel.setLanguage(lang) },
                        onSpeakSample = { text, _ -> viewModel.speak(text) }
                    )
                }
            }

            // Interactive Surprise Box Modal Dialog
            if (showSurpriseBox) {
                SurpriseBoxDialog(
                    reward = currentReward,
                    isHindi = isHindi,
                    onOpenBox = { viewModel.openSurpriseBox() },
                    onClaimReward = { viewModel.claimSurpriseReward() },
                    onDismiss = { viewModel.dismissSurpriseBox() }
                )
            }
        }
    }
}
