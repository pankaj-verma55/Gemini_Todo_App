package com.example.todoapp.Data

import android.util.Log
import com.example.todoapp.Domain.GeminiRepInterface
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend

class GeminiRepositoryImp(): GeminiRepInterface {
    // Vertex AI backend is used for Firebase integration
    private val model = Firebase.ai(
        backend = GenerativeBackend.googleAI()
    ).generativeModel(
        modelName = "gemini-3.6-flash"
    )

    override suspend fun askGemini(prompt: String): Result<String> {
        return try {
            Log.d("GeminiRepo", "Requesting AI for: $prompt")
            val response = model.generateContent(prompt)
            val responseText = response.text
            
            if (!responseText.isNullOrEmpty()) {
                Log.d("GeminiRepo", "Response successful")
                Result.success(responseText)
            } else {
                Log.w("GeminiRepo", "Response was empty. Check safety filters in Firebase console.")
                Result.failure(Exception("AI returned an empty response. It might be blocked by safety filters."))
            }
        } catch (e: Exception) {
            Log.e("GeminiRepo", "API Error: ${e.message}", e)
            Result.failure(e)
        }
    }
}