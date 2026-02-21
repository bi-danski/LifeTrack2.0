package org.lifetrack.ltapp.model.repository

import androidx.datastore.core.DataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.lifetrack.ltapp.model.datastore.AppPreferences
import org.lifetrack.ltapp.model.datastore.LTPreferences
import org.lifetrack.ltapp.model.datastore.TokenPreferences
import org.lifetrack.ltapp.model.datastore.UserPreferences
import java.io.IOException

class PreferenceRepository(
    private val appDataStore: DataStore<AppPreferences>,
    private val scope: CoroutineScope
) {
    val appPreferences: StateFlow<AppPreferences> = appDataStore.data
        .catch { e ->
            if (e is IOException) emit(AppPreferences()) else throw e
        }.stateIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = AppPreferences()
        )

    val tokenPreferences = appPreferences
        .map { it.tokenDatastore }
        .distinctUntilChanged()

    val userPreferences = appPreferences
        .map { it.userDatastore }
        .distinctUntilChanged()

    val ltPreferences = appPreferences
        .map { it.ltDatastore }
        .distinctUntilChanged()

    private suspend fun updateAppPreferences(transform: (AppPreferences) -> AppPreferences) {
        appDataStore.updateData { current -> transform(current) }
    }

    suspend fun updateTokenPreferences(accessToken: String?, refreshToken: String?) {
        updateAppPreferences { it.copy(
            tokenDatastore = it.tokenDatastore.copy(
                accessToken = accessToken,
                refreshToken = refreshToken
            )
        )}
    }

    suspend fun updateUserPreferences(transform: (UserPreferences) -> UserPreferences) {
        updateAppPreferences { it.copy(userDatastore = transform(it.userDatastore)) }
    }

    suspend fun updateLTPreferences(transform: (LTPreferences) -> LTPreferences) {
        updateAppPreferences { it.copy(ltDatastore = transform(it.ltDatastore)) }
    }

    suspend fun clearAllSessions() {
        updateAppPreferences {
            it.copy(
                userDatastore = UserPreferences(),
                tokenDatastore = TokenPreferences()
            )
        }
    }
}