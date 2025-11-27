package org.lifetrack.ltapp.model.repository

import org.lifetrack.ltapp.model.data.dto.Message

interface RoomRepository {
    fun getMyChats(): MutableList<Message>
    fun deleteMyChats()
    fun saveChat(message: Message)
    fun saveMyChats(chats: List<Message>)
}