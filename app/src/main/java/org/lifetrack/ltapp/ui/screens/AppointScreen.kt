package org.lifetrack.ltapp.ui.screens

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import org.lifetrack.ltapp.model.data.dclass.statusChips
import org.lifetrack.ltapp.presenter.UserPresenter
import org.lifetrack.ltapp.ui.components.appointscreen.AppointmentCard
import org.lifetrack.ltapp.ui.components.appointscreen.DoctorSelectionDropDown
import org.lifetrack.ltapp.ui.components.appointscreen.StatusChip
import org.lifetrack.ltapp.ui.components.appointscreen.AppointmentSwipeCard
import org.lifetrack.ltapp.ui.theme.Purple40

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointScreen(
    navController: NavController,
    userPresenter: UserPresenter
) {
    val appointments by userPresenter.userAppointments.collectAsState()
    val currentFilter by userPresenter.selectedFilter.collectAsState()
    val isDark = isSystemInDarkTheme()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Appointments",
                        fontWeight = FontWeight.SemiBold,
                        color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowCircleLeft, "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Purple40
                )
            )
        },
        bottomBar = {
            Surface(
                tonalElevation = 8.dp,
                shadowElevation = 8.dp,
                color = if (isSystemInDarkTheme()) Color(0xFF121212) else MaterialTheme.colorScheme.background
            ) {
                Column(
                    modifier = Modifier
                        .navigationBarsPadding()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    DoctorSelectionDropDown(userPresenter)
                    Button(
                        onClick = { userPresenter.bookAppointment() },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) { Text("Book Appointment") }
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)) {
            item {
                LazyRow(
                    modifier = Modifier.fillMaxWidth().padding(top = 15.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(statusChips) { chip ->
                        StatusChip(
                            label = chip.label,
                            count = userPresenter.getCountForStatus(chip.label).toString(),
                            accentColor = chip.color,
                            icon = chip.icon,
                            isSelected = currentFilter == chip.label,
                            onClick = { userPresenter.onFilterChanged(chip.label) }
                        )
                    }
                }
            }

            item {
                Text(
                    text = "$currentFilter Appointments",
                    fontWeight = FontWeight.ExtraBold,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(horizontal = 16.dp),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    color = if (isDark) MaterialTheme.colorScheme.primary else Purple40
                )
            }

            if (appointments.isEmpty()) {
                item {
                    Box(Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                        Text("No $currentFilter appointments found.", color = Color.Gray)
                    }
                }
            } else {
                items(items = appointments, key = { it.hashCode() }) { appointment ->
                    if (currentFilter != "Dismissed") {
                        AppointmentSwipeCard(
                            appointment = appointment,
                            onDismiss = {
                                val originalStatus = appointment.status
                                userPresenter.dismissAppointment(appointment)
                                scope.launch {
                                    val result = snackbarHostState.showSnackbar(
                                        message = "Moved to Dismissed",
                                        actionLabel = "Undo",
                                        duration = SnackbarDuration.Short
                                    )
                                    if (result == SnackbarResult.ActionPerformed) {
                                        userPresenter.undoDismiss(appointment, originalStatus)
                                    }
                                }
                            }
                        )
                    } else {
                        Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                            Column {
                                AppointmentCard(appointment)
                                TextButton(
                                    onClick = { userPresenter.restoreAppointment(appointment) },
                                    modifier = Modifier.align(Alignment.End)
                                ) {
                                    Icon(Icons.Default.Restore, null, modifier = Modifier.size(18.dp))
                                    Spacer(Modifier.width(8.dp))
                                    Text("Restore to Upcoming")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

