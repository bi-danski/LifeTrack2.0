package org.lifetrack.ltapp.core.events

sealed class AuthUiEvent {
    data object LoginSuccess : AuthUiEvent()
    data class Error(val message: String) : AuthUiEvent()
}