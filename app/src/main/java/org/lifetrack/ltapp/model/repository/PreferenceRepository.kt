package org.lifetrack.ltapp.model.repository

import androidx.datastore.core.DataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import org.lifetrack.ltapp.model.data.dclass.LTPreferences
import org.lifetrack.ltapp.model.data.dclass.TokenPreferences
import org.lifetrack.ltapp.model.data.dclass.UserPreferences
import java.io.IOException


class PreferenceRepository(
    private val ltDataStore: DataStore<LTPreferences>,
    private val tokenDataStore: DataStore<TokenPreferences>,
    private val userDataStore: DataStore<UserPreferences>,
    private val scope: CoroutineScope
) {

    val tokenPreferences: StateFlow<TokenPreferences> = tokenDataStore.data
        .catch { e ->
            if (e is IOException) emit(TokenPreferences()) else throw e
        }
        .stateIn(
            scope = scope,
            started = SharingStarted.Eagerly,
            initialValue = TokenPreferences()
        )

    val ltPreferences: StateFlow<LTPreferences> = ltDataStore.data
        .catch { e ->
            if (e is IOException) emit(LTPreferences()) else throw e
        }
        .stateIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = LTPreferences()
        )

    val userPreferences: StateFlow<UserPreferences> = userDataStore.data
        .catch { e ->
            if (e is IOException) emit(UserPreferences()) else throw e
        }.stateIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = UserPreferences()
        )


    suspend fun updateLTPreferences(transform: (LTPreferences) -> LTPreferences) {
        ltDataStore.updateData { transform(it) }
    }

    suspend fun updateUserPreferences(transform: (UserPreferences) -> UserPreferences) {
        userDataStore.updateData { transform(it) }
    }
    suspend fun updateTokens(accessToken: String?, refreshToken: String?) {
        tokenDataStore.updateData { current ->
            current.copy(
                accessToken = accessToken,
                refreshToken = refreshToken
            )
        }
    }

    suspend fun clearTokenPreferences() {
        tokenDataStore.updateData {
            TokenPreferences()
        }
    }

    suspend fun clearUserPreferences(){
        userDataStore.updateData {
            UserPreferences()
        }
    }

    suspend fun clearLTPreferences(){
        ltDataStore.updateData {
            LTPreferences()
        }
    }

    suspend fun clearAllSessions(){
        clearUserPreferences()
        clearTokenPreferences()
    }



}