package org.lifetrack.ltapp.utility

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.ui.unit.LayoutDirection
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.http.HttpStatusCode
import java.net.ConnectException
import java.net.UnknownHostException
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
            .take(2)
            .map { it.first() }
            .joinToString("")
            .uppercase()
    }

    fun formatTimestamp(timestamp: Long): String {
        val zoneId = ZoneId.systemDefault()
        val zonedDateTime = Instant.ofEpochMilli(timestamp).atZone(zoneId)
        val date = zonedDateTime.toLocalDate()
        val time = zonedDateTime.format(DateTimeFormatter.ofPattern("hh:mm a"))

        val today = LocalDate.now()
        val yesterday = today.minusDays(1)

        return when (date) {
            today -> time
            yesterday -> "Yesterday • $time"
            else -> date.format(DateTimeFormatter.ofPattern("dd MMM yyyy")) + " • $time"
        }
    }

    fun sanitizeErrorMessage(e: Exception): String {
        return when (e) {
            is UnknownHostException ->
                "Unable to reach the server. Please check your internet connection."

            is ConnectException ->
                "Connection failed. The server might be down or unreachable."

            is SocketTimeoutException, is HttpRequestTimeoutException ->
                "The request timed out. Please try again later."

            is ClientRequestException -> {
                when (e.response.status) {
                    HttpStatusCode.Unauthorized -> "Invalid email or password. Please try again."
                    HttpStatusCode.BadRequest -> "Invalid credentials format. Please check your input."
                    HttpStatusCode.Conflict -> "This account already exists."
                    HttpStatusCode.Forbidden -> "Access denied. Please contact support."
                    else -> "Client error: ${e.response.status.description}"
                }
            }

            is ServerResponseException -> {
                "Something went wrong on our end. Please try again shortly."
            }

            is ResponseException -> {
                "Network error: ${e.response.status.description}"
            }

            else -> {
                val msg = e.localizedMessage ?: "An unexpected error occurred. Try again shortly"

                if (msg.contains("http")) {
                    msg.substringAfterLast(":")
                        .replace(Regex("[\\\\(){}\\[\\]]"), "")
                        .trim()
                        .ifBlank { "Network request failed." }
                } else {
                    msg
                }
            }
        }
    }
}