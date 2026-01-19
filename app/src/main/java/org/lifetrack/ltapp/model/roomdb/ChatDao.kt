package org.lifetrack.ltapp.model.roomdb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.lifetrack.ltapp.model.data.dto.Message

@Dao
interface ChatDao {
    @Query("SELECT * FROM demChats ORDER BY timestamp DESC")
    fun getAllChats(): Flow<List<Message>>

    @Query("SELECT * FROM demChats WHERE type = :chatType ORDER BY timestamp DESC")
    fun getChatsByType(chatType: String): Flow<List<Message>>

    @Query("SELECT * FROM demChats WHERE type = :chatId ORDER BY timestamp DESC")
    fun getChatsById(chatId: String): Flow<List<Message>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChat(chat: MessageEntity)

    @Query("DELETE FROM demChats WHERE type = :chatType")
    suspend fun deleteChatsByType(chatType: String)

    @Query("DELETE FROM demChats")
    suspend fun deleteAllChats()
}