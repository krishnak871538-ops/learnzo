package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey val id: Int = 1,
    val childName: String = "Riya",
    val characterGender: String = "GIRL", // "BOY" or "GIRL"
    val characterNickname: String = "Super Star",
    val board: String = "CBSE", // CBSE, ICSE, State Board, IB, IGCSE
    val studentClass: String = "Class 6", // Class 3 to Class 10
    val goals: String = "Score 95%+, Build Strong Concepts",
    val userType: String = "Parent with Student", // "Parent with Student", "Only Student", "Only Parent"
    val coins: Int = 120,
    val stars: Int = 18,
    val totalStudySeconds: Long = 720, // 12 mins initial
    val todayStudyMinutes: Int = 8,
    val dailyGoalMinutes: Int = 15,
    val correctAnswersCount: Int = 8,
    val streakDays: Int = 4,
    val preferredLanguage: String = "en", // "en", "hi", "hinglish"
    val preferredTeacherGender: String = "BOTH", // "MALE", "FEMALE", "BOTH"
    val studyAlarmHour: Int = 18,
    val studyAlarmMinute: Int = 0,
    val isStudyAlarmEnabled: Boolean = true,
    val availableSurpriseBoxes: Int = 1
)

@Entity(tableName = "mistake_records")
data class MistakeEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val subjectId: String,
    val topicName: String,
    val questionText: String,
    val wrongAnswer: String,
    val correctAnswer: String,
    val mistakeCount: Int = 1,
    val lastMistakeTimestamp: Long = System.currentTimeMillis(),
    val isResolved: Boolean = false
)

@Entity(tableName = "daily_plan")
data class DailyPlanEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val titleHi: String,
    val titleEn: String,
    val subjectId: String,
    val targetType: String, // "MINUTES" or "QUESTIONS"
    val targetValue: Int,
    val currentValue: Int = 0,
    val isCompleted: Boolean = false,
    val isWeakTopic: Boolean = false
)

@Entity(tableName = "unlocked_rewards")
data class RewardEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val rewardType: String,
    val title: String,
    val value: Int,
    val emoji: String,
    val timestamp: Long = System.currentTimeMillis()
)
