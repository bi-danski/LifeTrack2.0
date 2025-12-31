package org.lifetrack.ltapp.ui.navigation

import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import org.koin.androidx.compose.koinViewModel
import org.lifetrack.ltapp.model.data.dclass.SessionStatus
import org.lifetrack.ltapp.presenter.AnalyticPresenter
import org.lifetrack.ltapp.presenter.AuthPresenter
import org.lifetrack.ltapp.presenter.ChatPresenter
import org.lifetrack.ltapp.presenter.FUVPresenter
import org.lifetrack.ltapp.presenter.HomePresenter
import org.lifetrack.ltapp.presenter.PrescPresenter
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
import org.lifetrack.ltapp.ui.screens.RestoreScreen
import org.lifetrack.ltapp.ui.screens.SignupScreen
import org.lifetrack.ltapp.ui.screens.SupportScreen
import org.lifetrack.ltapp.ui.screens.TelemedicineScreen
import org.lifetrack.ltapp.ui.screens.TimeLineScreen

@Composable
fun AppNavigation(
    navController: NavHostController,
    sessionStatus: SessionStatus
) {
    val activity = LocalActivity.current as? ComponentActivity
        ?: throw IllegalStateException("AppNavigation must be hosted in a ComponentActivity")

    val authPresenter = koinViewModel<AuthPresenter>(viewModelStoreOwner = activity)
    val userPresenter = koinViewModel<UserPresenter>(viewModelStoreOwner = activity)
    val sharedPresenter = koinViewModel<SharedPresenter>(viewModelStoreOwner = activity)
    val chatPresenter = koinViewModel<ChatPresenter>(viewModelStoreOwner = activity)
    val analyticPresenter = koinViewModel<AnalyticPresenter>(viewModelStoreOwner = activity)

    val isLoggedIn by authPresenter.isLoggedIn.collectAsState()

    if (sessionStatus == SessionStatus.INITIALIZING || isLoggedIn == null) {
        return
    }
    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn == true) "home" else "login",
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Start,
                animationSpec = tween(400)
            ) + fadeIn(animationSpec = tween(400))
        },
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Start,
                animationSpec = tween(400)
            ) + fadeOut(animationSpec = tween(400))
        },
        popEnterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = tween(400)
            ) + fadeIn(animationSpec = tween(400))
        },
        popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = tween(400)
            ) + fadeOut(animationSpec = tween(400))
        }
    ) {

        composable("login") {
            LoginScreen(navController, presenter = authPresenter, sharedPresenter = sharedPresenter)
        }
        composable("signup") {
            SignupScreen(navController, presenter = authPresenter)
        }

        composable("home") {
            HomeScreen(
                navController = navController,
                presenter = koinViewModel<HomePresenter>(),
                userPresenter = userPresenter,
                sharedPresenter = sharedPresenter
            )
        }
        composable("profile") {
            ProfileScreen(navController, authPresenter = authPresenter, userPresenter = userPresenter)
        }
        composable("menu") {
            MenuScreen(
                navController = navController,
                userPresenter = userPresenter,
                sharedPresenter = sharedPresenter,
                settingsPresenter = koinViewModel<SettingsPresenter>()
            )
        }

        composable("alma") {
            AlmaScreen(navController, presenter = chatPresenter)
        }
        composable("ltChats") {
            ChatScreen(navController, presenter = chatPresenter)
        }

        composable("analytics") {
            AnalyticScreen(navController, presenter = analyticPresenter)
        }
        composable("prescriptions") {
            PrescriptScreen(
                navController = navController,
                analyticPresenter = analyticPresenter,
                presenter = koinViewModel<PrescPresenter>()
            )
        }
        composable("appointments") {
            AppointScreen(navController, userPresenter = userPresenter)
        }

        composable(
            route = "prescription_detail/{medId}",
            arguments = listOf(navArgument("medId") { type = NavType.StringType })
        ) { backStackEntry ->
            val medId = backStackEntry.arguments?.getString("medId")
            val prescription = analyticPresenter.dummyPrescriptions.find { it.id == medId }
            if (prescription != null) {
                PDetailScreen(navController, userPresenter = userPresenter, prescription = prescription)
            }
        }

        composable("about") { AboutScreen(navController, sharedPresenter = sharedPresenter) }
        composable("FUV") { FollowUpScreen(navController, fuvPresenter = koinViewModel<FUVPresenter>()) }
        composable("restore") { RestoreScreen(navController) }
        composable("timeline") { TimeLineScreen(navController) }
        composable("telemedicine") { TelemedicineScreen(navController) }
        composable("alerts") { AlertScreen(navController) }
        composable("other") { OtherScreen(navController) }
        composable("support") { SupportScreen(navController) }
    }
}