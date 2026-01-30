package org.lifetrack.ltapp.ui.screens

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleLeft
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.lifetrack.ltapp.model.data.dclass.AppointmentStatus
import org.lifetrack.ltapp.presenter.UserPresenter
import org.lifetrack.ltapp.ui.components.appointscreen.AppointmentCard
import org.lifetrack.ltapp.ui.components.appointscreen.AppointmentSwipeCard
import org.lifetrack.ltapp.ui.components.appointscreen.DoctorSelectionDropDown
import org.lifetrack.ltapp.ui.components.appointscreen.StatusChip
import org.lifetrack.ltapp.ui.navigation.LTNavDispatch
import org.lifetrack.ltapp.ui.theme.Purple40

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointScreen(userPresenter: UserPresenter) {
    val appointments by userPresenter.userAppointments.collectAsState()
    val currentFilter by userPresenter.selectedFilter.collectAsState()
    val selectedDoctor by userPresenter.selectedDoctorProfile.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Appointments", fontWeight = FontWeight.SemiBold)
                },
                navigationIcon = {
                    IconButton(onClick = { LTNavDispatch.navigateBack() }) {
                        Icon(Icons.Default.ArrowCircleLeft,
                            "Back",
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Purple40,
                    navigationIconContentColor = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onPrimary,
                    titleContentColor = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onPrimary
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
//                        .fill
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    DoctorSelectionDropDown(
                        selectedDoctor = selectedDoctor,
                        onSelectDoctorProfile = { doc ->
                            userPresenter.onSelectDoctor(doc)
                        }
                    )
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
                    items(AppointmentStatus.entries) { status ->
                        StatusChip(
                            label = status.label,
                            count = userPresenter.getCountForStatus(status).toString(),
                            accentColor = status.color,
                            icon = status.icon,
                            isSelected = currentFilter == status,
                            onClick = { userPresenter.onFilterChanged(status) }
                        )
                    }
                }
            }

            item {
                Text(
                    text = "${currentFilter.label} Appointments",
                    fontWeight = FontWeight.ExtraBold,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(horizontal = 16.dp),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.primary else Purple40
                )
            }

            if (appointments.isEmpty()) {
                item {
                    Box(Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                        Text("No ${currentFilter.label} appointments found.", color = Color.Gray)
                    }
                }
            } else {
                items(items = appointments, key = { it.id }) { appointment ->
                    if (currentFilter != AppointmentStatus.DISMISSED) {
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