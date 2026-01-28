package org.lifetrack.ltapp.ui.navigation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

object LTNavDispatcher {
    private val _navigationEvents = Channel<LTNavTarget>(Channel.BUFFERED)
    val navigationEvents = _navigationEvents.receiveAsFlow()

    private val _currentRoute = mutableStateOf("home")
    val currentRoute: State<String> = _currentRoute

    private const val NAVIGATION_DEBOUNCE_THRESHOLD = 500L
    private var lastEventTime = 0L

    fun navigate(
        route: String,
        clearBackstack: Boolean = false,
        launchSingleTop: Boolean = true
    ) {
        if (canExecuteEvent()) {
            _navigationEvents.trySend(
                LTNavTarget.Screen(
                    route = route,
                    clearBackstack = clearBackstack,
                    launchSingleTop = launchSingleTop
                )
            )
        }
    }

    fun navigateBack() {
        if (canExecuteEvent()) {
            _navigationEvents.trySend(LTNavTarget.Back)
        }
    }

    fun updateCurrentRoute(route: String?) {
        route?.let {
            _currentRoute.value = it
        }
    }

    private fun canExecuteEvent(): Boolean {
        val currentTime = android.os.SystemClock.elapsedRealtime()
        return if (currentTime - lastEventTime > NAVIGATION_DEBOUNCE_THRESHOLD) {
            lastEventTime = currentTime
            true
        } else {
            false
        }
    }
}