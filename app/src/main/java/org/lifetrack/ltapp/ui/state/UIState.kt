package org.lifetrack.ltapp.ui.state

sealed class UIState {
    object Idle : UIState()
    object Loading : UIState()
    data class Success(val message: String? = null) : UIState()
    data class Error(val msg: String, val isNetworkError: Boolean = false) : UIState()
}