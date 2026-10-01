package com.example.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.entity.DailyPlanEntity
import com.example.data.entity.MistakeEntity
import com.example.data.entity.RewardEntity
import com.example.data.entity.UserProfile
import kotlinx.coroutines.flow.Flow

@Dao
interface LearnzoDao {
    @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
    fun getUserProfile(): Flow<UserProfile?>

    @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
    suspend fun getUserProfileOnce(): UserProfile?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: UserProfile)

    // Mistake Memory
    @Query("SELECT * FROM mistake_records WHERE isResolved = 0 ORDER BY mistakeCount DESC, lastMistakeTimestamp DESC")
    fun getActiveMistakes(): Flow<List<MistakeEntity>>

    @Query("SELECT * FROM mistake_records WHERE topicName = :topic LIMIT 1")
    suspend fun findMistakeByTopic(topic: String): MistakeEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMistake(mistake: MistakeEntity)

    @Update
    suspend fun updateMistake(mistake: MistakeEntity)

    @Query("UPDATE mistake_records SET isResolved = 1 WHERE topicName = :topic")
    suspend fun resolveMistakesForTopic(topic: String)

    // Daily Plan
    @Query("SELECT * FROM daily_plan ORDER BY isWeakTopic DESC, id ASC")
    fun getDailyPlan(): Flow<List<DailyPlanEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDailyPlan(items: List<DailyPlanEntity>)

    @Query("DELETE FROM daily_plan")
    suspend fun clearDailyPlan()

    @Update
    suspend fun updateDailyPlanItem(item: DailyPlanEntity)

    // Rewards & Badges
    @Query("SELECT * FROM unlocked_rewards ORDER BY timestamp DESC")
    fun getUnlockedRewards(): Flow<List<RewardEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReward(reward: RewardEntity)
}
