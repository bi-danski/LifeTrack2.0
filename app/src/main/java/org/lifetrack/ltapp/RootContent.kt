package org.lifetrack.ltapp

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import org.lifetrack.ltapp.model.data.dclass.SessionStatus
import org.lifetrack.ltapp.ui.components.other.LTRetry
import org.lifetrack.ltapp.ui.navigation.LTNavDispatch
import org.lifetrack.ltapp.ui.navigation.LTNavTarget
import org.lifetrack.ltapp.ui.navigation.LTNavigation
import org.lifetrack.ltapp.ui.theme.LTAppTheme


@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun RootContent(isOnline: Boolean, sessionStatus: SessionStatus) {
    LTAppTheme {
        val appNavController = rememberNavController()

        LaunchedEffect(appNavController) {
            LTNavDispatch.navigationEvents.collect { target ->
                when (target) {
                    is LTNavTarget.Screen -> {
                        appNavController.navigate(target.route) {
                            if (target.clearBackstack) {
                                appNavController.graph.startDestinationRoute?.let { route ->
                                    popUpTo(route) { inclusive = true }
                                }
                            }
                            launchSingleTop = target.launchSingleTop
                        }
                    }
                    is LTNavTarget.Back -> {
                        appNavController.popBackStack()
                    }
                }
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {
            Surface(modifier = Modifier.fillMaxSize()) {
                when (sessionStatus) {
                    SessionStatus.LOGGED_IN -> {
                        LTNavigation(navController = appNavController, startDestination = "home_graph")
                    }
                    SessionStatus.LOGGED_OUT -> {
                        LTNavigation(navController = appNavController, startDestination = "auth_graph")
                    }
                    else -> { }
                }
            }

            AnimatedVisibility(
                visible = !isOnline,
                enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 24.dp)
            ) {
                LTRetry()
            }
        }
    }
}
