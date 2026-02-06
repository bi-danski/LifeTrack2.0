package org.lifetrack.ltapp.model.repository

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.auth.clearAuthTokens
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.coroutines.flow.first
import org.lifetrack.ltapp.core.exception.NoInternetException
import org.lifetrack.ltapp.utility.ZetuZetuUtil.sanitizeErrorMessage
import org.lifetrack.ltapp.utility.toLoginRequest
import org.lifetrack.ltapp.utility.toSignUpRequest
import org.lifetrack.ltapp.model.data.dclass.AuthResult
import org.lifetrack.ltapp.model.data.dclass.LoginInfo
import org.lifetrack.ltapp.model.data.dclass.SignUpInfo
import org.lifetrack.ltapp.model.data.dclass.TokenPreferences
import org.lifetrack.ltapp.model.data.dto.SessionInfo

class AuthRepositoryImpl(
    private val client: HttpClient,
    private val prefs: PreferenceRepository,
) : AuthRepository {

    override suspend fun login(loginInfo: LoginInfo): AuthResult {
        return try {
            val response = client.post("/auth/login") {
                contentType(ContentType.Application.Json)
                setBody(loginInfo.toLoginRequest())
            }
            if (response.status == HttpStatusCode.OK) {
                client.clearAuthTokens()
                AuthResult.SuccessWithData(response.body<SessionInfo>())
            } else {
                AuthResult.Error("Invalid credentials. Please check your email and password.")
            }
        } catch (_: NoInternetException) {
            AuthResult.Error(isNetworkError = true, message = "No Internet Connection")
        } catch (ex: Exception) {
            AuthResult.Error(sanitizeErrorMessage(ex))
        }
    }

    override suspend fun signUp(signupInfo: SignUpInfo): AuthResult {
        return try {
            val response = client.post("/auth/signup") {
                contentType(ContentType.Application.Json)
                setBody(signupInfo.toSignUpRequest())
            }
            if (response.status == HttpStatusCode.Created) {
                AuthResult.Success
            } else {
                AuthResult.Error(response.bodyAsText())
            }
        } catch (_: NoInternetException) {
            AuthResult.Error(isNetworkError = true, message = "No Internet Connection")
        } catch (ex: Exception) {
            AuthResult.Error(sanitizeErrorMessage(ex))
        }
    }

    override suspend fun refreshSession(): AuthResult {
        val currentRefreshToken = try {
            prefs.tokenPreferences.first().refreshToken
        } catch (_: Exception) {
            null
        }

        if (currentRefreshToken.isNullOrBlank()) {
            return AuthResult.Error("No refresh token found")
        }

        return try {
            val response = client.post("/auth/refresh") {
                contentType(ContentType.Application.Json)
                setBody(mapOf("token" to currentRefreshToken))
            }
            if (response.status == HttpStatusCode.OK) {
                val newTokens = response.body<TokenPreferences>()

                prefs.updateTokenPreferences(
                    accessToken = newTokens.accessToken,
                    refreshToken = newTokens.refreshToken
                )
                AuthResult.Success
            } else {
                prefs.updateTokenPreferences(null, null)
                AuthResult.Error("Session expired. Please login again.")
            }
        } catch (_: NoInternetException) {
            AuthResult.Error(isNetworkError = true, message = "No Internet Connection")
        } catch (e: Exception) {
            AuthResult.Error("Network error: ${e.message}")
        }
    }

    override suspend fun logout(): AuthResult {
        return try {
            val response = client.post("/user/logout") {
                contentType(ContentType.Application.Json)
            }
            if (response.status == HttpStatusCode.OK) {
                client.clearAuthTokens()
                AuthResult.Success
            } else {
                AuthResult.Error(response.bodyAsText())
            }

        } catch (_: NoInternetException) {
            AuthResult.Error(isNetworkError = true, message = "No Internet Connection")
        } catch (ex: Exception) {
            AuthResult.Error(sanitizeErrorMessage(ex))
        }
    }
}