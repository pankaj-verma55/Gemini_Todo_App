package com.example.todoapp.Ui.Adapter.Viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todoapp.Domain.AskGeminiUseCase

class GeminiViewModelFactory(
    private val askGeminiUseCase: AskGeminiUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (modelClass.isAssignableFrom(GeminiViewModel::class.java)) {
            return GeminiViewModel(askGeminiUseCase) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}