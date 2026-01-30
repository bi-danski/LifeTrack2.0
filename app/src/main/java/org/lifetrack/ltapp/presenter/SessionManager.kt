package org.lifetrack.ltapp.presenter

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.lifetrack.ltapp.model.data.dclass.SessionStatus
import org.lifetrack.ltapp.model.repository.AuthRepository
import org.lifetrack.ltapp.model.repository.PreferenceRepository

class SessionManager(
    private val prefRepository: PreferenceRepository,
    private val authRepository: AuthRepository,
    private val koinScope: CoroutineScope
) {
    val sessionState: StateFlow<SessionStatus> = prefRepository.tokenPreferences
        .map {
            if (it.accessToken.isNullOrBlank()) {
                SessionStatus.LOGGED_OUT
            }else {
                SessionStatus.LOGGED_IN
            }
        }
        .stateIn(
            koinScope,
            SharingStarted.Eagerly,
            SessionStatus.INITIALIZING
        )

    suspend fun logout() {
        withContext(Dispatchers.IO){
            try {
                authRepository.logout()
            }catch (ex: Exception){
                Log.e("SessionManager::logout()", ex.localizedMessage, ex)
            }finally {
                prefRepository.clearAllSessions()
            }
        }
    }

    fun updateSessionTokens(accessToken: String?, refreshToken: String?) {
        koinScope.launch {
            try {
                if (!accessToken.isNullOrBlank() && !refreshToken.isNullOrBlank()) {
                    prefRepository.updateTokens(accessToken, refreshToken)
                } else {
                    prefRepository.clearTokenPreferences()
                }
            } catch (e: Exception) {
                Log.e("SessionManager", "Failed to update session tokens", e)
                prefRepository.clearTokenPreferences()
            }
        }
    }


}