package org.lifetrack.ltapp.model.repository

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import org.lifetrack.ltapp.core.utils.toLoginRequest
import org.lifetrack.ltapp.core.utils.toSignUpRequest
import org.lifetrack.ltapp.model.data.dclass.AuthResult
import org.lifetrack.ltapp.model.data.dclass.LoginInfo
import org.lifetrack.ltapp.model.data.dclass.SignUpInfo
import org.lifetrack.ltapp.model.data.dclass.TokenPreferences


class AuthRepositoryImpl(
    private val client: HttpClient,
    private val prefs: PreferenceRepository
) : AuthRepository {

    override suspend fun login(loginInfo: LoginInfo): AuthResult {
        return try {
            val response = client.post("auth/login") {
                contentType(ContentType.Application.Json)
                setBody(loginInfo.toLoginRequest())
            }
            if (response.status == HttpStatusCode.OK) {
                val tokens = response.body<TokenPreferences>()
                prefs.updateTokens(tokens.accessToken, tokens.refreshToken)
                println("**********************************$tokens**********************************")
                AuthResult.SuccessWithData(tokens)

            } else {
                AuthResult.Error("Invalid credentials: ${response.status}")
            }
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Unknown login error")
        }
    }

    override suspend fun signUp( signupInfo: SignUpInfo ): AuthResult {
        return try {
            client.post("auth/register") {
                contentType(ContentType.Application.Json)
                setBody(signupInfo.toSignUpRequest())
            }
            AuthResult.Success
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "An Error Occurred while creating your account")
        }
    }


    override suspend fun refreshSession(): AuthResult {
        val currentRefreshToken = prefs.tokenPreferences.value.refreshToken

        if (currentRefreshToken.isNullOrBlank()) {
            return AuthResult.Error("No refresh token found")
        }
        return try {
            val response = client.post("auth/refresh") {
                contentType(ContentType.Application.Json)
                setBody(mapOf("refreshToken" to currentRefreshToken))
            }
            if (response.status == HttpStatusCode.OK) {
                val newTokens = response.body<TokenPreferences>() // Your TokenPair

                prefs.updateTokens(
                    accessToken = newTokens.accessToken,
                    refreshToken = newTokens.refreshToken
                )
                AuthResult.Success
            } else {
                prefs.updateTokens(null, null)
                AuthResult.Error("Session expired. Please login again.")
            }
        } catch (e: Exception) {
            AuthResult.Error("Network error: ${e.message}")
        }
    }

    override suspend fun logout(): AuthResult {
        return try {
            prefs.clearSession()
            AuthResult.Success
        } catch (e: Exception) {
            AuthResult.Error("Logout failed: ${e.message}")
        }
    }
}