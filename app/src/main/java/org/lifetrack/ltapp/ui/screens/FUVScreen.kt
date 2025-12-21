package org.lifetrack.ltapp.ui.screens

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.lifetrack.ltapp.presenter.FUVPresenter
import org.lifetrack.ltapp.ui.components.fuvscreen.FollowUpDetailCard
import org.lifetrack.ltapp.ui.components.fuvscreen.HospitalVisitNode
import org.lifetrack.ltapp.ui.theme.Purple40


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FollowUpScreen(
    navController: NavController,
    fuvPresenter: FUVPresenter
){
    val hospitalData = fuvPresenter.hospitalData.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Follow Ups + Visits",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onPrimary

                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowCircleLeft, "Back")
                    }
                },
                actions = { },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Purple40,
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                Text(
                    "Upcoming Follow-up",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 15.dp, top = 10.dp),
                    color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.primary else Purple40
                )
                FollowUpDetailCard(
                    location = "Mama Lucy Kibaki",
                    time = "10:00 A.M",
                    treatment = "Chemotherapy",
                    date = "Mon 28th December 2025"
                )
                Spacer(modifier = Modifier.height(22.dp))
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Visits",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(bottom = 8.dp, top = 0.dp),
                        color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.primary else Purple40
                    )
                    Surface(
                        onClick = { /* Open modern bottom sheet */ },
                        shape = RoundedCornerShape(20.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant
                    ) {

                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Recent Visits", style = MaterialTheme.typography.labelLarge)
                            Icon(Icons.Default.ArrowDropDown, null)
                        }
                    }
                }
            }

            items(hospitalData.value) { visit ->
                HospitalVisitNode(visit)
            }
        }
    }
}


