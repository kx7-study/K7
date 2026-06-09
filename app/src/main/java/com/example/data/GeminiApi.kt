package com.example.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit
import com.example.BuildConfig

@JsonClass(generateAdapter = true)
data class GeminiPart(
    @Json(name = "text") val text: String? = null
)

@JsonClass(generateAdapter = true)
data class GeminiContent(
    @Json(name = "parts") val parts: List<GeminiPart>
)

@JsonClass(generateAdapter = true)
data class GeminiGenerationConfig(
    @Json(name = "temperature") val temperature: Float? = 0.4f,
    @Json(name = "maxOutputTokens") val maxOutputTokens: Int? = 2048
)

@JsonClass(generateAdapter = true)
data class GeminiRequest(
    @Json(name = "contents") val contents: List<GeminiContent>,
    @Json(name = "generationConfig") val generationConfig: GeminiGenerationConfig? = null,
    @Json(name = "systemInstruction") val systemInstruction: GeminiContent? = null
)

@JsonClass(generateAdapter = true)
data class GeminiCandidate(
    @Json(name = "content") val content: GeminiContent? = null,
    @Json(name = "finishReason") val finishReason: String? = null
)

@JsonClass(generateAdapter = true)
data class GeminiResponse(
    @Json(name = "candidates") val candidates: List<GeminiCandidate>? = null
)

interface GeminiService {
    @POST("v1beta/models/gemini-3.5-flash:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String,
        @Body request: GeminiRequest
    ): GeminiResponse
}

object GeminiClient {
    private const val BASE_URL = "https://generativelanguage.googleapis.com/"

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    val service: GeminiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
        retrofit.create(GeminiService::class.java)
    }

    suspend fun solveAcademicProblem(
        prompt: String,
        subject: String,
        curriculumTrack: String,
        language: String,
        persona: String
    ): String {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            if (subject == "Universal AI") {
                return """
                    ### 🎯 Core Answer
                    **[SIMULATED UNIVERSAL AI STAGE]** Active logic flow decoded successfully. I am fully configured to perform multi-subject general reasoning, translation, and code compilation.
                    
                    ### 🔍 Step-by-Step Breakdown
                    1. **Secure Sandbox Verification**: The local KX7-STUDY enterprise layer verified incoming request structure: "$prompt".
                    2. **Hardware Check**: Checked for configured live credentials in the platform's SECRETS panel on the sidebar.
                    3. **Simulated Synapse Output**: To experience live real-time ChatGPT/Gemini level intelligence, make sure to add the valid key.
                    
                    ### ⚡ 1% Mastery Key
                    Configure your live GEMINI_API_KEY via the Secrets Panel to bypass simulation limits and instantly unlock the full power of real-world AI logic synthesis!
                """.trimIndent()
            }
            return """
                ### 🎯 Core Answer
                **The simulated academic result is 1.602 x 10^-19 Joules per event.** This represents the dynamic energy threshold of physical states.

                ### 🔍 Step-by-Step Breakdown
                1. **Extract Variables under $curriculumTrack rules**: Current subject target parameter set to $subject ($language mode). Dynamic kinetic constant k = 8.85 x 10^-12 F/m, and system velocity v = 4.2 x 10^6 m/s.
                2. **Apply Persona Logic ($persona mode)**: We analyze system forces utilizing first-principles where Force Equation becomes net force: F = q * (E + v x B).
                3. **Dimensional Consistency Review**: Checked calculation parameters against localized board exam standards. Every coefficient is confirmed mathematically.

                ### ⚡ 1% Mastery Key
                Always verify your electrostatic dimensional units first. Examiners frequently mismatch voltage ratios to trip up 99% of students!
            """.trimIndent()
        }

        val systemPrompt = if (subject == "Universal AI") {
            """
                You are Gemini, the world's most advanced and highly general-purpose AI model running inside the KX7-STUDY ecosystem.
                You assist students by explaining technical, coding, or lifestyle ideas clearly.
                
                You MUST strictly follow these formatting rules:
                1. STRICT NO-LATEX POLICY: Never print raw backend LaTeX mathematical notation blocks like \[ ... \] or \frac{}{}. Instead, write all equations in clean, bold, highly legible plain text characters (for example, write "v^2 = u^2 + 2as" or "E = mc^2").
                2. TYPOGRAPHIC SCANNABILITY: Break dense knowledge blocks down into bite-sized paragraphs with a maximum of 2 sentences per line. 
                3. MANDATORY RESPONSE SEQUENCE: Every output must follow this absolute structure with zero variations:

                ### 🎯 Core Answer
                [Bold, high-visibility, definitive response or numerical calculation value first].

                ### 🔍 Step-by-Step Breakdown
                [Clean, numbered logical execution steps explaining the exact path to the solution].

                ### ⚡ 1% Mastery Key
                [A single-sentence tactical trick, formula shortcut, or common board exam trap to avoid].
            """.trimIndent()
        } else {
            """
                You are the core Neural Solver module of the KX7-STUDY enterprise platform: "The Operating System for the 1% Student".
                Your primary goal is to guide students to ultimate academic dominance.
                Adapt your response strictly to the following parameters:
                - SUBJECT: $subject
                - CURRENT TRACK: $curriculumTrack (Bangladesh NCTB / USA SAT-AP / Cambridge UK / India JEE-NCERT)
                - LANGUAGE PREFERENCE: $language (Translate dynamically if needed, use precise academic terminology)
                - TEACHING PERSONA: Apply the style of '$persona' mode:
                  * 'Einstein': deep first-principles physics, conceptual foundations.
                  * 'Fischer': hyper-efficient, cold, direct, mathematical logic.
                  * 'Feynman': beautiful intuitive analogies, simple explanations of complex jargon.
                  * 'Military Mode': strict discipline, tough love, high-intensity accountability, zero excuses.
                  * 'Exam Assassin': hyper-tactical mark-scheme optimization, direct examiner advice.

                You MUST strictly follow these formatting rules:
                1. STRICT NO-LATEX POLICY: Never print raw backend LaTeX mathematical notation blocks like \[ ... \] or \frac{}{}. Instead, write all equations in clean, bold, highly legible plain text characters (for example, write "v^2 = u^2 + 2as" or "E = mc^2").
                2. TYPOGRAPHIC SCANNABILITY: Break dense knowledge blocks down into bite-sized paragraphs with a maximum of 2 sentences per line. 
                3. MANDATORY RESPONSE SEQUENCE: Every output must follow this absolute structure with zero variations:

                ### 🎯 Core Answer
                [Bold, high-visibility, definitive response or numerical calculation value first].

                ### 🔍 Step-by-Step Breakdown
                [Clean, numbered logical execution steps explaining the exact path to the solution].

                ### ⚡ 1% Mastery Key
                [A single-sentence tactical trick, formula shortcut, or common board exam trap to avoid].
            """.trimIndent()
        }

        val request = GeminiRequest(
            contents = listOf(GeminiContent(parts = listOf(GeminiPart(text = prompt)))),
            systemInstruction = GeminiContent(parts = listOf(GeminiPart(text = systemPrompt)))
        )

        return try {
            val response = service.generateContent(apiKey, request)
            response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                ?: "Execution completed, but no candidates returned. Please refine your query parameters."
        } catch (e: Exception) {
            "Neural Engine communication error: ${e.localizedMessage ?: "Unknown operational delay"}"
        }
    }

    suspend fun summarizeAndFlashcards(
        contentSource: String,
        subject: String,
        curriculumTrack: String
    ): String {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            return """
                {
                  "summary": "This premium lecture introduces electromagnetic spectrum boundary metrics under the $curriculumTrack $subject framework. It maps frequency harmonics against signal attenuation vectors.",
                  "flashcards": [
                    {"q": "What defines the attenuation coefficient in this board?", "a": "The structural impedance ratio multiplier"},
                    {"q": "Name the key threshold constant applied", "a": "Plank's reduced constant limit h-bar"}
                  ]
                }
            """.trimIndent()
        }

        val systemPrompt = """
            Analyze the provided educational text, video transcript, or lecture note under the curriculum track '$curriculumTrack' for '$subject'.
            Output a JSON block in the exact following template format:
            {
              "summary": "A concise executive executive study summary matching the board's high-yield terms",
              "flashcards": [
                {"q": "question 1", "a": "answer 1"},
                {"q": "question 2", "a": "answer 2"}
              ]
            }
            Do not output any markdown other than the raw JSON itself.
        """.trimIndent()

        val request = GeminiRequest(
            contents = listOf(GeminiContent(parts = listOf(GeminiPart(text = contentSource)))),
            systemInstruction = GeminiContent(parts = listOf(GeminiPart(text = systemPrompt)))
        )

        return try {
            val response = service.generateContent(apiKey, request)
            response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                ?: ""
        } catch (e: Exception) {
            ""
        }
    }
}
