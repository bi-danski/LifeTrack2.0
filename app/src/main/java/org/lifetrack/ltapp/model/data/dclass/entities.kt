package org.lifetrack.ltapp.model.data.dclass

import androidx.room.PrimaryKey
import androidx.room.Entity

@Entity(tableName = "demChats")
data class Message(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val text: String,
    val isUser: Boolean,
    val timeStamp: String
//    val timestamp: Long = System.currentTimeMillis()
)