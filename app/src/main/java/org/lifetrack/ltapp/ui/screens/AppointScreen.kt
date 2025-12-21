package org.lifetrack.ltapp.ui.screens

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleLeft
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.lifetrack.ltapp.presenter.UserPresenter
import org.lifetrack.ltapp.ui.components.appointscreen.AppointmentCard
import org.lifetrack.ltapp.ui.components.appointscreen.DoctorSelectionDropDown
import org.lifetrack.ltapp.ui.components.appointscreen.StatusChip
import org.lifetrack.ltapp.ui.theme.Purple40


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointScreen(
    navController: NavController,
    userPresenter: UserPresenter
) {
    val appointments = userPresenter.userAppointments.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Appointments",
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
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Purple40
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 15.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    item {
                        StatusChip("Upcoming", "14", MaterialTheme.colorScheme.primary, Icons.Default.CalendarMonth)
                    }
                    item {
                        StatusChip("Attended", "1", Color(0xFF2ECC71), Icons.Default.CheckCircle)
                    }
                    item {
                        StatusChip("Rescheduled", "3", Color(0xFFE67E22), Icons.Default.History)
                    }
                    item {
                        StatusChip("Dismissed", 5.toString(), Color.Gray, Icons.Default.Cancel)
                    }
                }
            }

            item {
                Text(
                    "Upcoming Appointments",
                    fontWeight = FontWeight.ExtraBold,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.primary else Purple40
                )
            }

            items(appointments.value) { appointment ->
                Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                    AppointmentCard(appointment)
                }
            }

            item {
                Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                    DoctorSelectionDropDown(userPresenter)
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Button(
                        onClick = { /* TODO */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Book Appointment")
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}