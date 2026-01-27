package org.lifetrack.ltapp.model.roomdb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {
    @Query("SELECT * FROM demChats ORDER BY timestamp DESC")
    fun getAllChats(): Flow<List<MessageEntity>>

    @Query("SELECT * FROM demChats WHERE type = :chatType ORDER BY timestamp DESC")
    fun getChatsByType(chatType: String): Flow<List<MessageEntity>>

    @Query("SELECT * FROM demChats WHERE chatId = :chatId ORDER BY timestamp DESC")
    fun getChatsById(chatId: String): Flow<List<MessageEntity>>

    @Query("SELECT * FROM demChats GROUP BY chatId ORDER BY timestamp DESC")
    fun getUniqueChatSessions(): Flow<List<MessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChat(chat: MessageEntity)

    @Query("DELETE FROM demChats WHERE chatId = :chatId")
    suspend fun deleteChatsBySessionId(chatId: String)

    @Query("UPDATE demChats SET text = :newName WHERE chatId = :chatId AND id = (SELECT MIN(id) FROM demChats WHERE chatId = :chatId)")
    suspend fun updateChatSessionName(chatId: String, newName: String)

    @Query("DELETE FROM demChats WHERE type = :chatType")
    suspend fun deleteChatsByType(chatType: String)

    @Query("DELETE FROM demChats")
    suspend fun deleteAllChats()
}