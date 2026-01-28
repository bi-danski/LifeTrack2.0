package org.lifetrack.ltapp.presenter

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.lifetrack.ltapp.model.data.dto.Message
import org.lifetrack.ltapp.model.data.dto.UserPrompt
import org.lifetrack.ltapp.model.repository.ChatRepository
import org.lifetrack.ltapp.service.AlmaService
import java.util.UUID

class ChatPresenter(
    private val chatRepository: ChatRepository, private val savedStateHandle: SavedStateHandle, private val almaService: AlmaService
) : ViewModel() {

    companion object {
        const val TYPE_ALMA = "alma"
        private const val KEY_CHAT_DRAFT = "chat_draft"
        private const val KEY_CURRENT_CHAT_ID = "current_chat_id"
    }
    val chatInput: StateFlow<String> = savedStateHandle.getStateFlow(KEY_CHAT_DRAFT, "")
    val chatId: StateFlow<String> = savedStateHandle.getStateFlow(
        KEY_CURRENT_CHAT_ID,
        UUID.randomUUID().toString()
    )
    val chatHistory: StateFlow<List<Message>> = chatRepository.getUniqueSessions()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val almaChats: StateFlow<List<Message>> = chatId
        .flatMapLatest { id -> chatRepository.getMessagesByChatId(id) }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()


    fun deleteChat(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            chatRepository.deleteChatSession(id)
            if (chatId.value == id) {
                withContext(Dispatchers.Main) {
                    startNewChat()
                }
            }
        }
    }

    fun renameChat(id: String, newName: String) {
        if (newName.isBlank()) return
        viewModelScope.launch(Dispatchers.IO) {
            chatRepository.renameChatSession(id, newName)
        }
    }

    fun onMessageInput(value: String) {
        savedStateHandle[KEY_CHAT_DRAFT] = value
    }
    fun setChatSession(id: String) {
        savedStateHandle[KEY_CURRENT_CHAT_ID] = id
    }
    fun startNewChat() {
        savedStateHandle[KEY_CURRENT_CHAT_ID] = UUID.randomUUID().toString()
        clearInput()
    }

    fun chatWithAlma() {
        val content = chatInput.value
        val activeChatId = chatId.value
        if (content.isBlank()) return

        viewModelScope.launch {
            chatRepository.addChat(createMessage(content, activeChatId, true))
            clearInput()
            _isLoading.value = true
            try {
                val response = withContext(Dispatchers.IO) {
                    almaService.promptAssistant(UserPrompt(activeChatId, content))
                }
                chatRepository.addChat(createMessage(response, activeChatId, false))
            } catch (e: Exception) {
                Log.e("AlmaChat", "Error: ${e.localizedMessage}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun createMessage(text: String, sid: String, isPatient: Boolean) = Message(
        id = 0, text = text, chatId = sid, isFromPatient = isPatient,
        timestamp = System.currentTimeMillis(), type = TYPE_ALMA
    )

    private fun clearInput() {
        savedStateHandle[KEY_CHAT_DRAFT] = ""
    }
}