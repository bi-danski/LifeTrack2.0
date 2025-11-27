package org.lifetrack.ltapp.model.data.dao

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.lifetrack.ltapp.model.data.dto.Message

interface ChatDao {
    @Query("SELECT * FROM demChats ORDER BY id ASC")
    fun getAllChats(): Flow<List<Message>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertChat(chat: Message)

    @Query("DELETE FROM demChats")
    suspend fun deleteAllChats()
}