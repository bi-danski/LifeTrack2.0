package org.lifetrack.ltapp.core.utility

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.ui.unit.LayoutDirection
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter


object ZetuZetuUtil {

    operator fun PaddingValues.times(value: Float): PaddingValues = PaddingValues(
        top = calculateTopPadding() * value,
        bottom = calculateBottomPadding() * value,
        start = calculateStartPadding(LayoutDirection.Ltr) * value,
        end = calculateEndPadding(LayoutDirection.Ltr) * value
    )

    fun generateInitials(name: String): String {
        return name.split(" ")
            .filter { it.isNotBlank() }
            .map { it.first() }
            .joinToString("")
            .uppercase()
    }

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

    fun sanitizeErrorMessage(e: Exception): String {
        val localizedMsg = e.localizedMessage!!
        return when (e) {
            is io.ktor.client.network.sockets.ConnectTimeoutException, is java.net.ConnectException -> "Unable to connect to the server. Please check your internet connection."
            is io.ktor.client.plugins.HttpRequestTimeoutException -> "The request took too long. Please try again."
            else ->
                if (localizedMsg.contains("https")) {
                   localizedMsg
                       .substringAfter(".app")
                       .replace(Regex("[\\\\(){}]"), "")
                       .replace(Regex("/[^/\\s]+/[^/\\s]+"), "")
                       .replace(Regex("\\s+"), " ")
                       .trim()
                }else{
                    localizedMsg
                }
        }
    }

}