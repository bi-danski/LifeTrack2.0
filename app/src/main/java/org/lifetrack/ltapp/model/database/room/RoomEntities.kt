package org.lifetrack.ltapp.model.database.room

import androidx.room.PrimaryKey
import androidx.room.Entity

@Entity(tableName = "demChats")
data class MessageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: String,
    val text: String,
    val isFromPatient: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)