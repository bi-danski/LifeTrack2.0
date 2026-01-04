package org.lifetrack.ltapp.core.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.encodedPath
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.first
import kotlinx.serialization.json.Json
import org.lifetrack.ltapp.model.data.dclass.TokenPreferences
import org.lifetrack.ltapp.model.repository.PreferenceRepository


object KtorHttpClient {
    fun create(prefs: PreferenceRepository): HttpClient {
        return HttpClient(Android) {
            expectSuccess = true

            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                    encodeDefaults = true
                })
            }

            install(HttpTimeout) {
                requestTimeoutMillis = 15000
                connectTimeoutMillis = 15000
                socketTimeoutMillis = 30000
            }

            HttpResponseValidator {
                validateResponse { response ->
                    val path = response.call.request.url.encodedPath
                    if (response.status == HttpStatusCode.Unauthorized && path.contains("/auth/refresh")) {
                        android.util.Log.e("KtorValidator", "Refresh Token Expired. Hard Logout.")
                        prefs.clearUserPreferences()
                    }
                }
            }

            install(DefaultRequest) {
                url("https://lifetrack-1071288890438.us-central1.run.app")
                header(io.ktor.http.HttpHeaders.ContentType, ContentType.Application.Json)
            }

            install(Auth) {
                bearer {
                    loadTokens {
                        val currentTokens = prefs.tokenPreferences.first()
                        if (!currentTokens.accessToken.isNullOrBlank()) {
                            BearerTokens(
                                accessToken = currentTokens.accessToken,
                                refreshToken = currentTokens.refreshToken ?: ""
                            )
                        } else null
                    }

                    refreshTokens {
                        val currentPrefs = prefs.tokenPreferences.first()
                        val oldRefreshToken = currentPrefs.refreshToken ?: return@refreshTokens null

                        try {
                            val response = client.post("auth/refresh") {
                                markAsRefreshTokenRequest()
                                setBody(mapOf("token" to oldRefreshToken))
                            }
                            if (response.status == HttpStatusCode.OK) {
                                val newTokens = response.body<TokenPreferences>()
                                prefs.updateTokens(
                                    accessToken = newTokens.accessToken,
                                    refreshToken = newTokens.refreshToken
                                )
                                BearerTokens(
                                    accessToken = newTokens.accessToken ?: "",
                                    refreshToken = newTokens.refreshToken ?: ""
                                )
                            } else {
                                null
                            }
                        } catch (e: Exception) {
                            android.util.Log.e("KtorAuth", "Refresh failed", e)
                            null
                        }
                    }

                    sendWithoutRequest { request ->
                        val path = request.url.encodedPath
                        path.contains("/auth/login") ||
                                path.contains("/auth/register") ||
                                path.contains("/auth/refresh")
                    }
                }
            }

            install(Logging) {
                level = LogLevel.ALL
                sanitizeHeader { it == io.ktor.http.HttpHeaders.Authorization }
            }
        }
    }
}