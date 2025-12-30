package org.lifetrack.ltapp.model.network

import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode
import io.ktor.http.encodedPath
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.lifetrack.ltapp.model.data.dclass.TokenPreferences
import org.lifetrack.ltapp.model.repository.PreferenceRepository

object KtorHttpClient {
    fun create(prefs: PreferenceRepository): HttpClient {
        return HttpClient(Android) {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }

            install(DefaultRequest) {
                url("https://lifetrack-1071288890438.us-central1.run.app")
            }

            install(Auth) {
                bearer {
                    loadTokens {
                        val currentTokens = prefs.tokenPreferences.value
                        if (!currentTokens.accessToken.isNullOrBlank()) {
                            BearerTokens(
                                accessToken = currentTokens.accessToken,
                                refreshToken = currentTokens.refreshToken ?: ""
                            )
                        } else null
                    }

                    refreshTokens {
                        val currentPrefs = prefs.tokenPreferences.value
                        val oldRefreshToken = currentPrefs.refreshToken ?: return@refreshTokens null

                        try {
                            val response = client.post("auth/refresh") {
                                markAsRefreshTokenRequest()
                                setBody(mapOf("refreshToken" to oldRefreshToken))
                            }
                            if (response.status == HttpStatusCode.OK) {
                                val newTokens = response.body<TokenPreferences>()
                                prefs.updateTokens(newTokens.accessToken, newTokens.refreshToken)
                                BearerTokens(
                                    accessToken = newTokens.accessToken ?: "",
                                    refreshToken = newTokens.refreshToken ?: ""
                                )
                            } else {
                                prefs.updateTokens(null, null)
                                null
                            }
                        } catch (_: Exception) {
                            prefs.updateTokens(null, null)
                            null
                        }
                    }

                    sendWithoutRequest { request ->
                        !request.url.encodedPath.startsWith("/auth")
                    }
                }
            }
        }
    }
}