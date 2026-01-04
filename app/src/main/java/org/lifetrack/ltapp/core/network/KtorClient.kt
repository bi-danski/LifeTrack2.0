package org.lifetrack.ltapp.core.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpCallValidator
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
//import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode
import io.ktor.http.encodedPath
import io.ktor.serialization.kotlinx.json.json
//import kotlinx.coroutines.flow.filter
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
                socketTimeoutMillis = 15000
            }

            install(HttpCallValidator) {
                handleResponseExceptionWithRequest { exception, _ ->
                    android.util.Log.e("KtorValidator", "Network/Protocol Exception: ${exception.message}")
                }
            }

            HttpResponseValidator {
                validateResponse { response ->
                    if (response.status == HttpStatusCode.Unauthorized) {
                        android.util.Log.e("KtorValidator", "Unauthorized! Clearing session...")
                        prefs.clearTokenPreferences()
                    }
                }
            }

            install(DefaultRequest) {
                url("https://lifetrack-1071288890438.us-central1.run.app")
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
                        } else {
                            null
                        }
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
                                prefs.updateTokens(newTokens.accessToken, newTokens.refreshToken)
                                BearerTokens(
                                    accessToken = newTokens.accessToken ?: "",
                                    refreshToken = newTokens.refreshToken ?: ""
                                )
                            } else {
                                android.util.Log.e("KtorAuth", "Refresh failed: ${response.status}")
                                prefs.clearUserPreferences()
                                null
                            }
                        } catch (e: Exception) {
                            android.util.Log.e("KtorAuth", "Exception during refresh", e)
                            null
                        }
                    }

                    sendWithoutRequest { request ->
                        val path = request.url.encodedPath
                        path.contains("/auth/login") ||
                                path.contains("/auth/signup") ||
                                path.contains("/auth/refresh")
                    }
                }
            }

//            if (BuildConfig.DEBUG) {
            install(Logging) {
//                    logger = object : Logger {
//                        override fun log(message: String) {
//                            val maxLogSize = 3500
//                            for (i in 0..message.length / maxLogSize) {
//                                val start = i * maxLogSize
//                                var end = (i + 1) * maxLogSize
//                                end = if (end > message.length) message.length else end
//                                android.util.Log.d("KtorClient", message.substring(start, end))
//                            }
//                        }
//                    }
                level = LogLevel.INFO
                sanitizeHeader { header -> header == "Authorization" }
//                }
            }
        }
    }
}