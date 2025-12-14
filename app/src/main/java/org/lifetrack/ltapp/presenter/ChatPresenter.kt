package org.lifetrack.ltapp.presenter

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.lifetrack.ltapp.model.data.dto.Message
import org.lifetrack.ltapp.model.repository.ChatRepository
import org.lifetrack.ltapp.utils.toEntity
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class ChatPresenter(
    private val chatRepository: ChatRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val chatInput = MutableStateFlow("")
    val myChats: StateFlow<List<Message>> =
        chatRepository.chatsFlow.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    fun onMessageInput(value: String) {
        chatInput.value= value
    }

    @OptIn(ExperimentalUuidApi::class)
    fun sendUserMessage() {
        if(chatInput.value.isBlank()) return
        viewModelScope.launch {
            chatRepository.addChat(
                Message(
                    id = Uuid.random().toHexString(),
                    text = chatInput.value,
                    isFromPatient = true,
//                    timestamp = now()
                )
            )
        }
        chatInput.value = ""
    }

    override fun onCleared() {
//        savedStateHandle["myChats"] = _messages.value
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
