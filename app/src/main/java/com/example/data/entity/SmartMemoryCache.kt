package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "smart_memory_cache")
data class SmartMemoryCache(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val studentId: Int = 1,
    val subject: String,
    val topic: String,
    val weakReason: String,
    val difficultyLevel: Int = 1, // 1: Easy, 2: Medium, 3: Advanced
    val needsPractice: Boolean = true,
    val consecutiveWrongAnswers: Int = 1,
    val lastPromptedQuestion: String = "",
    val simplifiedExplanation: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
