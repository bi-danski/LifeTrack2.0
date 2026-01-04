package org.lifetrack.ltapp.model.repository

import kotlinx.coroutines.flow.Flow
import org.lifetrack.ltapp.model.data.dao.ChatDao
import org.lifetrack.ltapp.model.data.dto.Message
import org.lifetrack.ltapp.core.utility.toEntity

class ChatRepository(
    private var dao: ChatDao
) {
    private val chatsFlow: Flow<List<Message>> = dao.getAllChats()
    private var chatCounts: Int = 0

    fun getChatFlow(type: String): Flow<List<Message>> {
        return dao.getChatsByType(type)
    }

    suspend fun addChat(chat: Message){
        dao.insertChat(chat.toEntity())
    }

    suspend fun deleteAllChats(){
        dao.deleteAllChats()
    }

    suspend fun getChatCounts():Int {
        chatsFlow.collect {
            chatCounts = it.size
        }
        return chatCounts
    }

}