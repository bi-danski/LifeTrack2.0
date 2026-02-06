package org.lifetrack.ltapp.model.repository

import org.lifetrack.ltapp.model.data.dclass.AuthResult
import org.lifetrack.ltapp.model.data.dto.PwdRestoreRequest
import org.lifetrack.ltapp.model.data.dto.UserDataUpdate

interface UserRepository {
    suspend fun getCurrentUserInfo(): AuthResult

    suspend fun getUserAppointments(): AuthResult
    suspend fun updateCurrentUserInfo(user: UserDataUpdate): AuthResult
    suspend fun changePassword(passwordRequest: PwdRestoreRequest): AuthResult
    suspend fun deleteAccount(): AuthResult
}