package org.lifetrack.ltapp.model.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.lifetrack.ltapp.model.database.room.MessageEntity
import org.lifetrack.ltapp.model.data.dto.Message

@Dao
interface ChatDao {
    @Query("SELECT * FROM demChats ORDER BY timestamp DESC")
    fun getAllChats(): Flow<List<Message>>
//    @Query("INSERT INTO demChats (id, text, isFromPatient, timestamp) VALUES ( :chat.id, :text, :isFromPatient, :timestamp)")
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChat(chat: MessageEntity)

    @Query("DELETE FROM demChats")
    suspend fun deleteAllChats()
}