package org.lifetrack.ltapp.presenter

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.lifetrack.ltapp.core.service.AlmaService
import org.lifetrack.ltapp.model.data.dto.Message
import org.lifetrack.ltapp.model.data.dto.UserPrompt
import org.lifetrack.ltapp.model.repository.ChatRepository
import java.util.UUID
//import androidx.compose.material.icons.filled.Menu

class ChatPresenter(
    private val chatRepository: ChatRepository,
    private val savedStateHandle: SavedStateHandle,
    private val almaService: AlmaService
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

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    val chatHistory: StateFlow<List<Message>> = chatRepository.getUniqueSessions()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val almaChats: StateFlow<List<Message>> = chatId
        .flatMapLatest { id ->
            chatRepository.getMessagesByChatId(id)
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

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
            chatRepository.addChat(
                Message(
                    id = 0,
                    text = content,
                    chatId = activeChatId,
                    isFromPatient = true,
                    timestamp = System.currentTimeMillis(),
                    type = TYPE_ALMA
                )
            )
            clearInput()

            _isLoading.value = true
            try {
                val aiResponseText = withContext(Dispatchers.IO) {
                    almaService.promptAssistant(UserPrompt(activeChatId, content))
                }
                saveToRoom(aiResponseText, activeChatId, isFromPatient = false)
            } catch (e: Exception) {
                Log.e("ChatPresenter::chatWithAlma(): ", "Error: ${e.localizedMessage}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    private suspend fun saveToRoom(text: String, sessionId: String, isFromPatient: Boolean) {
        chatRepository.addChat(
            Message(
                id = 0,
                text = text,
                isFromPatient = isFromPatient,
                timestamp = System.currentTimeMillis(),
                type = TYPE_ALMA,
                chatId = sessionId
            )
        )
    }

    private fun clearInput() {
        savedStateHandle[KEY_CHAT_DRAFT] = ""
    }
}