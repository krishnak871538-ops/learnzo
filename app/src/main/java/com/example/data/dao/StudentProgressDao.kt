package com.example.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.entity.StudentProgress
import kotlinx.coroutines.flow.Flow

@Dao
interface StudentProgressDao {
    @Query("SELECT * FROM student_progress WHERE studentId = :studentId ORDER BY timestamp DESC")
    fun getProgressForStudent(studentId: Int): Flow<List<StudentProgress>>

    @Query("SELECT * FROM student_progress WHERE studentId = :studentId AND subject = :subject")
    fun getProgressBySubject(studentId: Int, subject: String): Flow<List<StudentProgress>>

    @Query("SELECT * FROM student_progress WHERE studentId = :studentId AND subject = :subject AND topic = :topic LIMIT 1")
    suspend fun getTopicProgress(studentId: Int, subject: String, topic: String): StudentProgress?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgress(progress: StudentProgress): Long

    @Update
    suspend fun updateProgress(progress: StudentProgress)

    @Delete
    suspend fun deleteProgress(progress: StudentProgress)

    @Query("DELETE FROM student_progress WHERE studentId = :studentId")
    suspend fun clearProgressForStudent(studentId: Int)
}
