package org.lifetrack.ltapp.core.events

sealed class AuthUiEvent {
    object LoginSuccess : AuthUiEvent()
    object SignupSuccess: AuthUiEvent()
}