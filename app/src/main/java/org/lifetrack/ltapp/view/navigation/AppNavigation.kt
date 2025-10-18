package org.lifetrack.ltapp.view.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope
import org.lifetrack.ltapp.view.ui.screens.patient.about_screen.AboutScreen
import org.lifetrack.ltapp.view.ui.screens.patient.other_screen.OtherScreen
import org.lifetrack.ltapp.view.ui.screens.patient.guide_screen.HelpSupportScreen
import org.lifetrack.ltapp.view.ui.screens.patient.home_screen.HomeScreen
import org.lifetrack.ltapp.view.ui.screens.patient.menu_screen.MenuScreen
import org.lifetrack.ltapp.view.ui.screens.patient.profile_screen.ProfileScreen
import org.lifetrack.ltapp.view.ui.screens.SplashScreen
import org.lifetrack.ltapp.view.ui.screens.patient.telemed_screen.TelemedicineScreen

@Composable
fun AppNavigation(scope: CoroutineScope) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
//    val userRepository = UserRepositoryImpl()
//    val authRepository = AuthRepositoryImpl()
//    val authPresenter = AuthPresenter(
//        view = object : AuthView {
//            override fun showLoading(isLoading: Boolean, message: String?) {
//                coroutineScope.launch {
//                    if (isLoading) {
//                        val defaultMessage = "Loading, please wait..."
//                        Toast.makeText(context, message ?: defaultMessage, Toast.LENGTH_SHORT).show()
//                    }
//                }
//            }
//            override fun showError(message: String) {
//                Toast.makeText(context, "Error: $message", Toast.LENGTH_LONG).show()
//            }
//
//            override fun onAuthSuccess() {
//                navController.navigate("home") {
//                    popUpTo("login") { inclusive = true }
//                }
//            }
//            override fun onAuthSuccessWithData(data: String) {
//                navController.navigate("home") {
//                    popUpTo("login") { inclusive = true }
//                }
//            }
//
//        },
//        repository = authRepository,
//        scope = scope
//    )
//    val client = KtorClientFactory().create()
//    val apiService = ApiService()
//    val chatPresenter = ChatPresenter(
//        apiService = apiService
//    )
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

//        composable("signup") {
//            RegistrationScreen(
//                navController = navController,
//                presenter = authPresenter
//            )
//        }

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

//        composable("reset") {
//            RestoreScreen(
//                navController = navController,
//                userRepository = userRepository
//            )
//        }
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
             HelpSupportScreen(navController)
        }
        composable("about"){
            AboutScreen(navController)
        }
    }
}

