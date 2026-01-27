package org.lifetrack.ltapp.model.data.dclass

sealed class AuthResult {
    data object Success : AuthResult()
    data object Loading : AuthResult()
    data class SuccessWithData<T>(val data: T) : AuthResult()
    data class Error(val message: String, val isNetworkError: Boolean = false) : AuthResult()
}