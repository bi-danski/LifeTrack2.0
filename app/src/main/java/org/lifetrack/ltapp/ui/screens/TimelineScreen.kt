package org.lifetrack.ltapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import org.lifetrack.ltapp.core.utility.formatMonthYear
import org.lifetrack.ltapp.core.utility.toYearMonth
import org.lifetrack.ltapp.model.data.dclass.VisitStatus
import org.lifetrack.ltapp.presenter.TLinePresenter
import org.lifetrack.ltapp.ui.components.timelinescreen.MedicalVisitCard
import org.lifetrack.ltapp.ui.components.timelinescreen.TimelineIndicator
import org.lifetrack.ltapp.ui.navigation.NavDispatcher
import org.lifetrack.ltapp.ui.theme.Purple40

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeLineScreen(presenter: TLinePresenter = viewModel()) {
    val filteredVisits by presenter.filteredVisits.collectAsState()
    val searchQuery by presenter.searchQuery.collectAsState()
    val selectedFilter by presenter.selectedFilter.collectAsState()
    val selectedVisitIds by presenter.selectedVisitIds.collectAsState()
    val showShareSheet by presenter.showShareSheet.collectAsState()
    val isSelectionMode by presenter.isSelectionMode.collectAsState()
    val expandedId by presenter.expandedVisitId.collectAsState()
    val isRefreshing by presenter.isRefreshing.collectAsState()
    val isSearchActive by presenter.isSearchActive.collectAsState()

    val visitsToDisplay = filteredVisits.sortedByDescending { it.date }
    val groupedVisits = remember(visitsToDisplay) {
        visitsToDisplay.groupBy { it.date.toYearMonth() }
    }
    val pullToRefreshState = rememberPullToRefreshState()

    if (showShareSheet) {
        ModalBottomSheet(
            onDismissRequest = presenter::closeShareSheet,
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Share Health Records",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Medium
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    "${selectedVisitIds.size} record${if (selectedVisitIds.size != 1) "s" else ""} selected",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(16.dp))

                ListItem(
                    headlineContent = { Text("Share selected records", fontWeight = FontWeight.SemiBold) },
                    leadingContent = { Icon(Icons.Default.Share, null) },
                    modifier = Modifier.clickable { presenter.shareSelectedRecords() },
                    colors = ListItemDefaults.colors(
                        MaterialTheme.colorScheme.surfaceContainerLow
//                        containerColor = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.surfaceContainerLow else
//                            MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f)
                    )
                )
                ListItem(
                    headlineContent = { Text("Download summary PDF", fontWeight = FontWeight.SemiBold) },
                    leadingContent = { Icon(Icons.Default.Download, null) },
                    modifier = Modifier.clickable { presenter.downloadSelectedAsPdf() },
                    colors = ListItemDefaults.colors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerLow
//                            if (isSystemInDarkTheme()) MaterialTheme.colorScheme.surfaceContainerLow else
//                            MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f)
                    )
                )
                ListItem(
                    headlineContent = { Text("Request correction", fontWeight = FontWeight.SemiBold) },
                    leadingContent = { Icon(Icons.Default.Info, null) },
                    modifier = Modifier.clickable { },
                    colors = ListItemDefaults.colors(
                        MaterialTheme.colorScheme.surfaceContainerLow
//                        containerColor = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.surfaceContainerLow else
//                            MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f)
                    )
                )

                Spacer(Modifier.height(16.dp))
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = presenter::closeShareSheet) {
                        Text("Cancel", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }

    val showFilterSheet by presenter.showFilterSheet.collectAsState()
    if (showFilterSheet) {
        ModalBottomSheet(onDismissRequest = presenter::closeFilterSheet) {
            Column(Modifier.padding(16.dp)) {
                Text("Filter by Status", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(16.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    VisitStatus.entries.forEach { status ->
                        FilterChip(
                            selected = selectedFilter == status,
                            onClick = {
                                presenter.setFilter(if (selectedFilter == status) null else status)
                                presenter.closeFilterSheet()
                            },
                            label = { Text(status.label) }
                        )
                    }
                }
                Spacer(Modifier.height(16.dp))
                TextButton(
                    onClick = presenter::clearFilter,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Clear Filter", fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    if (isSelectionMode) {
                        Text(
                            "${selectedVisitIds.size} selected",
                            color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onPrimary

                        )
                    } else {
                        Text(
                            "Health Records",
                            fontWeight = FontWeight.SemiBold,
                            color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onPrimary

                        )
                    }
                },
                navigationIcon = {
                    if (isSelectionMode) {
                        IconButton(onClick = presenter::clearSelection) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Cancel selection",
                                tint = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onPrimary

                            )
                        }
                    } else {
                        IconButton(onClick = { NavDispatcher.navigateBack() }) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                },
                actions = {
                    if (isSelectionMode) {
                        IconButton(
                            onClick = presenter::openShareSheet,
                            enabled = selectedVisitIds.isNotEmpty()
                        ) {
                            Icon(
                                Icons.Default.Share,
                                "Share selected",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    } else {
                        IconButton(onClick = presenter::openSearch) {
                            Icon(
                                Icons.Default.Search,
                                "Search",
                                tint = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onPrimary
                            )
                        }
                        IconButton(onClick = presenter::openFilterSheet) {
                            Icon(
                                Icons.Default.FilterList,
                                "Filter",
                                tint = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Purple40)
            )
        },
        floatingActionButton = {
            if (!isSelectionMode) {
                FloatingActionButton(onClick = presenter::openShareSheet) {
                    Icon(Icons.Default.Share, contentDescription = "Share records")
                }
            }
        }
    ) { paddingValues ->

        PullToRefreshBox(
            modifier = Modifier.padding(paddingValues),
            isRefreshing = isRefreshing,
            onRefresh = presenter::refreshData,
            state = pullToRefreshState
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                state = rememberLazyListState()
            ) {
                if (isSearchActive) {
                    item {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = presenter::onSearchQueryChange,
                            placeholder = { Text("Search diagnosis, treatment, doctor...") },
                            leadingIcon = { Icon(Icons.Default.Search, null) },
                            trailingIcon = {
                                if (searchQuery.isNotEmpty()) {
                                    IconButton(onClick = presenter::clearSearch) {
                                        Icon(Icons.Default.Close, "Clear search")
                                    }
                                }
                            },
                            modifier = Modifier
                                .padding(top = 12.dp)
                                .fillMaxWidth(),
                            singleLine = true
                        )
                        Spacer(Modifier.height(8.dp))
                    }
                }

                if (selectedFilter != null) {
                    item {
                        FilterChip(
                            selected = true,
                            onClick = presenter::clearFilter,
                            label = { Text(selectedFilter!!.label) },
                            trailingIcon = { Icon(Icons.Default.Close, "Remove filter") }
                        )
                        Spacer(Modifier.height(8.dp))
                    }
                }

                if (visitsToDisplay.isEmpty()) {
                    item {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(
                                "No records found",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                } else {
                    groupedVisits.forEach { (yearMonth, visitsInMonth) ->
                        stickyHeader {
                            Text(
                                text = yearMonth.formatMonthYear(),
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .background(MaterialTheme.colorScheme.background)
                                    .padding(vertical = 8.dp, horizontal = 4.dp)
                                    .fillMaxWidth()
                            )
                        }

                        itemsIndexed(visitsInMonth) { index, visit ->
                            val isExpanded = expandedId == visit.id
                            val isSelected = visit.id in selectedVisitIds
                            val isLastInGroup = index == visitsInMonth.lastIndex
                            val isOverallLast = yearMonth == groupedVisits.keys.last() && isLastInGroup

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.Top
                            ) {
                                TimelineIndicator(
                                    status = visit.status,
                                    isLast = isOverallLast
                                )

                                Spacer(Modifier.width(12.dp))

                                MedicalVisitCard(
                                    visit = visit,
                                    isExpanded = isExpanded,
                                    isSelected = isSelected,
//                                    notes = notes[visit.id].orEmpty(),
                                    onToggleExpanded = { presenter.toggleExpanded(visit.id) },
                                    onToggleSelection = {
                                        if (isSelectionMode) {
                                            presenter.toggleSelection(visit.id)
                                        } else {
                                            presenter.toggleExpanded(visit.id)
                                        }
                                    },
                                    onBookmark = { presenter.toggleBookmark(visit.id) },
                                    onShare = presenter::openShareSheet
                                )
                            }
                        }
                    }
                }

                item {
                    Spacer(Modifier.height(32.dp))
                    Text(
                        "End of Timeline",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.outline
                    )
                    Spacer(Modifier.height(80.dp))
                }
            }
        }
    }
}