package org.lifetrack.ltapp.model.repository

import org.lifetrack.ltapp.model.dto.AuthResult

class AuthRepositoryImpl: AuthRepository {
    override suspend fun login(
        email: String,
        password: String
    ): AuthResult {
        TODO("Not yet implemented")
    }

    override suspend fun signUp(
        email: String,
        password: String,
        phoneNumber: String,
        displayName: String
    ): AuthResult {
        TODO("Not yet implemented")
    }

    override suspend fun refreshSession(): AuthResult {
        TODO("Not yet implemented")
    }

    override suspend fun getTokenId(): String {
        TODO("Not yet implemented")
    }

    override fun logout(): AuthResult {
        TODO("Not yet implemented")
    }
}