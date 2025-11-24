package org.lifetrack.ltapp.presenter

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.lifetrack.ltapp.model.data.dummyMessages
import org.lifetrack.ltapp.model.dto.Message
import java.time.LocalTime
import java.time.format.DateTimeFormatter


class ChatPresenter(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages = _messages.asStateFlow()

    var messageInput = MutableStateFlow("")

    init {
        val saved = savedStateHandle.get<List<Message>>("myChats")
        _messages.value = saved ?: dummyMessages
    }

    fun onMessageInput(value: String) {
        messageInput.value = value
    }

    fun sendUserMessage() {
        val text = messageInput.value
        if (text.isBlank()) return

        val newMessage = Message(
            (_messages.value.size + 1).toString(),
            text,
            true,
            now()
        )

        val updated = _messages.value + newMessage
        _messages.value = updated
        savedStateHandle["myChats"] = updated
        messageInput.value = ""
    }

    override fun onCleared() {
        savedStateHandle["myChats"] = _messages.value
    }

    private fun now(): String {
        return LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))
    }
}











































//class ChatPresenter(
//    private val savedStateHandle: SavedStateHandle
//) : ViewModel() {
//
//    var messageInput by mutableStateOf("")
//        private set
//
//    var messages = mutableStateListOf<Message>()
//        private set
//
//    init {
//        val savedMessages = savedStateHandle.get<List<Message>>("myChats")
//        when {
//            savedMessages != null && savedMessages.isNotEmpty() -> {
//                messages.addAll(savedMessages)
//            }
//            else -> {
//                messages.addAll(dummyMessages)
//            }
//        }
//    }
//
//    fun onMessageInput(value: String) {
//        messageInput = value
//    }
//
//    fun onNewMessage() {
//        if (messageInput.isBlank()) return
//
//        val message = Message(
//            (messages.size + 1).toString(),
//            messageInput,
//            true,
//            now()
//        )
//        messages.add(message)
//        messageInput = ""
//        savedStateHandle["myChats"] = messages
//    }
//
//    fun sendUserMessage() = onNewMessage()
//
//    override fun onCleared() {
//        savedStateHandle["myChats"] = messages
//        super.onCleared()
//    }
//
//    private fun now(): String {
//        val formatter = DateTimeFormatter.ofPattern("HH:mm")
//        return LocalTime.now().format(formatter)
//    }
//}
