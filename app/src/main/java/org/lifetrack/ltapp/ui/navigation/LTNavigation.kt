package org.lifetrack.ltapp.ui.navigation

import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import org.koin.androidx.compose.koinViewModel
import org.lifetrack.ltapp.presenter.AuthPresenter
import org.lifetrack.ltapp.presenter.ChatPresenter
import org.lifetrack.ltapp.presenter.FUVPresenter
import org.lifetrack.ltapp.presenter.HomePresenter
import org.lifetrack.ltapp.presenter.PrescPresenter
import org.lifetrack.ltapp.presenter.SharedPresenter
import org.lifetrack.ltapp.presenter.TLinePresenter
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
import org.lifetrack.ltapp.ui.screens.MainScreen
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
import org.lifetrack.ltapp.ui.screens.VitalScreen

@Composable
fun LTNavigation(navController: NavHostController, startDestination: String ) {
    val activity = LocalActivity.current as? ComponentActivity
        ?: throw IllegalStateException("LTNavigation must be hosted in a ComponentActivity")

    LaunchedEffect(navController) {
        navController.currentBackStackEntryFlow.collect { backStackEntry ->
            val baseRoute = backStackEntry.destination.route?.substringBefore("/")
            LTNavDispatch.updateCurrentRoute(baseRoute)
        }
    }
    val authPresenter = koinViewModel<AuthPresenter>(viewModelStoreOwner = activity)
    val userPresenter = koinViewModel<UserPresenter>(viewModelStoreOwner = activity)
    val sharedPresenter = koinViewModel<SharedPresenter>(viewModelStoreOwner = activity)
    val chatPresenter = koinViewModel<ChatPresenter>(viewModelStoreOwner = activity)
    val homePresenter = koinViewModel<HomePresenter>(viewModelStoreOwner = activity)
    val fuvPresenter = koinViewModel<FUVPresenter>(viewModelStoreOwner = activity)

    NavHost(
        navController = navController,
        startDestination = startDestination,
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Start,
                animationSpec = tween(durationMillis = 150)
            ) + fadeOut(animationSpec = tween(150))
        },
        popEnterTransition = {
            slideIntoContainer(towards = AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = tween(durationMillis = 150)
            ) + fadeIn(animationSpec = tween(150))
        },
        popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = tween(durationMillis = 150) // faster
            ) + fadeOut(animationSpec = tween(150))
        }
    ) {
        navigation(startDestination = "login", route = "auth_graph") {
            composable("login") {
                LoginScreen(authPresenter, sharedPresenter)
            }
            composable("signup") {
                SignupScreen(authPresenter)
            }
            composable("restore") {
                RestoreScreen()
            }
        }

        navigation(startDestination = "home", route = "home_graph") {
            composable("home") {
                HomeScreen(homePresenter, userPresenter, authPresenter, sharedPresenter)
            }
            composable("profile") {
                ProfileScreen(authPresenter, userPresenter)
            }
            composable("menu") {
                MenuScreen(authPresenter, sharedPresenter)
            }
            composable("alma") {
                AlmaScreen(chatPresenter)
            }
            composable("chats") {
                ChatScreen(chatPresenter)
            }

            addHealthFeatures( userPresenter, authPresenter)

            addSupportFeatures(sharedPresenter)

            addUtilityFeatures( fuvPresenter)
        }
    }
}

fun NavGraphBuilder.addHealthFeatures(userPresenter: UserPresenter, authPresenter: AuthPresenter) {
    composable("analytics") {
        AnalyticScreen()//userPresenter)
    }
    composable("prescriptions") {
        PrescriptScreen(userPresenter, koinViewModel<PrescPresenter>())
    }
    composable("appointments") {
        AppointScreen(userPresenter)
    }
    composable(
        route = "prescription_detail/{medId}",
        arguments = listOf(navArgument("medId") { type = NavType.StringType })
    ) { backStackEntry ->
        val medId = backStackEntry.arguments?.getString("medId")
        val prescription = userPresenter.dummyPrescriptions.find { it.id == medId }
        prescription?.let {
            PDetailScreen(authPresenter = authPresenter, prescription = it)
        }
    }
    composable("telemedicine") {
        TelemedicineScreen()
    }
}

fun NavGraphBuilder.addSupportFeatures(sharedPresenter: SharedPresenter) {
    composable("about") { AboutScreen(sharedPresenter) }
    composable("support") { SupportScreen() }
    composable("alerts") { AlertScreen() }
    composable("bar-bra") { MainScreen() }
    composable("koala") { VitalScreen() }
}

fun NavGraphBuilder.addUtilityFeatures(fuvPresenter: FUVPresenter) {
    composable("FUV") {
        FollowUpScreen(fuvPresenter)
    }
    composable("timeline") { TimeLineScreen(koinViewModel<TLinePresenter>()) }
    composable("other") { OtherScreen() }
}