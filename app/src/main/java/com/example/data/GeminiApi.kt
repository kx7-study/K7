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
                    [SYSTEM WARNING] API key is not configured or is the default placeholder.
                    
                    Please insert a valid GEMINI_API_KEY into the SECRETS panel.
                    
                    ---- Simulated Gemini Universal AI Solver Response ----
                    
                    I am ready to answer any general type of school, coding, or lifestyle question. Since the actual live API key is currently mapped to default placeholders, here is my simulated answer structure:
                    
                    Your Query: "$prompt"
                    
                    Here is what I can do when configured with a real Gemini key:
                    1. Code Synthesis: Writing clean, modern algorithms with detailed comments.
                    2. Strategic Brainstorming: Creating frameworks, blueprints, and analytical plans.
                    3. Ultimate Homework Assistance: Step-by-step guidance on math, logic, translation, or literature.
                    
                    Please configure your live credentials via the Secrets Panel to proceed with true real-world AI reasoning!
                """.trimIndent()
            }
            return """
                [SYSTEM WARNING] API key is not configured or is the default placeholder.
                
                Please insert a valid GEMINI_API_KEY into the SECRETS panel.
                
                Simulated response for:
                Subject: $subject (Track: $curriculumTrack)
                AI Mentor: $persona Mode
                Language: $language
                
                ---- Academic Mastery Solved Step-by-Step ----
                Known variables extracted:
                - Target system velocity parameter: v = 4.2 × 10⁶ m/s
                - Dynamic kinetic field constant: k = 8.85 × 10⁻¹² F/m
                
                Step 1: Apply formulation matching curriculum standards ($curriculumTrack)
                Step 2: Solve with $persona cognitive style. Applying first principles:
                F = q (E + v × B)
                Step 3: Dimensional analysis yields [Newton] units checks out.
                
                Conclusion:
                Value verified mathematically to be 1.602 × 10⁻¹⁹ Joules per event.
            """.trimIndent()
        }

        val systemPrompt = if (subject == "Universal AI") {
            """
                You are Gemini, the world's most advanced and highly general-purpose AI model running inside the KX7-STUDY ecosystem.
                You are designed to assist the user on any question they have, including writing code, creative writing, answering math/science equations, translations, brainstorming ideas, summarizing articles, or explaining complex concepts.
                Be extremely helpful, direct, articulate, and smart. Give clear, detailed responses with rich code blocks, bullet points, or list structures where appropriate.
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

                Response Rules:
                1. Provide a beautiful Step-by-Step Problem Breakdown.
                2. First list **Knowns & Unknowns** and check the units.
                3. Write formulas using beautiful LaTeX-like mathematical formatting (e.g. \( E = m c^2 \)).
                4. Make your response feel premium, elite, and highly intellectual. No generic fluff!
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
