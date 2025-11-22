package org.lifetrack.ltapp.presenter

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.lifetrack.ltapp.model.dto.AlmaMessage

class AlmaPresenter(): ViewModel() {

    var userInput by mutableStateOf("")
        private set

    var messages by mutableStateOf(listOf<AlmaMessage>())
        private set

    var isLoading by  mutableStateOf(false)
        private set

    fun onUserInputChange(value: String){
        userInput = value
    }

    fun sendMessage() {
        if (userInput.isBlank()) return

        val userMessage = AlmaMessage(userInput, true)
        messages += userMessage
        userInput = ""
        isLoading = true

        viewModelScope.launch {
            delay(1000)
            val aiMsg = AlmaMessage("AI: ...", false, isUser = false)
            messages += aiMsg
            isLoading = false
        }
    }

}