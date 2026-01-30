package org.lifetrack.ltapp.network
//
//import android.util.Log
//import io.ktor.client.HttpClient
//import io.ktor.client.call.body
//import io.ktor.client.engine.android.Android
//import io.ktor.client.plugins.DefaultRequest
//import io.ktor.client.plugins.HttpResponseValidator
//import io.ktor.client.plugins.HttpTimeout
//import io.ktor.client.plugins.auth.Auth
//import io.ktor.client.plugins.auth.providers.BearerTokens
//import io.ktor.client.plugins.auth.providers.bearer
//import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
//import io.ktor.client.plugins.logging.LogLevel
//import io.ktor.client.plugins.logging.Logging

//import io.ktor.client.request.header
//import io.ktor.client.request.post
//import io.ktor.client.request.setBody
//import io.ktor.http.ContentType
//import io.ktor.http.HttpHeaders
//import io.ktor.http.HttpStatusCode
//import io.ktor.http.encodedPath
//import io.ktor.serialization.kotlinx.json.json
//import kotlinx.coroutines.flow.first
//import kotlinx.serialization.json.Json
//import org.lifetrack.ltapp.core.exception.NoInternetException
//import org.lifetrack.ltapp.model.data.dclass.TokenPreferences
//import org.lifetrack.ltapp.model.repository.PreferenceRepository
//
//
//object KtorHttpClient {
//    fun create(prefs: PreferenceRepository, networkObserver: NetworkObserver): HttpClient {
//        return HttpClient(Android) {
//            expectSuccess = true
//
//            install(ContentNegotiation) {
//                json(Json {
//                    prettyPrint = true
//                    isLenient = true
//                    ignoreUnknownKeys = true
//                    encodeDefaults = true
//                })
//            }
//
//            install(HttpTimeout) {
//                requestTimeoutMillis = 15000
//                connectTimeoutMillis = 10000
//                socketTimeoutMillis = 20000
//            }
//
////            install(HttpRequestRetry) {
////                maxRetries = 3
////                retryIf { _, response -> response.status.value in 500..599 }
////                retryOnExceptionIf { _, cause ->
////                    cause is SocketTimeoutException ||
////                            cause is ConnectTimeoutException
////                }
////                delayMillis { retry ->
////                    val baseDelay = retry * 2000L
////                    val jitter = (0..500).random()
////                    baseDelay + jitter
////                }
////            }
//
//            install("ConnectivityCheck") {
//                requestPipeline.intercept(HttpRequestPipeline.Before) {
//                    if (!networkObserver.isConnected.value ) {
//                        throw NoInternetException()
//                    }
//                    proceed()
//                }
//            }
//
//            HttpResponseValidator {
//                validateResponse { response ->
//                    val path = response.call.request.url.encodedPath
//                    if (response.status == HttpStatusCode.Unauthorized && path.contains("/auth/refresh")) {
//                        Log.e("KtorValidator", "Refresh Token Expired. Hard Logout.")
//                        prefs.clearUserPreferences()
//                    }
//                }
//            }
//
//            install(DefaultRequest) {
//                url("https://lifetrack-1071288890438.us-central1.run.app")
//                header(HttpHeaders.ContentType, ContentType.Application.Json)
//            }
//
//            install(Auth) {
//                bearer {
//                    loadTokens {
//                        val currentTokens = prefs.tokenPreferences.first()
//                        if (!currentTokens.accessToken.isNullOrBlank()) {
//                            BearerTokens(
//                                accessToken = currentTokens.accessToken,
//                                refreshToken = currentTokens.refreshToken ?: ""
//                            )
//                        } else null
//                    }
//
//                    refreshTokens {
//                        val currentPrefs = prefs.tokenPreferences.first()
//                        val oldRefreshToken = currentPrefs.refreshToken ?: return@refreshTokens null
//
//                        try {
//                            val response = client.post("auth/refresh") {
//                                markAsRefreshTokenRequest()
//                                setBody(mapOf("token" to oldRefreshToken))
//                            }
//                            if (response.status == HttpStatusCode.OK) {
//                                val newTokens = response.body<TokenPreferences>()
//                                prefs.updateTokens(
//                                    accessToken = newTokens.accessToken,
//                                    refreshToken = newTokens.refreshToken
//                                )
//                                BearerTokens(
//                                    accessToken = newTokens.accessToken ?: "",
//                                    refreshToken = newTokens.refreshToken ?: ""
//                                )
//                            } else {
//                                null
//                            }
//                        } catch (e: Exception) {
//                            Log.e("KtorAuth", "Refresh failed", e)
//                            null
//                        }
//                    }
//
//                    sendWithoutRequest { request ->
//                        val path = request.url.encodedPath
//                        path.contains("/auth/login") ||
//                                path.contains("/auth/register") ||
//                                path.contains("/auth/refresh")
//                    }
//                }
//            }
//
//            install(Logging) {
//                level = LogLevel.NONE
//                sanitizeHeader { it == HttpHeaders.Authorization }
//            }
//        }
//    }
//}

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
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.lifetrack.ltapp.core.exception.NoInternetException
import org.lifetrack.ltapp.model.data.dclass.TokenPreferences
import org.lifetrack.ltapp.model.repository.PreferenceRepository

object KtorHttpClient {
    fun create(prefs: PreferenceRepository, networkObserver: NetworkObserver, koinScope: CoroutineScope): HttpClient {
        return HttpClient(Android) {
            expectSuccess = true
//            val authScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

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

            install(Auth) {
                bearer {
                    loadTokens {
                        val currentTokens = kotlin.runCatching {
                            prefs.tokenPreferences.value
                        }.getOrNull()

                        if (!currentTokens?.accessToken.isNullOrBlank()) {
                            BearerTokens(
                                accessToken = currentTokens.accessToken,
                                refreshToken = currentTokens.refreshToken ?: ""
                            )
                        } else {
                            null
                        }
                    }

                    refreshTokens {
                        val currentPrefs = kotlin.runCatching {
                            prefs.tokenPreferences.value
                        }.getOrNull()
                        val oldRefreshToken = currentPrefs?.refreshToken ?: return@refreshTokens null

                        try {
                            val response = client.post("auth/refresh") {
                                markAsRefreshTokenRequest()
                                setBody(mapOf("token" to oldRefreshToken))
                            }
                            if (response.status == HttpStatusCode.OK) {
                                val newTokens = response.body<TokenPreferences>()

                                koinScope.launch {
                                    prefs.updateTokens(
                                        accessToken = newTokens.accessToken,
                                        refreshToken = newTokens.refreshToken
                                    )
                                }

                                BearerTokens(
                                    accessToken = newTokens.accessToken ?: "",
                                    refreshToken = newTokens.refreshToken ?: ""
                                )
                            } else {
                                null
                            }
                        } catch (e: Exception) {
                            Log.e("KtorAuth", "Refresh failed", e)
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
            install("ConnectivityCheck") {
                requestPipeline.intercept(HttpRequestPipeline.Before) {
                    if (!networkObserver.isConnected.value ) {
                        throw NoInternetException()
                    }
                    proceed()
                }
            }

            HttpResponseValidator {
                validateResponse { response ->
                    val path = response.call.request.url.encodedPath
                    if (response.status == HttpStatusCode.Unauthorized && path.contains("/auth/refresh")) {
                        Log.e("KtorValidator", "Refresh Token Expired. Hard Logout.")

                        koinScope.launch {
                            prefs.clearUserPreferences()
                        }
                    }
                }
            }

            install(Logging) {
                level = LogLevel.NONE
                sanitizeHeader { it == HttpHeaders.Authorization }
            }
        }
    }
}