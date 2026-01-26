package org.lifetrack.ltapp.ui.screens

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleLeft
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.lifetrack.ltapp.presenter.PrescPresenter
import org.lifetrack.ltapp.presenter.UserPresenter
import org.lifetrack.ltapp.ui.components.appointscreen.StatusChip
import org.lifetrack.ltapp.ui.components.prescriptscreen.PrescriptionCard
import org.lifetrack.ltapp.ui.components.prescriptscreen.SuccessRefillContent
import org.lifetrack.ltapp.ui.navigation.NavDispatcher
import org.lifetrack.ltapp.ui.theme.Purple40


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrescriptScreen(userPresenter: UserPresenter, presenter: PrescPresenter) {
    val prescriptions = userPresenter.dummyPrescriptions
    val isDark = isSystemInDarkTheme()
    val sheetState = rememberModalBottomSheetState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "E-Prescriptions",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isDark) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { NavDispatcher.navigateBack() }) {
                        Icon(Icons.Default.ArrowCircleLeft,
                            "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Purple40)
            )
        },
        containerColor = if (isDark) Color(0xFF121212) else Color(0xFFF8F9FF)
    ) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {

            LazyRow(
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(presenter.filters) { filter ->
                    val isSelected = presenter.selectedFilter == filter

                    val count = when (filter) {
                        "Active" -> prescriptions.count { it.status == "Active" }
                        "Refills" -> prescriptions.count { it.status == "Refill Due" }
                        "Expired" -> prescriptions.count { presenter.isDateExpired(it.endDate) }
                        else -> prescriptions.size
                    }

                    StatusChip(
                        label = filter,
                        count = count.toString(),
                        accentColor = presenter.getStatusColor(filter),
                        icon = presenter.getIconForFilter(filter),
                        isSelected = isSelected,
                        onClick = { presenter.selectedFilter = filter }
                    )
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                state = presenter.listState,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val filteredList = when (presenter.selectedFilter) {
                    "Active" -> prescriptions.filter { it.status == "Active" }
                    "Refills" -> prescriptions.filter { it.status == "Refill Due" }
                    "Expired" -> prescriptions.filter { presenter.isDateExpired(it.endDate) }
                    "All" -> prescriptions
                    else -> prescriptions
                }

                if (filteredList.isEmpty()) {
                    item {
                        Box(modifier = Modifier.fillParentMaxHeight(0.7f), contentAlignment = Alignment.Center) {
                            Text("No items found in ${presenter.selectedFilter}.", color = Color.Gray)
                        }
                    }
                } else {
                    items(filteredList, key = { it.id }) { prescription ->
                        PrescriptionCard(
                            prescription = prescription,
                            isExpired = presenter.isDateExpired(prescription.endDate),
                            onRefillRequest = { med ->
                                presenter.triggerRefillRequest(med.medicationName)
                            },
                            onCardClick = {
                                NavDispatcher.navigate("prescription_detail/${prescription.id}")
                            }
                        )
                    }
                }
                item { Spacer(modifier = Modifier.height(24.dp)) }
            }
        }

        if (presenter.showSuccessSheet) {
            ModalBottomSheet(
                onDismissRequest = { presenter.showSuccessSheet = false },
                sheetState = sheetState,
                containerColor = if (isDark) Color(0xFF1E1E1E) else Color.White,
                dragHandle = { BottomSheetDefaults.DragHandle(color = Color.Gray.copy(0.4f)) }
            ) {
                SuccessRefillContent(
                    medName = presenter.lastRequestedMedication,
                    onClose = { presenter.showSuccessSheet = false }
                )
            }
        }
    }
}