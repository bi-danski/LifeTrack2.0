package org.lifetrack.ltapp.view.navigation

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.lifetrack.ltapp.model.repository.AuthRepositoryImpl
import org.lifetrack.ltapp.presenter.AlmaPresenter
import org.lifetrack.ltapp.presenter.AuthPresenter
import org.lifetrack.ltapp.view.AlmaView
import org.lifetrack.ltapp.view.AuthView
import org.lifetrack.ltapp.view.ui.screens.*

@SuppressLint("ViewModelConstructorInComposable")
@Composable
fun AppNavigation(scope: CoroutineScope) {
    val navController = rememberNavController()
    val context = LocalContext.current
//    val coroutineScope = rememberCoroutineScope()
//    val userRepository = UserRepositoryImpl()
    val authRepository = AuthRepositoryImpl()
    val authPresenter = AuthPresenter(
        view = object : AuthView {
            override fun showLoading(isLoading: Boolean, msg: String?) {
                scope.launch {
                    if (isLoading) {
                        val defaultMessage = "Loading, please wait..."
                        Toast.makeText(context, msg ?: defaultMessage, Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun showError(msg: String) {
                scope.launch {
                    Toast.makeText(context, "Error: $msg", Toast.LENGTH_LONG).show()
                }
            }

            override fun onAuthSuccess() {
                navController.navigate("home") {
                    popUpTo("login") { inclusive = true }
                }
            }

            override fun onAuthSuccessWithData(data: String) {
                navController.navigate("home") {
                    popUpTo("login") { inclusive = true }
                }
            }

        },
        authRepository = authRepository,
    )
    val almaPresenter = AlmaPresenter(
        view = object: AlmaView {
            override fun displayAlmaResponse() {

            }
            override fun showError() {

            }

            override fun showLoading() {

            }

            override fun hideLoading() {

            }
        }
    )

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("splash") {
            SplashScreen(navController)
        }
        composable("login") {
            LoginScreen(navController, authPresenter)
        }
        composable("signup") {
            RegistrationScreen(
                navController = navController,
                presenter = authPresenter
            )
        }
        composable("home") {
            HomeScreen(
                navController = navController
            )
        }
        composable("alma"){
            ChatScreen(
                navController = navController,
                presenter = almaPresenter)
        }
        composable("profile"){
            ProfileScreen(
                navController = navController,
            )
        }
        composable("menu"){
            MenuScreen(
                navController = navController
            )
        }
        composable("restore") {
            RestoreScreen(
                navController = navController
            )
        }
//        composable ("kiongozi"){
//            AdminScreen(navController)
//        }
//        composable("expert") { ExpertScreen(navController) }
        composable("timeline") { TimeLineScreen(navController) }
        composable("telemedicine") { TelemedicineScreen(navController) }
        composable("alerts") { AlertScreen(navController) }
//        composable("info_hub") { InfoHubScreen(navController) }
        composable("other") { OtherScreen(navController) }
        composable("support") {
             SupportScreen(navController)
        }
        composable("about"){
            AboutScreen(navController)
        }
    }
}

