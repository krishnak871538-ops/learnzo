package com.example.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.entity.StudyHistory
import kotlinx.coroutines.flow.Flow

@Dao
interface StudyHistoryDao {
    @Query("SELECT * FROM study_history WHERE studentId = :studentId ORDER BY timestamp DESC")
    fun getHistoryForStudent(studentId: Int): Flow<List<StudyHistory>>

    @Query("SELECT * FROM study_history WHERE studentId = :studentId ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentHistory(studentId: Int, limit: Int): Flow<List<StudyHistory>>

    @Query("SELECT SUM(durationSeconds) FROM study_history WHERE studentId = :studentId")
    fun getTotalStudySeconds(studentId: Int): Flow<Long?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(history: StudyHistory): Long

    @Update
    suspend fun updateHistory(history: StudyHistory)

    @Delete
    suspend fun deleteHistory(history: StudyHistory)

    @Query("DELETE FROM study_history WHERE studentId = :studentId")
    suspend fun clearHistory(studentId: Int)
}
