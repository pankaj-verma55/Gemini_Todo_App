package com.example.todoapp.Domain

import com.example.todoapp.Data.GeminiRepositoryImp

class AskGeminiUseCase(
    private val repository: GeminiRepositoryImp
) {

    suspend operator fun invoke(prompt: String): Result<String> {
        return repository.askGemini(prompt)
    }
}