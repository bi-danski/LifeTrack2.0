package org.lifetrack.ltapp.network

import android.util.Log
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
import io.ktor.client.request.HttpRequestPipeline
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.encodedPath
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.lifetrack.ltapp.core.exception.NoInternetException
import org.lifetrack.ltapp.model.datastore.TokenPreferences
import org.lifetrack.ltapp.model.repository.PreferenceRepository

object KtorHttpClient {
    fun create(prefs: PreferenceRepository, networkObserver: NetworkObserver, koinScope: CoroutineScope): HttpClient {
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
                connectTimeoutMillis = 10000
                socketTimeoutMillis = 20000
            }

            install(DefaultRequest) {
                url("https://lifetrack-1071288890438.us-central1.run.app")
                header(HttpHeaders.ContentType, ContentType.Application.Json)
            }

            install("ConnectivityCheck") {
                requestPipeline.intercept(HttpRequestPipeline.Before) {
                    if (!networkObserver.isConnected.value) {
                        throw NoInternetException()
                    }
                    proceed()
                }
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
                        val currentTokens = prefs.tokenPreferences.first()
                        val oldRefreshToken = currentTokens.refreshToken ?: return@refreshTokens null

                        try {
                            val response = client.post("/auth/refresh") {
                                markAsRefreshTokenRequest()
                                setBody(mapOf("token" to oldRefreshToken))
                            }

                            if (response.status == HttpStatusCode.OK) {
                                val newTokens = response.body<TokenPreferences>()

                                prefs.updateTokenPreferences(
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
                            Log.e("KtorAuth", "Rotation attempt failed", e)
                            null
                        }
                    }

                    sendWithoutRequest { request ->
                        val path = request.url.encodedPath
                        path.endsWith("/auth/login") ||
                                path.endsWith("/auth/signup") ||
                                path.endsWith("/auth/refresh")
                    }
                }
            }

            HttpResponseValidator {
                validateResponse { response ->
                    val path = response.call.request.url.encodedPath
                    if (response.status == HttpStatusCode.Unauthorized && path.endsWith("/auth/refresh")) {
                        Log.e("KtorValidator", "Refresh token invalid. Wiping all Session data.")
                        koinScope.launch {
                            prefs.clearAllSessions()
                        }
                    }
                }
            }

            install(Logging) {
                level = LogLevel.INFO
                sanitizeHeader { it == HttpHeaders.Authorization }
            }
        }
    }
}