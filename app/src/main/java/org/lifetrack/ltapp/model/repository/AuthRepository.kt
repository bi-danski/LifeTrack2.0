package org.lifetrack.ltapp.model.repository

import org.lifetrack.ltapp.model.data.dclass.AuthResult
import org.lifetrack.ltapp.model.data.dclass.LoginInfo
import org.lifetrack.ltapp.model.data.dclass.SignUpInfo

interface AuthRepository {
    suspend fun login(loginInfo: LoginInfo): AuthResult
    suspend fun signUp(signupInfo: SignUpInfo): AuthResult
    suspend fun refreshSession(): AuthResult
    suspend fun logout(): AuthResult
}