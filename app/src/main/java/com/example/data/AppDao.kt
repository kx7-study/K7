package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    // --- USER PROFILE ---
    @Query("SELECT * FROM user_profiles WHERE uid = 'local_user' LIMIT 1")
    fun getUserProfile(): Flow<UserProfile?>

    @Query("SELECT * FROM user_profiles WHERE uid = 'local_user' LIMIT 1")
    suspend fun getUserProfileSynchronous(): UserProfile?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserProfile(profile: UserProfile)

    @Update
    suspend fun updateUserProfile(profile: UserProfile)

    // --- SECOND BRAIN VAULT ---
    @Query("SELECT * FROM second_brain_vault ORDER BY timestamp DESC")
    fun getBrainItems(): Flow<List<BrainItem>>

    @Query("SELECT * FROM second_brain_vault WHERE type = :type ORDER BY timestamp DESC")
    fun getBrainItemsByType(type: String): Flow<List<BrainItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBrainItem(item: BrainItem)

    @Query("DELETE FROM second_brain_vault WHERE id = :id")
    suspend fun deleteBrainItem(id: Long)

    @Query("SELECT * FROM second_brain_vault WHERE title LIKE '%' || :query || '%' OR content LIKE '%' || :query || '%'")
    fun searchBrainVault(query: String): Flow<List<BrainItem>>

    // --- WORLD CARDS (FLASHCARDS) ---
    @Query("SELECT * FROM world_cards ORDER BY id DESC")
    fun getWorldCards(): Flow<List<WorldCard>>

    @Query("SELECT * FROM world_cards WHERE nextReview <= :now ORDER BY retentionStrength ASC")
    fun getCardsToReview(now: Long): Flow<List<WorldCard>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorldCard(card: WorldCard)

    @Query("DELETE FROM world_cards WHERE id = :id")
    suspend fun deleteWorldCard(id: Long)

    // --- HISTORICAL MISTAKES ---
    @Query("SELECT * FROM historical_mistakes ORDER BY timestamp DESC")
    fun getHistoricalMistakes(): Flow<List<HistoricalMistake>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistoricalMistake(mistake: HistoricalMistake)

    @Query("DELETE FROM historical_mistakes WHERE id = :id")
    suspend fun deleteHistoricalMistake(id: Long)
}
