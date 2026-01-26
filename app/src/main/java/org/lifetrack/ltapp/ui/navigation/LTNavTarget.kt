package org.lifetrack.ltapp.ui.navigation

sealed class LTNavTarget {
    data class Screen(
        val route: String,
        val clearBackstack: Boolean = false,
        val launchSingleTop: Boolean = true
    ) : LTNavTarget()

    object Back : LTNavTarget()
}