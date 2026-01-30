package org.lifetrack.ltapp.ui.navigation

sealed class LTNavTarget {
    data class Screen(
        val launchSingleTop: Boolean = true,
        val route: String,
        val clearBackstack: Boolean = false,

    ) : LTNavTarget()

    object Back : LTNavTarget()
}

