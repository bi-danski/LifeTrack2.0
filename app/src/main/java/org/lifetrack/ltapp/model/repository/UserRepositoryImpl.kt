package org.lifetrack.ltapp.model.repository

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.expectSuccess
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.first
import org.lifetrack.ltapp.core.exception.NoInternetException
import org.lifetrack.ltapp.model.data.dclass.AuthResult
import org.lifetrack.ltapp.model.data.dto.AppointmentUpdate
import org.lifetrack.ltapp.model.data.dto.PwdRestoreRequest
import org.lifetrack.ltapp.model.data.dto.UserDataResponse
import org.lifetrack.ltapp.model.data.dto.UserDataUpdate


class UserRepositoryImpl(
    private val client: HttpClient,
    private val prefRepository: PreferenceRepository
) : UserRepository {

    override suspend fun getCurrentUserInfo(): AuthResult {
        val currentTokens = prefRepository.tokenPreferences.first()
        if (currentTokens.accessToken.isNullOrBlank()) {
            return AuthResult.Error("No active session found")
        }
        return try {
            val response = client.get("/user/info") { expectSuccess = true }
            if (response.status.isSuccess()) {
                AuthResult.SuccessWithData(response.body<UserDataResponse>())
            }else{
                AuthResult.Error(response.bodyAsText())
            }
        }catch(_: NoInternetException){
            AuthResult.Error(isNetworkError = true, message = "No Internet Connection")
        }catch (e: Exception) {
            AuthResult.Error(e.message ?: "Network request failed")
        }
    }

    override suspend fun getUserAppointments(): AuthResult {
        return try {
            val httpResponse = client.get("/user/appointment") { expectSuccess = true }
            if (httpResponse.status.isSuccess()){
                AuthResult.SuccessWithData(httpResponse.body<List<AppointmentUpdate>>())
            }else {
                AuthResult.Error(httpResponse.bodyAsText())
            }
        }catch (ex: Exception){
            AuthResult.Error(ex.message ?: ex.localizedMessage)
        }catch(_: NoInternetException) {
            AuthResult.Error(isNetworkError = true, message = "No Internet Connection")
        }
    }

    override suspend fun updateCurrentUserInfo(user: UserDataUpdate): AuthResult {
        return try {
            val response = client.patch("/user/updateAccount") {
                contentType(ContentType.Application.Json)
                setBody(user)
            }
            if (response.status.value in 200..299) {
                AuthResult.Success
            } else {
                AuthResult.Error("Update failed")
            }
        }catch(_: NoInternetException){
            AuthResult.Error(isNetworkError = true, message = "No Internet Connection")
        }catch (e: Exception) {
            AuthResult.Error(e.message ?: "Network Error")
        }
    }

    override suspend fun changePassword(passwordRequest: PwdRestoreRequest): AuthResult {
        return try {
            val response = client.post("/user/changePassword") {
                contentType(ContentType.Application.Json)
                setBody(passwordRequest)
            }
            if (response.status.value in 200..299) {
                AuthResult.Success
            } else {
                AuthResult.Error("Failed to change password. Please check your current password.")
            }
        }catch(_: NoInternetException){
            AuthResult.Error(isNetworkError = true, message = "No Internet Connection")
        }catch (e: Exception) {
            AuthResult.Error(e.message ?: "Connection error")
        }
    }

    override suspend fun deleteAccount(): AuthResult {
        return try {
            val response = client.delete("/user/deleteAccount")
            if (response.status.value in 200..299) {
                prefRepository.clearAllSessions()
                AuthResult.Success
            } else {
                AuthResult.Error("Could not delete account. Please try again.")
            }
        }catch(_: NoInternetException){
            AuthResult.Error(isNetworkError = true, message = "No Internet Connection")
        }catch (e: Exception) {
            AuthResult.Error(e.message ?: "Network error")
        }
    }

}