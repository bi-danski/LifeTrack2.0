package org.lifetrack.ltapp.model.dto

sealed class AuthResult {
    data object Success : AuthResult()
    data object Loading : AuthResult()
    data class SuccessWithData<T>(val data: T) : AuthResult()
    data class Failure(val message: String) : AuthResult()
}