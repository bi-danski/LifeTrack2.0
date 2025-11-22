package org.lifetrack.ltapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.koin.androidx.compose.koinViewModel
import org.lifetrack.ltapp.presenter.AlmaPresenter
import org.lifetrack.ltapp.presenter.AuthPresenter
import org.lifetrack.ltapp.presenter.SupportPresenter
import org.lifetrack.ltapp.ui.screens.*

@Composable
fun AppNavigation(
//    scope: CoroutineScope,
) {
    val navController = rememberNavController()
//    val context = LocalContext.current
//    val authRepository = koinInject<AuthRepository>()
    val authPresenter = koinViewModel<AuthPresenter>()

    NavHost(
        navController = navController,
        startDestination = "home" // splash
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
                presenter = koinViewModel<AlmaPresenter>())
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
             SupportScreen(
                 navController = navController,
                 presenter = koinViewModel<SupportPresenter>()
             )
        }
        composable("about"){
            AboutScreen(navController)
        }
    }
}

