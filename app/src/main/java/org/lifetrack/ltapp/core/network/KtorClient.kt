package org.lifetrack.ltapp.core.network

//import io.ktor.client.plugins.logging.Logger
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.DefaultRequest
//import io.ktor.client.plugins.HttpCallValidator
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.serialization.json.Json
import org.lifetrack.ltapp.BuildConfig
import org.lifetrack.ltapp.model.data.dclass.TokenPreferences
import org.lifetrack.ltapp.model.repository.PreferenceRepository

object KtorHttpClient {
    fun create(prefs: PreferenceRepository): HttpClient {
        return HttpClient(Android) {
            expectSuccess = true

            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = BuildConfig.DEBUG
                    isLenient = true
                    ignoreUnknownKeys = true
                    encodeDefaults = true
                    }
                )
            }

//            install(HttpCallValidator) {
//                handleResponseExceptionWithRequest { exception, _ ->
//                    android.util.Log.e("KtorValidator", "Network/Protocol Exception: ${exception.message}")
//                }
//            }

//            HttpResponseValidator {
//                validateResponse { response ->
//                    if (response.status == HttpStatusCode.Unauthorized) {
//                        android.util.Log.e("KtorValidator", "Unauthorized! Clearing session...")
//                        prefs.clearTokenPreferences()
//                    }
//                }
//            }
            install(HttpTimeout) {
                requestTimeoutMillis = 30000
                connectTimeoutMillis = 10000
                socketTimeoutMillis = 30000
            }

            install(DefaultRequest) {
                url("https://lifetrack-1071288890438.us-central1.run.app")
            }

            install(Auth) {
                bearer {

                    loadTokens {
                        val currentTokens = prefs.tokenPreferences.filter {
                            it.accessToken != null && it.refreshToken != null
                        }.first()

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
                            android.util.Log.d("KtorAuth", "401 Detected: Attempting Token Refresh...")

                            val response = client.post("/auth/refresh") {
                                markAsRefreshTokenRequest()
                                setBody(mapOf("token" to oldRefreshToken))
                            }

                            if (response.status == HttpStatusCode.OK) {
                                val newTokens = response.body<TokenPreferences>()
                                prefs.updateTokens(newTokens.accessToken, newTokens.refreshToken)

                                android.util.Log.d("KtorAuth","Refresh Success. Retrying original request.")

                                BearerTokens(
                                    accessToken = newTokens.accessToken ?: "",
                                    refreshToken = newTokens.refreshToken ?: ""
                                )
                            } else {
                                android.util.Log.e("KtorAuth","Refresh failed: ${response.status}. Clearing Session.")

                                prefs.clearTokenPreferences()
                                null
                            }
                        } catch (e: Exception) {
                            android.util.Log.e("KtorAuth", "Exception during refresh", e)
                            null
                        }
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