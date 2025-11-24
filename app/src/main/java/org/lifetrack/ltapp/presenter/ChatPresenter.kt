package org.lifetrack.ltapp.presenter

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import org.lifetrack.ltapp.model.data.dummyMessages
import org.lifetrack.ltapp.model.dto.Message
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class ChatPresenter(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    var messageInput by mutableStateOf("")
        private set

    var messages = mutableStateListOf<Message>()
        private set

    init {
        val savedMessages = savedStateHandle.get<List<Message>>("myChats")
        when {
            savedMessages != null && savedMessages.isNotEmpty() -> {
                messages.addAll(savedMessages)
            }
            else -> {
                messages.addAll(dummyMessages)
            }
        }
    }

    fun onMessageInput(value: String) {
        messageInput = value
    }

    fun onNewMessage() {
        if (messageInput.isBlank()) return

        val message = Message(
            (messages.size + 1).toString(),
            messageInput,
            true,
            now()
        )
        messages.add(message)
        messageInput = ""
        savedStateHandle["myChats"] = messages
    }

    fun sendUserMessage() = onNewMessage()

    override fun onCleared() {
        savedStateHandle["myChats"] = messages
        super.onCleared()
    }

    private fun now(): String {
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        return LocalTime.now().format(formatter)
    }
}
