package org.lifetrack.ltapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.koin.androidx.compose.koinViewModel
import org.lifetrack.ltapp.presenter.AlmaPresenter
import org.lifetrack.ltapp.presenter.AuthPresenter
import org.lifetrack.ltapp.presenter.SupportPresenter
import org.lifetrack.ltapp.ui.screens.*

@Composable
fun AppNavigation(navController: NavHostController) {
    val authPresenter = koinViewModel<AuthPresenter>()

    NavHost(
        navController = navController,
        startDestination = "home" // or splash
    ) {

        composable("splash") {
            SplashScreen(navController)
        }

        composable("login") {
            LoginScreen(
                navController = navController,
                presenter = authPresenter
            )
        }

        composable("signup") {
            RegistrationScreen(
                navController = navController,
                presenter = authPresenter
            )
        }

        composable("home") {
            HomeScreen(navController = navController)
        }

        composable("alma") { backStackEntry ->
            val presenter: AlmaPresenter =
                koinViewModel<AlmaPresenter>(viewModelStoreOwner = backStackEntry)

            ChatScreen(
                navController = navController,
                presenter = presenter
            )
        }

        composable("profile") {
            ProfileScreen(
                navController = navController,
                presenter = koinViewModel<AuthPresenter>()
            )
        }

        composable("menu") {
            MenuScreen(navController = navController)
        }

        composable("restore") {
            RestoreScreen(navController = navController)
        }

        composable("analytics") {
            AnalyticScreen(navController = navController)
        }

        composable("timeline") {
            TimeLineScreen(navController = navController)
        }

        composable("telemedicine") {
            TelemedicineScreen(navController)
        }

        composable("alerts") {
            AlertScreen(navController)
        }

        composable("other") {
            OtherScreen(navController)
        }

        composable("support") { backStackEntry ->
            val presenter: SupportPresenter =
                koinViewModel(viewModelStoreOwner = backStackEntry)

            SupportScreen(
                navController = navController,
                presenter = presenter
            )
        }

        composable("about") {
            AboutScreen(navController)
        }
    }
}
