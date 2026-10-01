package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.dao.LearnzoDao
import com.example.data.dao.SmartMemoryCacheDao
import com.example.data.dao.StudentProgressDao
import com.example.data.dao.StudyHistoryDao
import com.example.data.entity.DailyPlanEntity
import com.example.data.entity.MistakeEntity
import com.example.data.entity.RewardEntity
import com.example.data.entity.SmartMemoryCache
import com.example.data.entity.StudentProgress
import com.example.data.entity.StudyHistory
import com.example.data.entity.UserProfile

@Database(
    entities = [
        UserProfile::class,
        MistakeEntity::class,
        DailyPlanEntity::class,
        RewardEntity::class,
        StudentProgress::class,
        StudyHistory::class,
        SmartMemoryCache::class
    ],
    version = 3,
    exportSchema = false
)
abstract class LearnzoDatabase : RoomDatabase() {
    abstract fun learnzoDao(): LearnzoDao
    abstract fun studentProgressDao(): StudentProgressDao
    abstract fun studyHistoryDao(): StudyHistoryDao
    abstract fun smartMemoryCacheDao(): SmartMemoryCacheDao

    companion object {
        @Volatile
        private var INSTANCE: LearnzoDatabase? = null

        fun getDatabase(context: Context): LearnzoDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LearnzoDatabase::class.java,
                    "learnzo_database"
                )
                .fallbackToDestructiveMigration(dropAllTables = true)
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
