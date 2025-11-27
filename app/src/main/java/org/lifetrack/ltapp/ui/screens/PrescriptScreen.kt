package org.lifetrack.ltapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleLeft
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import org.lifetrack.ltapp.presenter.AnalyticPresenter
import org.lifetrack.ltapp.ui.components.homescreen.LifeTrackTopBar
import org.lifetrack.ltapp.ui.components.medicalcharts.PrescriptionItem

@Composable
fun PrescriptScreen(
    navController: NavController,
    presenter: AnalyticPresenter
    ) {
    val dummyPrescriptions = presenter.dummyPrescriptions
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(dummyPrescriptions.size) {
        scope.launch {
            listState.animateScrollToItem(dummyPrescriptions.size)
        }
    }

    Scaffold(
        topBar = {
            LifeTrackTopBar(
                title = "Prescriptions",
                navigationIcon = Icons.Default.ArrowCircleLeft,
                backCallback = {
                    navController.popBackStack()
                },
                actionIcon = Icons.Default.Share,
                actionCallback = {}

            )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            state = listState,

            contentPadding = innerPadding,
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(dummyPrescriptions){  prescription ->
                PrescriptionItem(prescription)
            }
        }

    }
}