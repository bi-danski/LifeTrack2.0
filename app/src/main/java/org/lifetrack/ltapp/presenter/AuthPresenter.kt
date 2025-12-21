package org.lifetrack.ltapp.presenter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.lifetrack.ltapp.model.data.dclass.LoginInfo
import org.lifetrack.ltapp.model.repository.AuthRepository


class AuthPresenter(
    val authRepository: AuthRepository,
): ViewModel() {

    private val _loginInfo = MutableStateFlow(LoginInfo())
    val loginInfo = _loginInfo.asStateFlow()

    suspend fun getTokenId(): String{
        return  authRepository.getTokenId()
    }

    fun onLoginInfoUpdate(value: LoginInfo){
        viewModelScope.launch {
            _loginInfo.value = LoginInfo(
                emailAddress = value.emailAddress,
                password = value.password
            )
        }
    }

    fun logout(navController: NavController) {
//        authRepository.logout()
        navController.navigate("login"){
            popUpTo(0) {
                inclusive = true
            }
        }
    }

    fun login(email: String, password: String, navController: NavController){
        // zetu zetu apo ku verify
        navController.navigate("home")
    }

    fun signUp(email: String, password: String, telNumber: String, fullName: String){

    }

}

















//        view = object : AuthView {
//            override fun showLoading(isLoading: Boolean, msg: String?) {
//                scope.launch {
//                    if (isLoading) {
//                        val defaultMessage = "Loading, please wait..."
//                        Toast.makeText(context, msg ?: defaultMessage, Toast.LENGTH_SHORT).show()
//                    }
//                }
//            }
//
//            override fun showError(msg: String) {
//                scope.launch {
//                    Toast.makeText(context, "Error: $msg", Toast.LENGTH_LONG).show()
//                }
//            }
//
//            override fun onAuthSuccess() {
//                navController.navigate("home") {
//                    popUpTo("login") { inclusive = true }
//                }
//            }
//
//            override fun onAuthSuccessWithData(data: String) {
//                navController.navigate("home") {
//                    popUpTo("login") { inclusive = true }
//                }
//            }
//
//        },
