package org.lifetrack.ltapp.core.utility

object ZetuZetuUtil {
    fun generateInitials(name: String): String {
        return name.split(" ")
            .filter { it.isNotBlank() }
            .map { it.first() }
            .joinToString("")
            .uppercase()
    }
}