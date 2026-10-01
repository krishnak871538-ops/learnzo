package com.example.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.entity.SmartMemoryCache
import kotlinx.coroutines.flow.Flow

@Dao
interface SmartMemoryCacheDao {
    @Query("SELECT * FROM smart_memory_cache WHERE studentId = :studentId ORDER BY needsPractice DESC, consecutiveWrongAnswers DESC, timestamp DESC")
    fun getAllCachedMistakes(studentId: Int): Flow<List<SmartMemoryCache>>

    @Query("SELECT * FROM smart_memory_cache WHERE studentId = :studentId AND needsPractice = 1 ORDER BY consecutiveWrongAnswers DESC")
    fun getActiveWeakTopics(studentId: Int): Flow<List<SmartMemoryCache>>

    @Query("SELECT * FROM smart_memory_cache WHERE studentId = :studentId AND topic = :topic LIMIT 1")
    suspend fun findCacheByTopic(studentId: Int, topic: String): SmartMemoryCache?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateCache(cache: SmartMemoryCache): Long

    @Update
    suspend fun updateCache(cache: SmartMemoryCache)

    @Delete
    suspend fun deleteCache(cache: SmartMemoryCache)

    @Query("UPDATE smart_memory_cache SET needsPractice = 0 WHERE studentId = :studentId AND topic = :topic")
    suspend fun markTopicResolved(studentId: Int, topic: String)

    @Query("DELETE FROM smart_memory_cache WHERE studentId = :studentId")
    suspend fun clearCache(studentId: Int)
}
