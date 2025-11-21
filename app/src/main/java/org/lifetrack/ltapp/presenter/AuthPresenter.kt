package org.lifetrack.ltapp.presenter

import androidx.lifecycle.ViewModel
import org.lifetrack.ltapp.model.repository.AuthRepository
import org.lifetrack.ltapp.ui.view.AuthView

class AuthPresenter(
    var view: AuthView?,
    val authRepository: AuthRepository
): ViewModel() {

    suspend fun getTokenId(): String{
        return  authRepository.getTokenId()
    }

    fun logout() {
        authRepository.logout()
    }

    fun login(email: String, password: String){

    }

    fun signUp(email: String, password: String, telNumber: String, fullName: String){

    }

}