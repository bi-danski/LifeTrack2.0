package org.lifetrack.ltapp.model.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import org.lifetrack.ltapp.model.roomdb.ChatDao
import org.lifetrack.ltapp.model.data.dto.Message
import org.lifetrack.ltapp.core.utility.toEntity
import org.lifetrack.ltapp.presenter.ChatPresenter

class ChatRepository(
    private var dao: ChatDao
) {
    private val chatsFlow: Flow<List<Message>> = dao.getAllChats()

    fun getChatFlow(type: String): Flow<List<Message>> {
        return dao.getChatsByType(type)
    }
    fun getMessagesByChatId(chatId: String): Flow<List<Message>> {
        return dao.getChatsById(chatId)
    }

    suspend fun addChat(chat: Message){
        dao.insertChat(chat.toEntity())
    }

    suspend fun clearAllChats(){
        dao.deleteAllChats()
    }

    suspend fun clearAlmaHistory(){
        dao.deleteChatsByType(ChatPresenter.TYPE_ALMA)
    }

    suspend fun getChatCounts(): Int {
        return chatsFlow.first().size
    }

}