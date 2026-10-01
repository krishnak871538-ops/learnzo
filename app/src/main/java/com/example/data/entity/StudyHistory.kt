package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "study_history")
data class StudyHistory(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val studentId: Int = 1,
    val subject: String,
    val topic: String,
    val durationSeconds: Int,
    val score: Int = 0,
    val activityType: String = "LESSON", // "LESSON", "QUIZ", "CHECK_IN"
    val notes: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
