package org.lifetrack.ltapp.presenter

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.lifetrack.ltapp.model.data.AlmaMessage


class AlmaPresenter(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    var userInput by mutableStateOf("")
        private set

//    var chats = mutableStateListOf<AlmaMessage>()
//        private set

    private val _allChats = MutableStateFlow(mutableListOf<AlmaMessage>())
    val allChats = _allChats.asStateFlow()

    var isLoading by mutableStateOf(false)
        private set

//    init {
//        val saved = savedStateHandle.get<List<AlmaMessage>>("chats")
//        if (saved != null) {
//            chats.addAll(saved)
//        }
//    }

    fun onUserInputChange(value: String) {
        userInput = value
    }

    fun onMessagesUpdate(msg: AlmaMessage) {
        allChats.value.add(msg)
        savedStateHandle["chats"] = allChats.value
    }

    fun sendMessage() {
        if (userInput.isBlank()) return

        onMessagesUpdate(AlmaMessage(userInput, true))
        userInput = ""
        isLoading = true

        viewModelScope.launch {
            delay(1000)

            onMessagesUpdate(AlmaMessage("AI: ...", false))
            isLoading = false
        }
    }
}
