package org.lifetrack.ltapp.presenter

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import org.lifetrack.ltapp.model.repository.AuthRepository


class AuthPresenter(
    val authRepository: AuthRepository,
): ViewModel() {

    suspend fun getTokenId(): String{
        return  authRepository.getTokenId()
    }

    fun logout(navController: NavController) {
        authRepository.logout()
        navController.navigate("login"){
            popUpTo(0) {
                inclusive = true
            }
        }
    }

    fun login(email: String, password: String, navController: NavController){
        //
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
