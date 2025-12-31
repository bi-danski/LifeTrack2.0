package org.lifetrack.ltapp.presenter

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.lifetrack.ltapp.model.data.dto.Message
import org.lifetrack.ltapp.model.repository.ChatRepository

class ChatPresenter(
    private val chatRepository: ChatRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    companion object {
        const val TYPE_GENERAL = "general"
        const val TYPE_ALMA = "alma"
    }

    private val _chatInput = MutableStateFlow(savedStateHandle["chat_draft"] ?: "")
    val chatInput = _chatInput.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    val dbChats: StateFlow<List<Message>> = chatRepository.getChatFlow(TYPE_GENERAL)
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    val almaChats: StateFlow<List<Message>> = chatRepository.getChatFlow(TYPE_ALMA)
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    fun onMessageInput(value: String) {
        _chatInput.value = value
        savedStateHandle["chat_draft"] = value
    }

    fun sendUserMessageToDoctor() {
        val content = _chatInput.value
        if (content.isBlank()) return

        viewModelScope.launch {
            chatRepository.addChat(
                Message(
                    id = 0,
                    text = content,
                    isFromPatient = true,
                    timestamp = System.currentTimeMillis(),
                    type = TYPE_GENERAL
                )
            )
            clearInput()
        }
    }

    fun sendUserMessageToAlma() {
        val content = _chatInput.value
        if (content.isBlank()) return

        viewModelScope.launch {
            chatRepository.addChat(
                Message(
                    id = 0,
                    text = content,
                    isFromPatient = true,
                    timestamp = System.currentTimeMillis(),
                    type = TYPE_ALMA
                )
            )
            clearInput()

            _isLoading.value = true
            delay(1000)

            chatRepository.addChat(
                Message(
                    id = 0,
                    text = "AI: ...",
                    isFromPatient = false,
                    timestamp = System.currentTimeMillis(),
                    type = TYPE_ALMA
                )
            )
            _isLoading.value = false
        }
    }

    private fun clearInput() {
        _chatInput.value = ""
        savedStateHandle["chat_draft"] = ""
    }
}
