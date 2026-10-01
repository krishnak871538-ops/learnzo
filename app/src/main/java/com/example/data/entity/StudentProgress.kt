package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "student_progress")
data class StudentProgress(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val studentId: Int = 1,
    val subject: String,
    val topic: String,
    val masteryLevel: String = "BEGINNER", // "BEGINNER", "INTERMEDIATE", "MASTERED"
    val questionsAttempted: Int = 0,
    val questionsCorrect: Int = 0,
    val accuracyPercentage: Float = 0f,
    val lastCheckInStatus: String = "UNDERSTOOD", // "UNDERSTOOD", "NEEDS_PRACTICE", "HELPED"
    val timestamp: Long = System.currentTimeMillis()
)
