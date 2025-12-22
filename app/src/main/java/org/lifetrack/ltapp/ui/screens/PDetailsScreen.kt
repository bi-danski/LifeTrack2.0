package org.lifetrack.ltapp.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleLeft
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import org.lifetrack.ltapp.core.utils.ScheduleUtility
import org.lifetrack.ltapp.core.utils.openDialer
import org.lifetrack.ltapp.model.data.dclass.Prescription
import org.lifetrack.ltapp.ui.components.detailscreen.ContactPharmacyCard
import org.lifetrack.ltapp.ui.components.detailscreen.DetailHeaderCard
import org.lifetrack.ltapp.ui.components.detailscreen.DosageTrackerCard
import org.lifetrack.ltapp.ui.components.detailscreen.SafetyInfoGrid
import org.lifetrack.ltapp.ui.theme.Purple40


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PDetailScreen(
    navController: NavController,
    prescription: Prescription
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    var isReminderEnabled by remember { mutableStateOf(true) }
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            isReminderEnabled = true
            ScheduleUtility.scheduleReminder(context, prescription)
        } else {
            isReminderEnabled = false
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Prescription Details",
                        fontWeight = FontWeight.Bold,
                        color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onPrimary
                        )
                    },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowCircleLeft,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* Share logic */ }) {
                        Icon(Icons.Default.Share, contentDescription = "Share")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = if (isSystemInDarkTheme()) Color(0xFF121212) else Purple40//Color(0xFFF8F9FF)
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(if (isSystemInDarkTheme()) Color(0xFF121212) else Color(0xFFF8F9FF))
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            DetailHeaderCard(prescription)

            DosageTrackerCard(
                isReminderEnabled = isReminderEnabled,
                onReminderToggle = { enabled ->
                    if (enabled) {
                        val status = ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.POST_NOTIFICATIONS
                        )
                        if (status == PackageManager.PERMISSION_GRANTED) {
                            isReminderEnabled = true
                            ScheduleUtility.scheduleReminder(context, prescription)
                        } else {
                            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        }
                    } else {
                        isReminderEnabled = false
                        ScheduleUtility.cancelReminder(context, prescription)
                    }
                }
            )

            SafetyInfoGrid()

            ContactPharmacyCard(
                prescription,
                onCallPharmacy = { context.openDialer("911") },
                onMessageDoctor = {}
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}








