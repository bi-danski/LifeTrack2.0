package org.lifetrack.ltapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.lifetrack.ltapp.presenter.HomePresenter
import org.lifetrack.ltapp.presenter.SharedPresenter
import org.lifetrack.ltapp.presenter.UserPresenter
import org.lifetrack.ltapp.ui.components.carousels.LtHomeCarousel
import org.lifetrack.ltapp.ui.components.homescreen.AppBottomBar
import org.lifetrack.ltapp.ui.components.homescreen.AppTopBar
//import org.lifetrack.ltapp.ui.components.homescreen.FeatureGrid
import org.lifetrack.ltapp.ui.components.homescreen.GlassFloatingActionButton
import org.lifetrack.ltapp.ui.components.homescreen.featureGridContent


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    presenter: HomePresenter,
    userPresenter: UserPresenter,
    sharedPresenter: SharedPresenter
) {
    val autoRotate2NextCard = presenter.autoRotate2NextCard
    val caroItemsCount = presenter.caroItemsCount
    val homeScreenContextInstance = LocalContext.current

    Scaffold(
        floatingActionButton = {
            GlassFloatingActionButton(onClick = { navController.navigate("alma") }) {
                Icon(Icons.AutoMirrored.Filled.Chat, contentDescription = "Quick Chat")
            }
        },
        bottomBar = { AppBottomBar(navController) },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 10.dp),
            verticalArrangement = Arrangement.spacedBy(15.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Column {
                    Spacer(Modifier.height(8.dp))
                    AppTopBar(navController)
                    Spacer(Modifier.height(18.dp))
                    LtHomeCarousel(
                        autoRotate = autoRotate2NextCard,
                        itemsCount = caroItemsCount,
                        userPresenter = userPresenter,
                        onEmergencyClickAction = {
                            sharedPresenter.handleEmergencyCall(
                                homeScreenContextInstance
                            )
                        }
                    )
                    Spacer(Modifier.height(18.dp))
                }
            }

            featureGridContent(navController)

            item(span = { GridItemSpan(maxLineSpan) }) {
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}
