package org.lifetrack.ltapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import kotlinx.coroutines.CoroutineScope
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import org.lifetrack.ltapp.presenter.AnalyticPresenter
import org.lifetrack.ltapp.presenter.AuthPresenter
import org.lifetrack.ltapp.presenter.ChatPresenter
import org.lifetrack.ltapp.presenter.EPrescriptPresenter
import org.lifetrack.ltapp.presenter.FUVPresenter
import org.lifetrack.ltapp.presenter.HomePresenter
import org.lifetrack.ltapp.presenter.SettingsPresenter
import org.lifetrack.ltapp.presenter.SharedPresenter
import org.lifetrack.ltapp.presenter.UserPresenter
import org.lifetrack.ltapp.ui.screens.AboutScreen
import org.lifetrack.ltapp.ui.screens.AlertScreen
import org.lifetrack.ltapp.ui.screens.AlmaScreen
import org.lifetrack.ltapp.ui.screens.AnalyticScreen
import org.lifetrack.ltapp.ui.screens.AppointScreen
import org.lifetrack.ltapp.ui.screens.ChatScreen
import org.lifetrack.ltapp.ui.screens.FollowUpScreen
import org.lifetrack.ltapp.ui.screens.HomeScreen
import org.lifetrack.ltapp.ui.screens.LoginScreen
import org.lifetrack.ltapp.ui.screens.MenuScreen
import org.lifetrack.ltapp.ui.screens.OtherScreen
import org.lifetrack.ltapp.ui.screens.PDetailScreen
import org.lifetrack.ltapp.ui.screens.PrescriptScreen
import org.lifetrack.ltapp.ui.screens.ProfileScreen
import org.lifetrack.ltapp.ui.screens.SignupScreen
import org.lifetrack.ltapp.ui.screens.RestoreScreen
import org.lifetrack.ltapp.ui.screens.SplashScreen
import org.lifetrack.ltapp.ui.screens.SupportScreen
import org.lifetrack.ltapp.ui.screens.TelemedicineScreen
import org.lifetrack.ltapp.ui.screens.TimeLineScreen


@Composable
fun AppNavigation(
    navController: NavHostController,
    scope: CoroutineScope = koinInject()
) {
    val authPresenter = koinViewModel<AuthPresenter>()
    val analyticPresenter = koinViewModel<AnalyticPresenter>()
    val chatPresenter = koinViewModel<ChatPresenter>()
    val userPresenter = koinViewModel<UserPresenter>()
    val sharedPresenter = koinViewModel<SharedPresenter>()
    val settingsPresenter = koinViewModel<SettingsPresenter>()

    NavHost(
        navController = navController,
        startDestination = if (authPresenter.isAuthenticated()) "home" else "login"
    ) {

        composable("splash") {
            SplashScreen(navController)
        }

        composable("login") {
            LoginScreen(
                navController = navController,
                presenter = authPresenter,
                sharedPresenter = sharedPresenter,
//                ltScope = scope
            )
        }

        composable("signup") {
            SignupScreen(
                navController = navController,
                presenter = authPresenter,
            )
        }

        composable("home") {
            HomeScreen(
                navController = navController,
                presenter = koinViewModel<HomePresenter>(),
                userPresenter = userPresenter,
                sharedPresenter = sharedPresenter
            )
        }

        composable("alma") {
            AlmaScreen(
                navController = navController,
                presenter = chatPresenter
            )
        }

        composable("profile") {
            ProfileScreen(
                navController = navController,
                authPresenter = authPresenter,
                userPresenter = userPresenter
            )
        }

        composable("menu") {
            MenuScreen(
                navController = navController,
                userPresenter = userPresenter,
                sharedPresenter = sharedPresenter,
                settingsPresenter = settingsPresenter
            )
        }

        composable("ltChats"){
            ChatScreen(
                navController = navController,
                presenter = chatPresenter
            )
        }

        composable("restore") {
            RestoreScreen(navController = navController)
        }

        composable("analytics") {
            AnalyticScreen(
                navController = navController,
                presenter = analyticPresenter
            )
        }

        composable("timeline") {
            TimeLineScreen(navController = navController)
        }

        composable("telemedicine") {
            TelemedicineScreen(navController = navController)
        }

        composable("prescriptions") {
            PrescriptScreen(
                navController = navController,
                analyticPresenter = analyticPresenter,
                presenter = koinViewModel<EPrescriptPresenter>()
                )
        }

        composable("alerts") {
            AlertScreen(navController)
        }

        composable("other") {
            OtherScreen(navController)
        }

        composable("support") {
            SupportScreen(
                navController = navController,
            )
        }

        composable("about") {
            AboutScreen(
                navController = navController,
                sharedPresenter = sharedPresenter
            )
        }

        composable("appointments"){
            AppointScreen(
                navController = navController,
                userPresenter = userPresenter
            )
        }

        composable("FUV"){
            FollowUpScreen(
                navController = navController,
                fuvPresenter = koinViewModel<FUVPresenter>()
            )
        }

        composable(
            route = "prescription_detail/{medId}",
            arguments = listOf(navArgument("medId") { type = NavType.StringType })
        ) { backStackEntry ->
            val medId = backStackEntry.arguments?.getString("medId")
            val prescription = analyticPresenter.dummyPrescriptions.find { it.id == medId }
            if (prescription != null) {
                PDetailScreen(
                    navController = navController,
                    userPresenter = userPresenter,
                    prescription = prescription
                )
            }
        }
    }

}
