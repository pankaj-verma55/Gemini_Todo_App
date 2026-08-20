package com.example.todoapp.Domain

interface GeminiRepInterface {

    suspend fun askGemini(prompt: String): Result<String>
}