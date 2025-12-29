package org.lifetrack.ltapp.core.utils

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDateTime
import java.time.Instant
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

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

fun LocalDateTime.customFormat(pattern: String): String {
    val formatter = DateTimeFormatter.ofPattern(pattern)
    return this.toJavaLocalDateTime().format(formatter)
}

fun LocalDate.toYearMonth(): YearMonth = YearMonth.from(this)

fun YearMonth.formatMonthYear(): String {
    val month = month.getDisplayName(TextStyle.FULL, Locale.getDefault())
    return "$month $year"
}