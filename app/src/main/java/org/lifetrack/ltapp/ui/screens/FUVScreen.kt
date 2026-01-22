package org.lifetrack.ltapp.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.lifetrack.ltapp.model.data.dclass.*
import org.lifetrack.ltapp.presenter.FUVPresenter
import org.lifetrack.ltapp.ui.components.fuvscreen.FollowUpDetailCard
import org.lifetrack.ltapp.ui.components.fuvscreen.HospitalVisitNode
import org.lifetrack.ltapp.ui.theme.Purple40

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FollowUpScreen(
    navController: NavController,
    fuvPresenter: FUVPresenter
) {
    val history = fuvPresenter.hospitalData
    val upcoming = fuvPresenter.upcomingVisits
    val isExpanded by fuvPresenter.isUpcomingExpanded.collectAsState()
    val showSheet by fuvPresenter.showFilterSheet.collectAsState()
    val activeFilter by fuvPresenter.selectedFilter.collectAsState()

    val sheetState = rememberModalBottomSheetState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Visits",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onPrimaryContainer else Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowCircleLeft, "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Purple40)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            if (upcoming.isNotEmpty()) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Upcoming",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.primary else Purple40
                        )
                        IconButton(onClick = { fuvPresenter.toggleUpcomingExpansion() }) {
                            Icon(
                                imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                contentDescription = null
                            )
                        }
                    }

                    FollowUpDetailCard(upcoming.first())

                    AnimatedVisibility(visible = isExpanded) {
                        Column(
                            modifier = Modifier
                                .padding(top = 12.dp)
                                .heightIn(max = 400.dp)
                                .verticalScroll(rememberScrollState()),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            upcoming.drop(1).forEach { visit ->
                                FollowUpDetailCard(visit)
                            }
                        }
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "History",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.primary else Purple40
                    )
                    Surface(
                        onClick = { fuvPresenter.setFilterSheetVisibility(true) },
                        shape = RoundedCornerShape(20.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(activeFilter.displayName, style = MaterialTheme.typography.labelLarge)
                            Icon(Icons.Default.ArrowDropDown, null)
                        }
                    }
                }
            }

            items(history) { visit ->
                HospitalVisitNode(visit)
            }
        }

        if (showSheet) {
            ModalBottomSheet(
                onDismissRequest = { fuvPresenter.setFilterSheetVisibility(false) },
                sheetState = sheetState,
//                containerColor = if (isSystemInDarkTheme()) Color(0xFF1E1E1E)
//                else Purple40
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .padding(bottom = 32.dp)
                ) {
                    Text(
                        "Sort By",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp),
//                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    filterOptions.forEach { filter ->
                        ListItem(
                            headlineContent = { Text(filter.displayName, fontWeight = FontWeight.SemiBold) },
                            leadingContent = {
                                RadioButton(
                                    selected = (filter == activeFilter),
                                    onClick = { },
                                    colors = RadioButtonDefaults.colors(
                                        selectedColor = if (isSystemInDarkTheme()) Color.Green else Purple40,
//                                        unselectedColor = Color.Black
                                    )

                                )
                            },
                            modifier = Modifier
//                                .background(MaterialTheme.colorScheme.background)
                                .clickable { fuvPresenter.onFilterSelected(filter) },
                            colors = ListItemDefaults.colors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainerLow
//                                    if (isSystemInDarkTheme()) Color(0xFF1E1E1E) else Purple40
                            )
                        )
                    }
                }
            }
        }
    }
}