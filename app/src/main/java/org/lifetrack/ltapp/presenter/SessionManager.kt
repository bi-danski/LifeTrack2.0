package org.lifetrack.ltapp.presenter

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.lifetrack.ltapp.core.utility.toUserPreferences
import org.lifetrack.ltapp.core.utility.toUserProfileInfo
import org.lifetrack.ltapp.model.data.dclass.SessionStatus
import org.lifetrack.ltapp.model.data.dto.SessionInfo
import org.lifetrack.ltapp.model.repository.AuthRepository
import org.lifetrack.ltapp.model.repository.PreferenceRepository

class SessionManager(
    private val prefRepository: PreferenceRepository,
    private val authRepository: AuthRepository,
    private val koinScope: CoroutineScope
) {
    val sessionState: StateFlow<SessionStatus> = prefRepository.tokenPreferences
        .map { tokens ->
            if (tokens.accessToken.isNullOrBlank()) SessionStatus.LOGGED_OUT else SessionStatus.LOGGED_IN
        }
        .distinctUntilChanged()
        .stateIn(
            koinScope,
            SharingStarted.Eagerly,
            SessionStatus.INITIALIZING
        )

    suspend fun logout() {
        withContext(Dispatchers.IO) {
            try {
                authRepository.logout()
            } catch (ex: Exception) {
                Log.e("SessionManager", "Remote logout failed", ex)
            } finally {
                prefRepository.clearAllSessions()
            }
        }
    }

    fun updateSessionPreferences(sessionInfo: SessionInfo) {
        koinScope.launch {
            try {
                if (sessionInfo.accessToken.isNotBlank()) {
                    prefRepository.updateTokenPreferences(
                        accessToken = sessionInfo.accessToken,
                        refreshToken = sessionInfo.refreshToken
                    )
                    prefRepository.updateUserPreferences {
                        sessionInfo.agent47.toUserProfileInfo().toUserPreferences()
                    }
                } else {
                    prefRepository.clearAllSessions()
                }
            } catch (e: Exception) {
                Log.e("SessionManager", "Failed to update session preferences", e)
                prefRepository.clearAllSessions()
            }
        }
    }
}