package org.lifetrack.ltapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleLeft
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.lifetrack.ltapp.presenter.AnalyticPresenter
import org.lifetrack.ltapp.ui.components.homescreen.LifeTrackTopBar
import org.lifetrack.ltapp.ui.components.medicalcharts.PrescriptionItem

@Composable
fun PrescriptScreen(
    navController: NavController,
    presenter: AnalyticPresenter
    ) {
    val dummyPrescriptions = presenter.dummyPrescriptions

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
                .fillMaxSize(),
            contentPadding = innerPadding,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(dummyPrescriptions){  prescription ->
                PrescriptionItem(prescription)
            }
        }

    }
}