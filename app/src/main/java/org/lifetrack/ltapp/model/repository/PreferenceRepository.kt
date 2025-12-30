package org.lifetrack.ltapp.model.repository

import androidx.datastore.core.DataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import org.lifetrack.ltapp.model.data.dclass.LTPreferences
import org.lifetrack.ltapp.model.data.dclass.TokenPreferences
import java.io.IOException


class PreferenceRepository(
    private val ltDataStore: DataStore<LTPreferences>,
    private val tokenDataStore: DataStore<TokenPreferences>,
    private val scope: CoroutineScope
) {

    val ltPreferences: StateFlow<LTPreferences> = ltDataStore.data
        .catch { e ->
            if (e is IOException) emit(LTPreferences()) else throw e
        }
        .stateIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = LTPreferences()
        )

    val tokenPreferences: StateFlow<TokenPreferences> = tokenDataStore.data
        .catch { e ->
            if (e is IOException) emit(TokenPreferences()) else throw e
        }
        .stateIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = TokenPreferences()
        )

    suspend fun updateLTPreferences(transform: (LTPreferences) -> LTPreferences) {
        ltDataStore.updateData { transform(it) }
    }

    suspend fun updateTokens(accessToken: String?, refreshToken: String?) {
        tokenDataStore.updateData { current ->
            current.copy(
                accessToken = accessToken,
                refreshToken = refreshToken
            )
        }
    }

    suspend fun clearSession() {

        tokenDataStore.updateData {
            TokenPreferences()
        }

//        ltDataStore.updateData {
//            LTPreferences()
//        }
    }
}