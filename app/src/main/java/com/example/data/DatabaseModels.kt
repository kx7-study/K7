package com.example.data

import androidx.room.*
import androidx.annotation.Keep
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = true)
@Entity(tableName = "user_profiles")
data class UserProfile(
    @PrimaryKey val uid: String = "local_user",
    val displayName: String = "Competitor Alpha",
    val curriculumTrack: String = "SAT_USA", // Backward compatibility
    val selectedCountryCode: String = "USA", // e.g., "USA", "GBR", "IND", "BGD"
    val selectedBoardId: String = "SAT", // e.g., "SAT", "A_LEVELS", "CBSE", "NCTB"
    val languagePreference: String = "en", // "en" | "bn" | "es" | "hi"
    val auraPoints: Int = 1200,
    val leagueTier: String = "Bronze", // Bronze -> Gold -> Platinum -> Diamond -> Elite 1%
    val currentStreak: Int = 3,
    val isPremium: Boolean = false,
    val subscriptionType: String = "FREE", // "FREE", "1_MONTH", "3_MONTHS", "1_YEAR"
    val subscriptionExpiresTimestamp: Long = 0L,
    // Cognitive Twin Metrics
    val lsi: Float = 0.65f, // Learning Speed Index
    val peakStudyStartHour: Int = 8,
    val peakStudyEndHour: Int = 11,
    val burnoutRiskScore: Int = 15, // 0 - 100
    val primarySolvingStyle: String = "Analytical", // Analytical, Intuitive, Formula-Heavy
    val missionTarget: String = "MIT", // MIT, Stanford, Harvard, Oxford, Cambridge
    val activeMentorPersona: String = "Feynman", // Einstein, Fischer, Feynman, Military, Exam Assassin
    val lastActiveDate: String = "",
    val usedSecondsToday: Long = 0,
    // Ulite Identity Engine & Security Matrix
    val email: String = "candidate@kx7.study",
    val passwordHash: String = "pbkdf2_v2_91a03f", // safe baseline
    val kx7Id: String = "KX7-SAT-USA-503A12A1",
    val clearance: String = "USER_NODE", // "USER_NODE" | "ADMIN_CORE"
    val isAuthenticated: Boolean = false,
    val status: String = "ACTIVE" // "ACTIVE" | "SUSPENDED" | "BANNED"
)

@Keep
@Entity(tableName = "second_brain_vault")
data class BrainItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val type: String, // "notes", "mistakes", "cheat_sheet", "summary"
    val title: String,
    val content: String,
    val subject: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Keep
@Entity(tableName = "world_cards")
data class WorldCard(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val front: String,
    val back: String,
    val subject: String,
    val retentionStrength: Float = 0.3f, // 0.0 to 1.0 (neon indicators!)
    val difficulty: Float = 0.5f, // 0.1 to 1.0
    val lastReviewed: Long = 0,
    val nextReview: Long = System.currentTimeMillis()
)

@Keep
@Entity(tableName = "historical_mistakes")
data class HistoricalMistake(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val subject: String,
    val question: String,
    val userAnswer: String,
    val correctAnswer: String,
    val conceptsMissed: String,
    val timestamp: Long = System.currentTimeMillis(),
    val durationSeconds: Int = 45
)
