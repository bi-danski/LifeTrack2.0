package org.lifetrack.ltapp.utils

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun formatTimestamp(timestamp: Long): String {
    val date = Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDate()
    val time = Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalTime()
        .format(DateTimeFormatter.ofPattern("hh:mm a"))

    val today = LocalDate.now()
    val yesterday = today.minusDays(1)

    return when (date) {
        today -> "$time"
        yesterday -> "Yesterday • $time"
        else -> date.format(DateTimeFormatter.ofPattern("dd MMM yyyy")) + " • $time"
    }
}

//    fun now(): String {
//        return LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))
//    }