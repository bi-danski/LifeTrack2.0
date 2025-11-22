package org.lifetrack.ltapp.ui.state

sealed class UIState {
    object Idle: UIState()
    object Loading: UIState()
    object Success: UIState()
    data class Error(val msg: String): UIState()
}