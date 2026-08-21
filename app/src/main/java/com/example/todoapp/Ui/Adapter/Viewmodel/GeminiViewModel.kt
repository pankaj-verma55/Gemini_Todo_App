package com.example.todoapp.Ui.Adapter.Viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.Domain.UseCase.AskGeminiUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GeminiViewModel(
    private val askGeminiUseCase: AskGeminiUseCase
) : ViewModel() {

    private val _response = MutableStateFlow("")
    val response = _response.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun askGemini(prompt: String) {
        viewModelScope.launch {
            _isLoading.value = true
            // Reset response so the UI knows a new request started
            _response.value = "" 

            askGeminiUseCase(prompt)
                .onSuccess { result ->
                    _response.value = result
                }
                .onFailure { error ->
                    // Show error details in the UI
                    _response.value = "Error: ${error.localizedMessage ?: "Unknown error"}"
                }

            _isLoading.value = false
        }
    }
}