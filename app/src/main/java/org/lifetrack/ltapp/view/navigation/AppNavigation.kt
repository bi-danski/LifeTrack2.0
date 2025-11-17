package org.lifetrack.ltapp.view.navigation

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.lifetrack.ltapp.model.repository.AuthRepositoryImpl
import org.lifetrack.ltapp.presenter.AuthPresenter
import org.lifetrack.ltapp.view.AuthView
import org.lifetrack.ltapp.view.ui.screens.*

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

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("splash") {
            SplashScreen(navController)
        }

//        composable("login") {
//            LoginScreen(navController, authPresenter)
//        }

        composable("signup") {
            RegistrationScreen(
                navController = navController,
                presenter = authPresenter
            )
        }

        composable("home") {
            HomeScreen(
                navController = navController
//                onEmergency = { navController.navigate("emergency") },
//                onSearch = { navController.navigate("search")},
//                onAlma = { navController.navigate("alma") }
            )
        }

//        composable("chat"){
//            ChatScreen(
//                navController = navController,
//                presenter = chatPresenter)
//        }
        composable("profile"){
            ProfileScreen(
                navController = navController,
//                userRepository = userRepository,
//                onLogout = {
//                    scope.launch {
//                        authRepository.logout()
//                        navController.navigate("login") {
//                            popUpTo("home") { inclusive = true }
//                        }
//                    }
//                }
            )
        }

        composable("menu"){
            MenuScreen(
                navController = navController
            )
        }

        composable("reset") {
            RestoreScreen(
                navController = navController
            )
        }
//        composable ("kiongozi"){
//            AdminScreen(navController)
//        }
//        composable("expert") { ExpertScreen(navController) }
//        composable("med_timeline") { MedicalTimelineScreen(navController) }
        composable("telemedicine") { TelemedicineScreen(navController) }
//        composable("epidemic_alert") { EpidemicAlertScreen(navController) }
//        composable("info_hub") { InfoHubScreen(navController) }
        composable("other") { OtherScreen(navController) }
        composable("help_support") {
             SupportScreen(navController)
        }
        composable("about"){
            AboutScreen(navController)
        }
    }
}

