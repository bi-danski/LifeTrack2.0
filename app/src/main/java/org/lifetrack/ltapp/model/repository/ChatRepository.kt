package org.lifetrack.ltapp.model.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.lifetrack.ltapp.model.roomdb.ChatDao
import org.lifetrack.ltapp.model.data.dto.Message
import org.lifetrack.ltapp.core.utility.toEntity
import org.lifetrack.ltapp.core.utility.toDto // Ensure you have this mapper
import org.lifetrack.ltapp.presenter.ChatPresenter

class ChatRepository(
    private val dao: ChatDao
) {
    private val chatsFlow: Flow<List<Message>> = dao.getAllChats().map { entities ->
        entities.map { it.toDto() }
    }

    fun getChatFlow(type: String): Flow<List<Message>> {
        return dao.getChatsByType(type).map { entities ->
            entities.map { it.toDto() }
        }
    }

    fun getMessagesByChatId(chatId: String): Flow<List<Message>> {
        return dao.getChatsById(chatId).map { entities ->
            entities.map { it.toDto() }
        }
    }

    fun getUniqueSessions(): Flow<List<Message>> {
        return dao.getUniqueChatSessions().map { entities ->
            entities.map { it.toDto() }
        }
    }

    suspend fun addChat(chat: Message) {
        dao.insertChat(chat.toEntity())
    }

    suspend fun clearAllChats() {
        dao.deleteAllChats()
    }

    suspend fun clearAlmaHistory() {
        dao.deleteChatsByType(ChatPresenter.TYPE_ALMA)
    }

    suspend fun getChatCounts(): Int {
        return chatsFlow.first().size
    }
}