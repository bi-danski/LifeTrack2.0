package org.lifetrack.ltapp.core.utility

object ZetuZetuUtil {
    fun generateInitials(name: String): String {
        return name.split(" ")
            .filter { it.isNotBlank() }
            .map { it.first() }
            .joinToString("")
            .uppercase()
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