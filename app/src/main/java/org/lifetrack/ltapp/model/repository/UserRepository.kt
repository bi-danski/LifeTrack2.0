package org.lifetrack.ltapp.model.repository

import org.lifetrack.ltapp.model.data.dclass.AuthResult
import org.lifetrack.ltapp.model.data.dclass.User
import org.lifetrack.ltapp.model.data.dto.PwdRestoreRequest

interface UserRepository {
    suspend fun getCurrentUserInfo(): AuthResult
    suspend fun updateCurrentUserInfo(user: User): AuthResult
    suspend fun changePassword(passwordRequest: PwdRestoreRequest): AuthResult
    suspend fun deleteAccount(): AuthResult
}