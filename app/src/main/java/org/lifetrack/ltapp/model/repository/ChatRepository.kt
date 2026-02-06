package org.lifetrack.ltapp.model.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.lifetrack.ltapp.utility.toDto
import org.lifetrack.ltapp.utility.toEntity
import org.lifetrack.ltapp.model.data.dto.Message
import org.lifetrack.ltapp.model.roomdb.ChatDao

class ChatRepository(
    private val dao: ChatDao
) {
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

    suspend fun deleteChatSession(chatId: String) {
        dao.deleteChatsBySessionId(chatId)
    }

    suspend fun renameChatSession(chatId: String, newName: String) {
        dao.updateChatSessionName(chatId, newName)
    }

    suspend fun clearAlmaHistory() {
        dao.deleteChatsByType("alma")
    }
}