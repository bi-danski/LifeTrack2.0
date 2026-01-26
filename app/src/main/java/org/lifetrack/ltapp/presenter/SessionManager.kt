package org.lifetrack.ltapp.presenter

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.lifetrack.ltapp.model.data.dclass.SessionStatus
import org.lifetrack.ltapp.model.repository.AuthRepository
import org.lifetrack.ltapp.model.repository.PreferenceRepository

class SessionManager(
    private val prefRepository: PreferenceRepository,
    private val authRepository: AuthRepository,
    private val koinScope: CoroutineScope
) {
    val sessionState: StateFlow<SessionStatus> = prefRepository.tokenPreferences
        .map { if (it.accessToken.isNullOrBlank()) SessionStatus.LOGGED_OUT else SessionStatus.LOGGED_IN }
        .stateIn(
            koinScope,
            SharingStarted.Eagerly,
            SessionStatus.INITIALIZING
        )

    fun logout() {
        koinScope.launch {
            authRepository.logout()
            prefRepository.clearAllSessions()
        }
    }
}