package org.lifetrack.ltapp.ui.navigation

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

sealed class NavTarget {
    data class Screen(
        val route: String,
        val clearBackstack: Boolean = false,
        val launchSingleTop: Boolean = true
    ) : NavTarget()

    object Back : NavTarget()
}


object NavDispatcher {
    private val _navigationEvents = Channel<NavTarget>(Channel.BUFFERED)
    val navigationEvents = _navigationEvents.receiveAsFlow()

    fun navigate(route: String, clearBackstack: Boolean = false) {
        _navigationEvents.trySend(NavTarget.Screen(route, clearBackstack))
    }

    fun navigateBack() {
        _navigationEvents.trySend(NavTarget.Back)
    }
}