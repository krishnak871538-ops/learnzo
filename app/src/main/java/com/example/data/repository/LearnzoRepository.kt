package com.example.data.repository

import com.example.data.dao.LearnzoDao
import com.example.data.entity.DailyPlanEntity
import com.example.data.entity.MistakeEntity
import com.example.data.entity.RewardEntity
import com.example.data.entity.UserProfile
import com.example.model.RewardPool
import com.example.model.RewardType
import com.example.model.SurpriseReward
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class LearnzoRepository(private val dao: LearnzoDao) {

    val userProfile: Flow<UserProfile?> = dao.getUserProfile()
    val activeMistakes: Flow<List<MistakeEntity>> = dao.getActiveMistakes()
    val dailyPlan: Flow<List<DailyPlanEntity>> = dao.getDailyPlan()
    val unlockedRewards: Flow<List<RewardEntity>> = dao.getUnlockedRewards()

    suspend fun getUserProfileOnce(): UserProfile? = dao.getUserProfileOnce()

    suspend fun ensureInitialized() {
        val existing = dao.getUserProfileOnce()
        if (existing == null) {
            val detectedLang = com.example.util.AppStrings.detectDeviceLanguage()
            val initialProfile = UserProfile(
                id = 1,
                childName = "Riya",
                characterGender = "GIRL",
                characterNickname = "Super Star",
                board = "CBSE",
                studentClass = "Class 6",
                goals = "Score 95%+, Build Strong Concepts",
                userType = "Parent with Student",
                coins = 150,
                stars = 24,
                totalStudySeconds = 950, // 15+ mins
                todayStudyMinutes = 8,
                dailyGoalMinutes = 15,
                correctAnswersCount = 7, // 3 away from next surprise box!
                streakDays = 4,
                preferredLanguage = detectedLang,
                preferredTeacherGender = "BOTH",
                studyAlarmHour = 18,
                studyAlarmMinute = 0,
                isStudyAlarmEnabled = true,
                availableSurpriseBoxes = 1
            )
            dao.insertOrUpdateProfile(initialProfile)

            // Initial Smart Learning Memory record
            dao.insertMistake(
                MistakeEntity(
                    subjectId = "maths",
                    topicName = "Multiplication",
                    questionText = "4 × 3 = ?",
                    wrongAnswer = "10",
                    correctAnswer = "12",
                    mistakeCount = 2,
                    isResolved = false
                )
            )
            dao.insertMistake(
                MistakeEntity(
                    subjectId = "english",
                    topicName = "Opposites",
                    questionText = "Opposite of HOT",
                    wrongAnswer = "Warm",
                    correctAnswer = "Cold",
                    mistakeCount = 1,
                    isResolved = false
                )
            )

            // Initial Personal Daily Study Plan
            val plan = listOf(
                DailyPlanEntity(
                    titleHi = "🔢 गणित (Maths) — 15 मिनट",
                    titleEn = "🔢 Mathematics — 15 Minutes",
                    subjectId = "maths",
                    targetType = "MINUTES",
                    targetValue = 15,
                    currentValue = 10,
                    isCompleted = false,
                    isWeakTopic = false
                ),
                DailyPlanEntity(
                    titleHi = "🔤 अंग्रेज़ी (English) — 10 मिनट",
                    titleEn = "🔤 English — 10 Minutes",
                    subjectId = "english",
                    targetType = "MINUTES",
                    targetValue = 10,
                    currentValue = 10,
                    isCompleted = true,
                    isWeakTopic = false
                ),
                DailyPlanEntity(
                    titleHi = "🧩 Quiz चैलेंज — 10 सवाल",
                    titleEn = "🧩 Quiz Challenge — 10 Questions",
                    subjectId = "all",
                    targetType = "QUESTIONS",
                    targetValue = 10,
                    currentValue = 7,
                    isCompleted = false,
                    isWeakTopic = false
                ),
                DailyPlanEntity(
                    titleHi = "🧠 पिछली गलती की Practice (Multiplication)",
                    titleEn = "🧠 Smart Practice: Multiplication",
                    subjectId = "maths",
                    targetType = "QUESTIONS",
                    targetValue = 5,
                    currentValue = 3,
                    isCompleted = false,
                    isWeakTopic = true
                ),
                DailyPlanEntity(
                    titleHi = "🎁 पूरा करने पर Surprise Box अनलॉक!",
                    titleEn = "🎁 Complete All → Surprise Box Unlock!",
                    subjectId = "reward",
                    targetType = "GOAL",
                    targetValue = 1,
                    currentValue = 0,
                    isCompleted = false,
                    isWeakTopic = false
                )
            )
            dao.insertDailyPlan(plan)

            // Initial Rewards
            dao.insertReward(
                RewardEntity(
                    rewardType = "BADGE",
                    title = "First Step Champ 🏆",
                    value = 1,
                    emoji = "🏆"
                )
            )
            dao.insertReward(
                RewardEntity(
                    rewardType = "COINS",
                    title = "Welcome Bonus Coins 🪙",
                    value = 50,
                    emoji = "🪙"
                )
            )
        }
    }

    suspend fun saveProfile(profile: UserProfile) {
        dao.insertOrUpdateProfile(profile)
    }

    suspend fun recordCorrectAnswer(subjectId: String, topicName: String): Boolean {
        val profile = dao.getUserProfileOnce() ?: return false
        val newCorrectCount = profile.correctAnswersCount + 1
        var newBoxes = profile.availableSurpriseBoxes
        var boxEarned = false

        // Every 10 correct answers awards a Surprise Box!
        if (newCorrectCount % 10 == 0) {
            newBoxes += 1
            boxEarned = true
        }

        val updatedProfile = profile.copy(
            correctAnswersCount = newCorrectCount,
            coins = profile.coins + 10,
            stars = profile.stars + 1,
            availableSurpriseBoxes = newBoxes
        )
        dao.insertOrUpdateProfile(updatedProfile)

        // If this was a weak topic that was correctly answered, check to resolve
        val existingMistake = dao.findMistakeByTopic(topicName)
        if (existingMistake != null && !existingMistake.isResolved) {
            dao.resolveMistakesForTopic(topicName)
        }

        return boxEarned
    }

    suspend fun recordMistake(
        subjectId: String,
        topicName: String,
        questionText: String,
        wrongAnswer: String,
        correctAnswer: String
    ) {
        val existing = dao.findMistakeByTopic(topicName)
        if (existing != null) {
            dao.updateMistake(
                existing.copy(
                    mistakeCount = existing.mistakeCount + 1,
                    lastMistakeTimestamp = System.currentTimeMillis(),
                    isResolved = false,
                    wrongAnswer = wrongAnswer,
                    questionText = questionText
                )
            )
        } else {
            dao.insertMistake(
                MistakeEntity(
                    subjectId = subjectId,
                    topicName = topicName,
                    questionText = questionText,
                    wrongAnswer = wrongAnswer,
                    correctAnswer = correctAnswer,
                    mistakeCount = 1,
                    isResolved = false
                )
            )
        }
    }

    suspend fun addStudySeconds(seconds: Long): Boolean {
        val profile = dao.getUserProfileOnce() ?: return false
        val oldSec = profile.totalStudySeconds
        val newSec = oldSec + seconds
        var newBoxes = profile.availableSurpriseBoxes
        var earnedBox = false

        // 20 minutes (1200 seconds) milestone
        val oldMilestones = oldSec / 1200
        val newMilestones = newSec / 1200
        if (newMilestones > oldMilestones) {
            newBoxes += (newMilestones - oldMilestones).toInt()
            earnedBox = true
        }

        dao.insertOrUpdateProfile(
            profile.copy(
                totalStudySeconds = newSec,
                availableSurpriseBoxes = newBoxes
            )
        )
        return earnedBox
    }

    suspend fun openSurpriseBox(): SurpriseReward? {
        val profile = dao.getUserProfileOnce() ?: return null
        if (profile.availableSurpriseBoxes <= 0) return null

        val reward = RewardPool.getRandomReward()
        var newCoins = profile.coins
        var newStars = profile.stars

        when (reward.type) {
            RewardType.COINS -> newCoins += reward.value
            RewardType.STARS -> newStars += reward.value
            else -> {
                newCoins += 25
                newStars += 2
            }
        }

        dao.insertOrUpdateProfile(
            profile.copy(
                coins = newCoins,
                stars = newStars,
                availableSurpriseBoxes = profile.availableSurpriseBoxes - 1
            )
        )

        dao.insertReward(
            RewardEntity(
                rewardType = reward.type.name,
                title = reward.titleHi,
                value = reward.value,
                emoji = reward.emoji
            )
        )

        return reward
    }

    suspend fun updateOnboardingProfile(
        name: String,
        gender: String,
        nickname: String,
        language: String,
        board: String,
        studentClass: String,
        goals: String,
        userType: String
    ) {
        val profile = dao.getUserProfileOnce() ?: UserProfile()
        dao.insertOrUpdateProfile(
            profile.copy(
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
    }

    suspend fun updateAlarm(hour: Int, minute: Int, enabled: Boolean) {
        val profile = dao.getUserProfileOnce() ?: return
        dao.insertOrUpdateProfile(
            profile.copy(
                studyAlarmHour = hour,
                studyAlarmMinute = minute,
                isStudyAlarmEnabled = enabled
            )
        )
    }

    suspend fun setLanguage(lang: String) {
        val profile = dao.getUserProfileOnce() ?: return
        dao.insertOrUpdateProfile(profile.copy(preferredLanguage = lang))
    }

    suspend fun toggleLanguage() {
        val profile = dao.getUserProfileOnce() ?: return
        val newLang = if (profile.preferredLanguage == "hi") "en" else "hi"
        setLanguage(newLang)
    }
}
