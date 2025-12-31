package org.lifetrack.ltapp.model.repository

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.expectSuccess
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.lifetrack.ltapp.model.data.dclass.AuthResult
import org.lifetrack.ltapp.model.data.dclass.User
import org.lifetrack.ltapp.model.data.dto.PwdRestoreRequest
import org.lifetrack.ltapp.model.data.dto.UserDataResponse


class UserRepositoryImpl(
    private val client: HttpClient,
    private val prefRepository: PreferenceRepository
) : UserRepository {

    override suspend fun getCurrentUserInfo(): AuthResult {
        val currentTokens = prefRepository.tokenPreferences.value
        if (currentTokens.accessToken.isNullOrBlank()) {
            return AuthResult.Error("No active session found")
        }
        return try {
            val response = client.get("user/info") {
                expectSuccess = true
            }
            AuthResult.SuccessWithData(response.body<UserDataResponse>())
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Network request failed")
        }
    }

    override suspend fun updateCurrentUserInfo(user: User): AuthResult {
        return try {
            val response = client.patch("user/updateAccount") {
                contentType(ContentType.Application.Json)
                setBody(user)
            }
            if (response.status.value in 200..299) {
                AuthResult.Success
            } else {
                AuthResult.Error("Update failed")
            }
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Network Error")
        }
    }

    override suspend fun changePassword(passwordRequest: PwdRestoreRequest): AuthResult {
        return try {
            val response = client.post("user/changePassword") {
                contentType(ContentType.Application.Json)
                setBody(passwordRequest)
            }
            if (response.status.value in 200..299) {
                AuthResult.Success
            } else {
                AuthResult.Error("Failed to change password. Please check your current password.")
            }
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Connection error")
        }
    }

    override suspend fun deleteAccount(): AuthResult {
        return try {
            val response = client.delete("user/deleteAccount")
            if (response.status.value in 200..299) {
                prefRepository.clearSession()
                AuthResult.Success
            } else {
                AuthResult.Error("Could not delete account. Please try again.")
            }
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Network error")
        }
    }

}