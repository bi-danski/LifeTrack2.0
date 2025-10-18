package org.lifetrack.ltapp.model.repository

import org.lifetrack.ltapp.model.data.AuthResult

interface AuthRepository {
    suspend fun login(email: String, password: String): AuthResult
    suspend fun signUp(email: String, password: String, phoneNumber: String, displayName: String): AuthResult
    suspend fun refreshSession(): AuthResult
    suspend fun getTokenId(): String
    fun logout(): AuthResult

}