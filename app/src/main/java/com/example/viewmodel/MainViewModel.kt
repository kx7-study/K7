package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.util.UUID

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getDatabase(application)
    private val dao = db.appDao()

    // --- Core StateFlow DB Streams ---
    val userProfile: StateFlow<UserProfile?> = dao.getUserProfile()
        .distinctUntilChanged()
        .stateInViewModel(null)

    val brainItems: StateFlow<List<BrainItem>> = dao.getBrainItems()
        .stateInViewModel(emptyList())

    val worldCards: StateFlow<List<WorldCard>> = dao.getWorldCards()
        .stateInViewModel(emptyList())

    val historicalMistakes: StateFlow<List<HistoricalMistake>> = dao.getHistoricalMistakes()
        .stateInViewModel(emptyList())

    // Helper extension for state flow binding
    private fun <T> kotlinx.coroutines.flow.Flow<T>.stateInViewModel(initialValue: T): StateFlow<T> {
        return this.stateIn(
            scope = viewModelScope,
            started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
            initialValue = initialValue
        )
    }

    // --- Temporary UI States ---
    private val _solveLoading = MutableStateFlow(false)
    val solveLoading: StateFlow<Boolean> = _solveLoading.asStateFlow()

    private val _solveResult = MutableStateFlow<String?>(null)
    val solveResult: StateFlow<String?> = _solveResult.asStateFlow()

    private val _smartIngestionLoading = MutableStateFlow(false)
    val smartIngestionLoading: StateFlow<Boolean> = _smartIngestionLoading.asStateFlow()

    private val _smartIngestionResult = MutableStateFlow<String?>(null)
    val smartIngestionResult: StateFlow<String?> = _smartIngestionResult.asStateFlow()

    // --- Timer/Focus State (Biometric Protocol) ---
    private val _focusTimerSeconds = MutableStateFlow(1500) // 25 mins default
    val focusTimerSeconds: StateFlow<Int> = _focusTimerSeconds.asStateFlow()

    private val _isFocusActive = MutableStateFlow(false)
    val isFocusActive: StateFlow<Boolean> = _isFocusActive.asStateFlow()

    private val _gazeTrackingTriggered = MutableStateFlow(false)
    val gazeTrackingTriggered: StateFlow<Boolean> = _gazeTrackingTriggered.asStateFlow()

    // --- Battle Arena Websocket Matchmacking Emulation ---
    private val _battleState = MutableStateFlow<BattleState>(BattleState.Idle)
    val battleState: StateFlow<BattleState> = _battleState.asStateFlow()

    // --- Premium Checkout Subscription State ---
    private val _premiumProcessingStatus = MutableStateFlow<CheckoutStatus>(CheckoutStatus.Idle)
    val premiumProcessingStatus: StateFlow<CheckoutStatus> = _premiumProcessingStatus.asStateFlow()

    // --- Mock Exams Engine ---
    private val _mockExamState = MutableStateFlow<MockExamState?>(null)
    val mockExamState: StateFlow<MockExamState?> = _mockExamState.asStateFlow()

    // --- Weakness Chart Coordinates ---
    val weaknessValues = mapOf(
        "Math" to 0.85f,
        "Physics" to 0.78f,
        "Chemistry" to 0.52f,
        "Biology" to 0.91f,
        "English" to 0.61f
    )

    init {
        // Run database priming on background thread
        viewModelScope.launch(Dispatchers.IO) {
            val existing = dao.getUserProfileSynchronous()
            if (existing == null) {
                dao.insertUserProfile(UserProfile()) // insert default profile
                primeSampleData()
            }
        }
    }

    private suspend fun primeSampleData() {
        // Prime standard notes in Second Brain Vault
        dao.insertBrainItem(
            BrainItem(
                type = "notes",
                title = "Maxwell's Equations Derivation Core",
                content = "Evaluating \u222e B \u22c5 dA = 0 and dynamic electromagnetic flux density vectors in a cylindrical cavity matrix.",
                subject = "Physics"
            )
        )
        dao.insertBrainItem(
            BrainItem(
                type = "mistakes",
                title = "Redox Balancing Acidic Solution Error",
                content = "Forgot water addition multipliers on LHS. Standard formulation mapping is CBSE JEE NCERT track.",
                subject = "Chemistry"
            )
        )

        // World Spaced Repetitions (World Cards)
        dao.insertWorldCard(
            WorldCard(
                front = "What is the primary solving equation for relativistic mass dilation?",
                back = "m = m0 / sqrt(1 - v^2/c^2)",
                subject = "Physics",
                retentionStrength = 0.45f
            )
        )
        dao.insertWorldCard(
            WorldCard(
                front = "State the first law of thermodynamics conceptually.",
                back = "dU = dQ - dW; total energy in a closed system is conserved.",
                subject = "Physics",
                retentionStrength = 0.90f
            )
        )
        dao.insertWorldCard(
            WorldCard(
                front = "Define NCTB Dhaka HSC standard standard gravity value constraints.",
                back = "9.8 m/s^2 but specific exam board math accepts 9.81 m/s^2 depending on coordinate markers.",
                subject = "Physics",
                retentionStrength = 0.20f
            )
        )

        // Prime historical mistake
        dao.insertHistoricalMistake(
            HistoricalMistake(
                subject = "Physics",
                question = "Determine terminal velocity inside liquid with viscosity eta.",
                userAnswer = "v = 2 r^2 g / (9 eta)",
                correctAnswer = "v = 2 r^2 g (rho_object - rho_liquid) / (9 eta)",
                conceptsMissed = "Buoyant fluid density adjustment parameters."
            )
        )
    }

    // --- State Operations ---

    fun updateProfileSettings(
        track: String,
        lang: String,
        name: String,
        uni: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val current = dao.getUserProfileSynchronous() ?: UserProfile()
            val updated = current.copy(
                curriculumTrack = track,
                languagePreference = lang,
                displayName = name,
                missionTarget = uni
            )
            dao.insertUserProfile(updated)
        }
    }

    fun earnAuraPoints(points: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val current = dao.getUserProfileSynchronous() ?: UserProfile()
            val newAura = current.auraPoints + points
            // Level up mechanics
            val tier = when {
                newAura >= 20000 -> "Elite 1%"
                newAura >= 10000 -> "Diamond"
                newAura >= 5000 -> "Platinum"
                newAura >= 2500 -> "Gold"
                else -> "Bronze"
            }
            dao.insertUserProfile(current.copy(auraPoints = newAura, leagueTier = tier))
        }
    }

    fun updateMentorPersona(persona: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val current = dao.getUserProfileSynchronous() ?: UserProfile()
            dao.insertUserProfile(current.copy(activeMentorPersona = persona))
        }
    }

    // --- BIOMETRIC STATE FOCUS PROTOCOL ---

    fun startFocusSession(durationMinutes: Int) {
        _focusTimerSeconds.value = durationMinutes * 60
        _isFocusActive.value = true
        _gazeTrackingTriggered.value = false
        triggerFocusTick()
    }

    fun stopFocusSession() {
        _isFocusActive.value = false
    }

    private fun triggerFocusTick() {
        viewModelScope.launch {
            while (_isFocusActive.value && _focusTimerSeconds.value > 0) {
                delay(1000)
                if (_isFocusActive.value) {
                    _focusTimerSeconds.value -= 1
                    // Simulated random distraction warning to mimic gaze vector front-camera logic every 45 secs
                    if (_focusTimerSeconds.value % 45 == 0) {
                        _gazeTrackingTriggered.value = true
                    }
                }
            }
            if (_focusTimerSeconds.value == 0 && _isFocusActive.value) {
                _isFocusActive.value = false
                earnAuraPoints(150) // completed deep work session gives 150 Aura!
            }
        }
    }

    fun clearGazeAlert() {
        _gazeTrackingTriggered.value = false
    }

    // --- WORLD CARDS (spaced repetition) ACTIONS ---

    fun addWorldCard(front: String, back: String, subject: String) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.insertWorldCard(
                WorldCard(
                    front = front,
                    back = back,
                    subject = subject,
                    retentionStrength = 0.2f
                )
            )
        }
    }

    fun reviewWorldCard(card: WorldCard, isCorrect: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            val newRetention = if (isCorrect) {
                (card.retentionStrength + 0.15f).coerceAtMost(1.0f)
            } else {
                (card.retentionStrength - 0.25f).coerceAtLeast(0.05f)
            }
            val checkInterval = if (isCorrect) 86400000 * 3 else 30000 // reschedule soon or far
            dao.insertWorldCard(
                card.copy(
                    retentionStrength = newRetention,
                    lastReviewed = System.currentTimeMillis(),
                    nextReview = System.currentTimeMillis() + checkInterval
                )
            )
            earnAuraPoints(if (isCorrect) 10 else 2)
        }
    }

    fun deleteWorldCard(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteWorldCard(id)
        }
    }

    // --- SECOND BRAIN VAULT ACTIONS ---

    fun addBrainNote(title: String, content: String, subject: String, type: String = "notes") {
        viewModelScope.launch(Dispatchers.IO) {
            dao.insertBrainItem(
                BrainItem(
                    title = title,
                    content = content,
                    subject = subject,
                    type = type
                )
            )
        }
    }

    fun deleteBrainNote(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteBrainItem(id)
        }
    }

    // --- NEURAL SOLVER COMPILATION (GEMINI INTEGRATION) ---

    fun callNeuralSolver(prompt: String, subject: String) {
        viewModelScope.launch {
            _solveLoading.value = true
            _solveResult.value = null
            val profile = dao.getUserProfileSynchronous() ?: UserProfile()
            val result = withContext(Dispatchers.IO) {
                GeminiClient.solveAcademicProblem(
                    prompt = prompt,
                    subject = subject,
                    curriculumTrack = profile.curriculumTrack,
                    language = profile.languagePreference,
                    persona = profile.activeMentorPersona
                )
            }
            _solveResult.value = result
            _solveLoading.value = false

            // Save this question to mistakes log randomly to build user profile error banks
            if (prompt.length > 5 && result.contains("Conclusion")) {
                withContext(Dispatchers.IO) {
                    dao.insertHistoricalMistake(
                        HistoricalMistake(
                            subject = subject,
                            question = prompt,
                            userAnswer = "Incomplete/Skipped derivation variables",
                            correctAnswer = "See neural solver step breakdown",
                            conceptsMissed = "First principles conceptual binding"
                        )
                    )
                }
            }
        }
    }

    fun clearSolverResult() {
        _solveResult.value = null
    }

    // --- SMART CONTENT ENGINE SUMMARIZER ---

    fun ingestSmartContent(sourceUrl: String, subject: String) {
        viewModelScope.launch {
            _smartIngestionLoading.value = true
            _smartIngestionResult.value = null
            val profile = dao.getUserProfileSynchronous() ?: UserProfile()
            val rawJson = withContext(Dispatchers.IO) {
                GeminiClient.summarizeAndFlashcards(
                    contentSource = "Analyze this educational dynamic reference: $sourceUrl",
                    subject = subject,
                    curriculumTrack = profile.curriculumTrack
                )
            }
            _smartIngestionResult.value = rawJson
            _smartIngestionLoading.value = false

            // Automatically map returned output into notes & flashcards if possible
            try {
                val cleanJson = if (rawJson.contains("{")) {
                    rawJson.substring(rawJson.indexOf("{"), rawJson.lastIndexOf("}") + 1)
                } else rawJson

                val obj = JSONObject(cleanJson)
                val summary = obj.optString("summary", "Summarization finalized.")
                val flashcardsArray = obj.optJSONArray("flashcards")

                withContext(Dispatchers.IO) {
                    dao.insertBrainItem(
                        BrainItem(
                            type = "summary",
                            title = "Instant Summary: $subject",
                            content = summary,
                            subject = subject
                        )
                    )

                    if (flashcardsArray != null) {
                        for (i in 0 until flashcardsArray.length()) {
                            val cardObj = flashcardsArray.getJSONObject(i)
                            dao.insertWorldCard(
                                WorldCard(
                                    front = cardObj.getString("q"),
                                    back = cardObj.getString("a"),
                                    subject = subject
                                )
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                // Keep raw result
            }
        }
    }

    // --- BATTLE ARENA DUEL MATCHMAKER (WEBSOCKET EMULATOR) ---

    fun startBattleSearch(subject: String) {
        viewModelScope.launch {
            _battleState.value = BattleState.Searching
            delay(3500) // Simulated find matchmaking lookup
            val opponentName = listOf(
                "JEE_Titan_99", "CambridgePhysic_God", "SAT_Sniper",
                "MitHustler", "NCTB_SSC_Topper", "Stamford_A1"
            ).random()
            val opponentElo = (1300..1650).random()
            val userElo = 1400 // start base

            _battleState.value = BattleState.Active(
                opponentName = opponentName,
                opponentElo = opponentElo,
                subject = subject,
                currentQuestionIndex = 0,
                questionsList = generateSpeedQuestions(subject),
                userScore = 0,
                opponentScore = 0,
                secondsRemaining = 15
            )
            triggerBattleTimer()
        }
    }

    private fun generateSpeedQuestions(subject: String): List<SpeedQuestion> {
        return when (subject) {
            "Physics" -> listOf(
                SpeedQuestion(
                    text = "If velocity matches v = 3t² + 2t. Find kinetic force acceleration at t=2.",
                    choices = listOf("14 m/s²", "12 m/s²", "10 m/s²", "16 m/s²"),
                    correctIndex = 0
                ),
                SpeedQuestion(
                    text = "Which thermal system parameter remains constant during isochoric shifts?",
                    choices = listOf("Entropy", "Volume", "Pressure", "Enthalpy"),
                    correctIndex = 1
                ),
                SpeedQuestion(
                    text = "A satellite loops radius R. Orbits double speeds. Required gravitational force change factor?",
                    choices = listOf("Double", "Half", "Quadruple", "Identical"),
                    correctIndex = 2
                )
            )
            else -> listOf(
                SpeedQuestion(
                    text = "Select prime factor solving x² - 5x + 6 = 0 coordinates.",
                    choices = listOf("(x-3)(x-2)", "(x-1)(x-6)", "(x-5)(x+1)", "(x+3)(x+2)"),
                    correctIndex = 0
                ),
                SpeedQuestion(
                    text = "Derivative of function y = ln(4x³) at center x=1 equals?",
                    choices = listOf("1", "4", "3", "12"),
                    correctIndex = 2
                ),
                SpeedQuestion(
                    text = "Find determinant vector trace value inside identity matrices size 3x3.",
                    choices = listOf("1", "3", "0", "9"),
                    correctIndex = 0
                )
            )
        }
    }

    private fun triggerBattleTimer() {
        viewModelScope.launch {
            while (true) {
                val state = _battleState.value
                if (state !is BattleState.Active) break
                delay(1000)
                val curr = _battleState.value as? BattleState.Active ?: break

                // Opponent gains random scores periodically
                val randomOpponentGain = if (Math.random() > 0.65) (20..50).random() else 0

                if (curr.secondsRemaining <= 1) {
                    // Question expired or complete
                    if (curr.currentQuestionIndex < curr.questionsList.size - 1) {
                        _battleState.value = curr.copy(
                            currentQuestionIndex = curr.currentQuestionIndex + 1,
                            secondsRemaining = 15,
                            opponentScore = curr.opponentScore + randomOpponentGain
                        )
                    } else {
                        // Duel ended
                        val userFinal = curr.userScore
                        val oppFinal = curr.opponentScore + randomOpponentGain
                        val isUserWinner = userFinal >= oppFinal
                        val auraDiff = if (isUserWinner) 250 else -100

                        _battleState.value = BattleState.Completed(
                            winner = if (isUserWinner) "Competitor Alpha" else curr.opponentName,
                            userScoreFinal = userFinal,
                            opponentScoreFinal = oppFinal,
                            auraTransferred = auraDiff
                        )
                        earnAuraPoints(auraDiff)
                        break
                    }
                } else {
                    _battleState.value = curr.copy(
                        secondsRemaining = curr.secondsRemaining - 1,
                        opponentScore = curr.opponentScore + randomOpponentGain
                    )
                }
            }
        }
    }

    fun answerBattleQuestion(choiceIndex: Int) {
        val state = _battleState.value as? BattleState.Active ?: return
        val currentQ = state.questionsList[state.currentQuestionIndex]
        val pointsEarned = if (choiceIndex == currentQ.correctIndex) {
            100 + (state.secondsRemaining * 5) // time bonus!
        } else {
            0
        }

        viewModelScope.launch {
            if (state.currentQuestionIndex < state.questionsList.size - 1) {
                // Next Q
                _battleState.value = state.copy(
                    userScore = state.userScore + pointsEarned,
                    currentQuestionIndex = state.currentQuestionIndex + 1,
                    secondsRemaining = 15
                )
            } else {
                // End Battle
                val finalUser = state.userScore + pointsEarned
                val finalOpp = state.opponentScore + (0..40).random()
                val userWin = finalUser >= finalOpp
                val auraTx = if (userWin) 250 else -100

                _battleState.value = BattleState.Completed(
                    winner = if (userWin) "Competitor Alpha" else state.opponentName,
                    userScoreFinal = finalUser,
                    opponentScoreFinal = finalOpp,
                    auraTransferred = auraTx
                )
                earnAuraPoints(auraTx)
            }
        }
    }

    fun dismissBattle() {
        _battleState.value = BattleState.Idle
    }

    // --- ADVERSARIAL MOCK ENGINE TESTS ---

    fun generateAdversarialMock(subject: String) {
        _mockExamState.value = MockExamState.Loading
        viewModelScope.launch {
            delay(2000)
            _mockExamState.value = MockExamState.Active(
                title = "HOSTILE ADVERSARIAL MOCK: $subject",
                questions = listOf(
                    "Evaluate terminal dynamic buoyancy when viscosity parameters are missing in your historical error bank.",
                    "Derivations of relativistic mass transformations for critical coordinates ($subject track).",
                    "Synthesize organic acidic reactions mapping NCERT standard CBSE benchmarks, highlighting where your profile lost 24 accuracy last week."
                ),
                answers = mutableMapOf()
            )
        }
    }

    fun submitMockAnswers(answers: Map<Int, String>) {
        _mockExamState.value = MockExamState.Completed(
            markedScorePercent = (70..95).random(),
            criticalReview = "Accuracy trends normal. Relativistic calculations resolved correctly, but fluid velocity boundary checks require daily Master Streak protocols. Warning: Do not drop study intervals.",
            gainedAura = 300
        )
        earnAuraPoints(300)
    }

    fun exitMockExam() {
        _mockExamState.value = null
    }

    // --- CRYPTO PREMIUM PAYMENT INDEXER (MOCK) ---

    fun triggerPremiumCryptoySubscription(chain: String, txHash: String) {
        _premiumProcessingStatus.value = CheckoutStatus.VerifyingHash
        viewModelScope.launch {
            delay(3000) // simulated blockchain verification delay via REST explorer
            if (txHash.length > 10) {
                // success
                _premiumProcessingStatus.value = CheckoutStatus.VerifiedSuccess
                val p = dao.getUserProfileSynchronous() ?: UserProfile()
                dao.insertUserProfile(p.copy(isPremium = true))
                earnAuraPoints(500) // premium status bonus aura!
            } else {
                _premiumProcessingStatus.value = CheckoutStatus.FailedVerification("Invalid/truncated blockchain receipt transaction hash.")
            }
        }
    }

    fun dismissPremiumCheckout() {
        _premiumProcessingStatus.value = CheckoutStatus.Idle
    }
}

// --- Dynamic Simulation State Models ---

sealed interface BattleState {
    object Idle : BattleState
    object Searching : BattleState
    data class Active(
        val opponentName: String,
        val opponentElo: Int,
        val subject: String,
        val currentQuestionIndex: Int,
        val questionsList: List<SpeedQuestion>,
        val userScore: Int,
        val opponentScore: Int,
        val secondsRemaining: Int
    ) : BattleState
    data class Completed(
        val winner: String,
        val userScoreFinal: Int,
        val opponentScoreFinal: Int,
        val auraTransferred: Int
    ) : BattleState
}

data class SpeedQuestion(
    val text: String,
    val choices: List<String>,
    val correctIndex: Int
)

sealed interface CheckoutStatus {
    object Idle : CheckoutStatus
    object VerifyingHash : CheckoutStatus
    object VerifiedSuccess : CheckoutStatus
    data class FailedVerification(val errorMsg: String) : CheckoutStatus
}

sealed interface MockExamState {
    object Loading : MockExamState
    data class Active(
        val title: String,
        val questions: List<String>,
        val answers: MutableMap<Int, String>
    ) : MockExamState
    data class Completed(
        val markedScorePercent: Int,
        val criticalReview: String,
        val gainedAura: Int
    ) : MockExamState
}
