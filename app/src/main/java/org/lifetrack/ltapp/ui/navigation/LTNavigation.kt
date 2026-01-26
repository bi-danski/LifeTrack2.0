package org.lifetrack.ltapp.ui.navigation

import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import org.koin.androidx.compose.koinViewModel
import org.lifetrack.ltapp.presenter.*
import org.lifetrack.ltapp.ui.screens.*

@Composable
fun LTNavigation(
    navController: NavHostController,
    startDestination: String
) {
    val activity = LocalActivity.current as? ComponentActivity
        ?: throw IllegalStateException("LTNavigation must be hosted in a ComponentActivity")

    val authPresenter = koinViewModel<AuthPresenter>(viewModelStoreOwner = activity)
    val userPresenter = koinViewModel<UserPresenter>(viewModelStoreOwner = activity)
    val sharedPresenter = koinViewModel<SharedPresenter>(viewModelStoreOwner = activity)
    val chatPresenter = koinViewModel<ChatPresenter>(viewModelStoreOwner = activity)
    val homePresenter = koinViewModel<HomePresenter>(viewModelStoreOwner = activity)
    val prescPresenter = koinViewModel<PrescPresenter>(viewModelStoreOwner = activity)
    val fuvPresenter = koinViewModel<FUVPresenter>(viewModelStoreOwner = activity)

    NavHost(
        navController = navController,
        startDestination = startDestination,
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Start,
                animationSpec = tween(100)
            ) + fadeOut(animationSpec = tween(100))
        },
        popEnterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = tween(100)
            ) + fadeIn(animationSpec = tween(100))
        },
        popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = tween(100)
            ) + fadeOut(animationSpec = tween(100))
        }
    ) {
        navigation(startDestination = "login", route = "auth_graph") {
            composable("login") {
                LoginScreen(navController, authPresenter, sharedPresenter)
            }
            composable("signup") {
                SignupScreen(navController, authPresenter)
            }
            composable("restore") {
                RestoreScreen(navController)
            }
        }

        navigation(startDestination = "home", route = "home_graph") {
            composable("home") {
                HomeScreen(navController, homePresenter, userPresenter, authPresenter, sharedPresenter)
            }
            composable("profile") {
                ProfileScreen(navController, authPresenter, userPresenter)
            }
            composable("menu") {
                MenuScreen(navController, authPresenter, sharedPresenter)
            }
            composable("alma") {
                AlmaScreen(navController, chatPresenter)
            }
            composable("ltChats") {
                ChatScreen(navController, chatPresenter)
            }

            addHealthFeatures(navController, userPresenter, authPresenter, prescPresenter)

            addSupportFeatures(navController, sharedPresenter)

            addUtilityFeatures(navController, fuvPresenter)
        }
    }
}

fun NavGraphBuilder.addHealthFeatures(
    navController: NavHostController,
    userPresenter: UserPresenter,
    authPresenter: AuthPresenter,
    prescPresenter: PrescPresenter
) {
    composable("analytics") {
        AnalyticScreen(navController, userPresenter)
    }
    composable("prescriptions") {
        PrescriptScreen(navController, userPresenter, prescPresenter)
    }
    composable("appointments") {
        AppointScreen(navController, userPresenter)
    }
    composable(
        route = "prescription_detail/{medId}",
        arguments = listOf(navArgument("medId") { type = NavType.StringType })
    ) { backStackEntry ->
        val medId = backStackEntry.arguments?.getString("medId")
        val prescription = userPresenter.dummyPrescriptions.find { it.id == medId }
        prescription?.let {
            PDetailScreen(navController, authPresenter = authPresenter, prescription = it)
        }
    }
    composable("telemedicine") { TelemedicineScreen(navController) }
}

fun NavGraphBuilder.addSupportFeatures(
    navController: NavHostController,
    sharedPresenter: SharedPresenter
) {
    composable("about") { AboutScreen(navController, sharedPresenter) }
    composable("support") { SupportScreen(navController) }
    composable("alerts") { AlertScreen(navController) }
}

fun NavGraphBuilder.addUtilityFeatures(
    navController: NavHostController,
    fuvPresenter: FUVPresenter
) {
    composable("FUV") {
        FollowUpScreen(navController, fuvPresenter)
    }
    composable("timeline") { TimeLineScreen(navController) }
    composable("other") { OtherScreen(navController) }
}