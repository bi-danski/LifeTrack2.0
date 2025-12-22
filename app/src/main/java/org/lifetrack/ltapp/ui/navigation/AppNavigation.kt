package org.lifetrack.ltapp.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import org.koin.androidx.compose.koinViewModel
import org.lifetrack.ltapp.presenter.AnalyticPresenter
import org.lifetrack.ltapp.presenter.AuthPresenter
import org.lifetrack.ltapp.presenter.ChatPresenter
import org.lifetrack.ltapp.presenter.EPrescriptPresenter
import org.lifetrack.ltapp.presenter.FUVPresenter
import org.lifetrack.ltapp.presenter.HomePresenter
import org.lifetrack.ltapp.presenter.SettingsPresenter
import org.lifetrack.ltapp.presenter.SharedPresenter
import org.lifetrack.ltapp.presenter.UserPresenter
import org.lifetrack.ltapp.ui.screens.*


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun AppNavigation(navController: NavHostController) {
    val authPresenter = koinViewModel<AuthPresenter>()
    val analyticPresenter = koinViewModel<AnalyticPresenter>()
    val chatPresenter = koinViewModel<ChatPresenter>()
    val userPresenter = koinViewModel<UserPresenter>()
    val sharedPresenter = koinViewModel<SharedPresenter>()
    val settingsPresenter = koinViewModel<SettingsPresenter>()


    NavHost(
        navController = navController,
        startDestination = "home"
    ) {

        composable("splash") {
            SplashScreen(navController)
        }

        composable("login") {
            LoginScreen(
                navController = navController,
                presenter = authPresenter,
                sharedPresenter = sharedPresenter
            )
        }

        composable("signup") {
            RegistrationScreen(
                navController = navController,
                presenter = authPresenter,
            )
        }

        composable("home") {
            HomeScreen(
                navController = navController,
                presenter = koinViewModel<HomePresenter>(),
                userPresenter = userPresenter
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
                PDetailScreen(navController = navController, prescription = prescription)
            }
        }
    }

}
