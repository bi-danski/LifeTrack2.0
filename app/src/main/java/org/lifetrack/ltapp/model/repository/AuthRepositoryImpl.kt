package org.lifetrack.ltapp.model.repository

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import org.lifetrack.ltapp.core.exception.NoInternetException
import org.lifetrack.ltapp.core.utility.ZetuZetuUtil.sanitizeErrorMessage
import org.lifetrack.ltapp.core.utility.toLoginRequest
import org.lifetrack.ltapp.core.utility.toSignUpRequest
import org.lifetrack.ltapp.model.data.dclass.AuthResult
import org.lifetrack.ltapp.model.data.dclass.LoginInfo
import org.lifetrack.ltapp.model.data.dclass.SignUpInfo
import org.lifetrack.ltapp.model.data.dclass.TokenPreferences


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
                val tokens = response.body<TokenPreferences>()
                prefs.updateTokens(tokens.accessToken, tokens.refreshToken)
                AuthResult.SuccessWithData(tokens)
            }else {
                AuthResult.Error("Invalid credentials. Please check your email and password.")
            }
        }catch(_: NoInternetException){
            AuthResult.Error(isNetworkError = true, message = "No Internet Connection")
        }catch (ex: Exception) {
            AuthResult.Error(sanitizeErrorMessage(ex))
        }
    }

    override suspend fun signUp( signupInfo: SignUpInfo ): AuthResult {
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
        }catch(_: NoInternetException){
            AuthResult.Error(isNetworkError = true, message = "No Internet Connection")
        }catch (ex: Exception) {
            AuthResult.Error(sanitizeErrorMessage(ex))
        }
    }

    override suspend fun refreshSession(): AuthResult {
        val currentRefreshToken = prefs.tokenPreferences.value.refreshToken

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

                prefs.updateTokens(
                    accessToken = newTokens.accessToken,
                    refreshToken = newTokens.refreshToken
                )
                AuthResult.Success
            } else {
                prefs.updateTokens(null, null)
                AuthResult.Error("Session expired. Please login again.")
            }
        }catch(_: NoInternetException){
            AuthResult.Error(isNetworkError = true, message = "No Internet Connection")
        }catch (e: Exception) {
            AuthResult.Error("Network error: ${e.message}")
        }
    }

    override suspend fun logout(): AuthResult {
        return try {
            val response = client.post("/user/logout") {
                contentType(ContentType.Application.Json)
            }
            if (response.status == HttpStatusCode.OK) {
                AuthResult.Success
            }else{
                AuthResult.Error(response.bodyAsText())
            }
        }catch(_: NoInternetException){
            AuthResult.Error(isNetworkError = true, message = "No Internet Connection")
        }catch (ex: Exception) {
            AuthResult.Error(sanitizeErrorMessage(ex))
        }
    }
}