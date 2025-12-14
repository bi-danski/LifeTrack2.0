package org.lifetrack.ltapp.utils

import org.lifetrack.ltapp.model.database.room.MessageEntity
import org.lifetrack.ltapp.model.data.dto.Message

fun Message.toEntity(): MessageEntity{
    return MessageEntity(
        id = this.id,
        text = this.text,
        isFromPatient = this.isFromPatient,
        timestamp = this.timestamp,
    )
}