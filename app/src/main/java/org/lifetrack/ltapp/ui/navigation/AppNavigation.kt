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
import org.lifetrack.ltapp.presenter.*
import org.lifetrack.ltapp.ui.screens.*

@Composable
fun AppNavigation(
    navController: NavHostController,
) {
    val activity = LocalActivity.current as? ComponentActivity
        ?: throw IllegalStateException("AppNavigation must be hosted in a ComponentActivity")

    val authPresenter = koinViewModel<AuthPresenter>(viewModelStoreOwner = activity)
    val userPresenter = koinViewModel<UserPresenter>(viewModelStoreOwner = activity)
    val sharedPresenter = koinViewModel<SharedPresenter>(viewModelStoreOwner = activity)
    val chatPresenter = koinViewModel<ChatPresenter>(viewModelStoreOwner = activity)

    val isLoggedIn by authPresenter.isLoggedIn.collectAsState()

    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn == true) "home" else "login",
//        enterTransition = {
//            slideIntoContainer(
//                towards = AnimatedContentTransitionScope.SlideDirection.Start,
//                animationSpec = tween(400)
//            ) + fadeIn(animationSpec = tween(400))
//        },
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
            LoginScreen(navController, authPresenter = authPresenter, sharedPresenter = sharedPresenter)
        }
        composable("signup") {
            SignupScreen(navController, authPresenter = authPresenter)
        }

        composable("home") {
            HomeScreen(
                navController = navController,
                homePresenter = koinViewModel<HomePresenter>(),
                userPresenter = userPresenter,
                authPresenter = authPresenter,
                sharedPresenter = sharedPresenter
            )
        }
        composable("profile") {
            ProfileScreen(navController, authPresenter = authPresenter, userPresenter = userPresenter)
        }
        composable("menu") {
            MenuScreen(
                navController = navController,
                authPresenter = authPresenter,
                sharedPresenter = sharedPresenter,
            )
        }

        composable("alma") {
            AlmaScreen(navController, presenter = chatPresenter)
        }
        composable("ltChats") {
            ChatScreen(navController, presenter = chatPresenter)
        }

        composable("analytics") {
            AnalyticScreen(navController, presenter = userPresenter)
        }
        composable("prescriptions") {
            PrescriptScreen(
                navController = navController,
                userPresenter = userPresenter,
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
            val prescription = userPresenter.dummyPrescriptions.find { it.id == medId }
            if (prescription != null) {
                PDetailScreen(navController, authPresenter = authPresenter, prescription = prescription)
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