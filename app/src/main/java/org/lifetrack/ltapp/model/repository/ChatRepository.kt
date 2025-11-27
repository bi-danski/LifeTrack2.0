package org.lifetrack.ltapp.model.repository

import kotlinx.coroutines.flow.Flow
import org.lifetrack.ltapp.model.data.dao.ChatDao
import org.lifetrack.ltapp.model.data.dto.Message

class ChatRepository(
    private var dao: ChatDao
) {
    val chatsFlow: Flow<List<Message>> = dao.getAllChats()

    suspend fun addChat(chat: Message){
        dao.insertChat(chat)
    }

    suspend fun deleteAllChats(){
        dao.deleteAllChats()
    }
}