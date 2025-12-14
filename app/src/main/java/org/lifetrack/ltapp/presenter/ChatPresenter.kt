//package org.lifetrack.ltapp.presenter
//
//import androidx.lifecycle.SavedStateHandle
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.SharingStarted
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.flow.combine
//import kotlinx.coroutines.flow.stateIn
//import kotlinx.coroutines.launch
//import org.lifetrack.ltapp.model.data.dto.Message
//import org.lifetrack.ltapp.model.repository.ChatRepository
//
//class ChatPresenter(
//    private val chatRepository: ChatRepository,
//    private val savedStateHandle: SavedStateHandle
//) : ViewModel() {
//
//    // --- SHARED INPUT STATE ---
//    private val _chatInput = MutableStateFlow(savedStateHandle["chat_draft"] ?: "")
//    val chatInput = _chatInput.asStateFlow()
//
//    // --- SHARED LOADING STATE ---
//    private val _isLoading = MutableStateFlow(false)
//    val isLoading = _isLoading.asStateFlow()
//
//    // --- DATABASE MESSAGES (For Main Chat) ---
//    // This flow comes directly from Room DB
//    val dbChats: StateFlow<List<Message>> = chatRepository.getChatFlow()
//        .stateIn(
//            viewModelScope,
//            SharingStarted.WhileSubscribed(5000),
//            emptyList()
//        )
//
//    // --- ALMA (AI) MESSAGES ---
//    // If you want Alma chats to be temporary (like your original AlmaPresenter), keep them in memory.
//    // If you want them saved, you should add a 'type' column to your DB and query by type.
//    // For now, let's keep Alma separate in memory as requested by your original code structure.
//    private val _almaChats = MutableStateFlow<List<Message>>(emptyList())
//    val almaChats = _almaChats.asStateFlow()
//
//    init {
//        // Restore Alma chats if process died
//        val savedAlma = savedStateHandle.get<List<Message>>("alma_history")
//        if (savedAlma != null) {
//            _almaChats.value = savedAlma
//        }
//    }
//
//    // --- INPUT HANDLER ---
//    fun onMessageInput(value: String) {
//        _chatInput.value = value
//        savedStateHandle["chat_draft"] = value
//    }
//
//    // --- ACTION: SEND TO DATABASE (ChatScreen) ---
//    fun sendUserMessageToDb() {
//        val content = _chatInput.value
//        if (content.isBlank()) return
//
//        viewModelScope.launch {
//            // Save to DB
//            chatRepository.addChat(
//                Message(
//                    id = 0,
//                    text = content,
//                    isFromPatient = true,
//                    timestamp = System.currentTimeMillis()
//                )
//            )
//            clearInput()
//        }
//    }
//
//    // --- ACTION: SEND TO ALMA (AlmaScreen) ---
//    fun sendUserMessageToAlma() {
//        val content = _chatInput.value
//        if (content.isBlank()) return
//
//        viewModelScope.launch {
//            // 1. Add User Message to List
//            val userMsg = Message(
//                id = System.currentTimeMillis(), // Temporary ID
//                text = content,
//                isFromPatient = true,
//                timestamp = System.currentTimeMillis()
//            )
//            updateAlmaList(userMsg)
//            clearInput()
//
//            // 2. Simulate AI Response
//            _isLoading.value = true
//            delay(1000) // Fake Network Delay
//
//            val aiMsg = Message(
//                id = System.currentTimeMillis() + 1,
//                text = "AI Response: I heard you say '$content'",
//                isFromPatient = false,
//                timestamp = System.currentTimeMillis()
//            )
//            updateAlmaList(aiMsg)
//            _isLoading.value = false
//        }
//    }
//
//    private fun updateAlmaList(newMessage: Message) {
//        val currentList = _almaChats.value.toMutableList()
//        currentList.add(newMessage)
//        _almaChats.value = currentList
//        savedStateHandle["alma_history"] = currentList
//    }
//
//    private fun clearInput() {
//        _chatInput.value = ""
//        savedStateHandle["chat_draft"] = ""
//    }
//}
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
