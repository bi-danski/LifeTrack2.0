package org.lifetrack.ltapp.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import org.lifetrack.ltapp.utility.formatMonthYear
import org.lifetrack.ltapp.utility.toYearMonth
import org.lifetrack.ltapp.model.data.dclass.MedicalVisit
import org.lifetrack.ltapp.presenter.TLinePresenter
import org.lifetrack.ltapp.ui.components.timelinescreen.StatusChip
import org.lifetrack.ltapp.ui.navigation.LTNavDispatch
import org.lifetrack.ltapp.ui.theme.Purple40

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeLineScreen(presenter: TLinePresenter) {
    val filteredVisits by presenter.filteredVisits.collectAsState()
    val isSelectionMode by presenter.isSelectionMode.collectAsState()
    val selectedVisitIds by presenter.selectedVisitIds.collectAsState()
    val expandedNodes by presenter.expandedNodes.collectAsState()
    val bookmarkedVisits by presenter.bookmarkedVisits.collectAsState()

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val isDark = isSystemInDarkTheme()

    val treeData = remember(filteredVisits) {
        filteredVisits
            .groupBy { it.date.year }
            .mapValues { it.value.groupBy { v -> v.date.toYearMonth() } }
    }

    LaunchedEffect(treeData) {
        if (expandedNodes.isEmpty() && treeData.isNotEmpty()) {
            val latestYear = treeData.keys.maxOrNull()
            val latestMonth = treeData[latestYear]?.keys?.maxOrNull()

            if (latestYear != null && latestMonth != null) {
                presenter.toggleNode("year_$latestYear")
                presenter.toggleNode("month_${latestYear}_${latestMonth.monthValue}")
            }
        }
    }

    val showScrollToTop by remember {
        derivedStateOf { listState.firstVisibleItemIndex > 3 }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = if (isSelectionMode) "${selectedVisitIds.size} Selected" else "Health Records",
                        color = if (isDark) MaterialTheme.colorScheme.onBackground else Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { if (isSelectionMode) presenter.clearSelection() else LTNavDispatch.navigateBack() }) {
                        Icon(
                            imageVector = if (isSelectionMode) Icons.Default.Close else Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    if (!isSelectionMode) {
                        IconButton(onClick = presenter::openSearch) { Icon(Icons.Default.Search, null, tint = Color.White) }
                        IconButton(onClick = presenter::openFilterSheet) { Icon(Icons.Default.FilterList, null, tint = Color.White) }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = if (isDark) MaterialTheme.colorScheme.background else Purple40,
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            if (showScrollToTop) {
                SmallFloatingActionButton(
                    onClick = { coroutineScope.launch { listState.animateScrollToItem(0) } },
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ) {
                    Icon(Icons.Default.ArrowUpward, "Scroll to top")
                }
            }
        },
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp),
            state = listState
        ) {
            treeData.forEach { (year, months) ->
                val yearId = "year_$year"
                item(key = yearId) {
                    TreeHeader(year.toString(), expandedNodes.contains(yearId), 0) { presenter.toggleNode(yearId) }
                }

                if (expandedNodes.contains(yearId)) {
                    months.forEach { (month, visits) ->
                        val monthId = "month_${year}_${month.monthValue}"
                        item(key = monthId) {
                            TreeHeader(month.formatMonthYear(),
                                expandedNodes.contains(monthId),
                                1
                            ) { presenter.toggleNode(monthId) }
                        }

                        if (expandedNodes.contains(monthId)) {
                            itemsIndexed(visits, key = { _, v -> v.id }) { index, visit ->
                                TreeVisitRow(
                                    visit = visit,
                                    isLast = index == visits.size - 1,
                                    isSelected = visit.id in selectedVisitIds,
                                    isBookmarked = visit.id in bookmarkedVisits,
                                    onToggle = {
                                        if (isSelectionMode) presenter.toggleSelection(visit.id)
                                        else presenter.toggleExpanded(visit.id)
                                    },
                                    onBookmark = { presenter.toggleBookmark(visit.id) }
                                )
                            }
                        }
                    }
                }
            }
            item { Spacer(Modifier.height(120.dp)) }
        }
    }
}

@Composable
fun TreeHeader(label: String, isExpanded: Boolean, level: Int, onClick: () -> Unit) {
    val isDark = isSystemInDarkTheme()
    val indentation = if (level == 0) 0.dp else 24.dp

    val bgColor = if (level == 0) {
        if (isDark) MaterialTheme.colorScheme.secondaryContainer//.copy(alpha = 0.4f)
        else MaterialTheme.colorScheme.secondaryContainer//.copy(alpha = 0.1f)
    } else {
        if (isDark) Color.Gray.copy(alpha = 0.2f)
        else Color.LightGray.copy(alpha = 0.3f)
    }

    val contentColor = if (isDark) Color.White else MaterialTheme.colorScheme.onSurface

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = bgColor,
        modifier = Modifier.padding(start = indentation, top = 12.dp, bottom = 4.dp)
            .then(if (level == 0) Modifier.fillMaxWidth() else Modifier.wrapContentWidth())
    ) {
        Row(Modifier.padding(horizontal = 20.dp, vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(label, color = contentColor, fontWeight = FontWeight.Bold)
            Spacer(Modifier.width(8.dp))
            Icon(
                imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = contentColor.copy(0.7f)
            )
        }
    }
}

@Composable
fun TreeVisitRow(
    visit: MedicalVisit,
    isLast: Boolean,
    isSelected: Boolean,
    isBookmarked: Boolean,
    onToggle: () -> Unit,
    onBookmark: () -> Unit
) {
    val isDark = isSystemInDarkTheme()
    Row(Modifier.fillMaxWidth().padding(start = 32.dp).height(IntrinsicSize.Min)) {
        Box(Modifier.width(48.dp).fillMaxHeight()) {
            Canvas(Modifier.fillMaxSize()) {
                val centerX = size.width / 2
                val centerY = size.height / 2
                val trunkColor = if (isDark) Color.Gray else Color.Black.copy(alpha = 0.1f)

                drawLine(trunkColor, Offset(centerX, 0f), Offset(centerX, if (isLast) centerY else size.height), 3f)
                drawLine(trunkColor, Offset(centerX, centerY), Offset(size.width, centerY), 3f)
                drawCircle(visit.status.color, 7f, Offset(centerX, centerY))
            }
        }
        MedVisitCard(
            visit = visit,
            isSelected = isSelected,
            isBookmarked = isBookmarked,
            modifier = Modifier.padding(vertical = 8.dp),
            onToggleSelection = onToggle,
            onToggleExpanded = onToggle,
            onBookmark = onBookmark
        )
    }
}

@Composable
fun MedVisitCard(
    visit: MedicalVisit,
    isSelected: Boolean,
    isBookmarked: Boolean,
    modifier: Modifier = Modifier,
    onToggleSelection: () -> Unit,
    onToggleExpanded: () -> Unit,
    onBookmark: () -> Unit
) {
    val textColor = if (isSystemInDarkTheme()) Color.White else Color.Black
    val subTextColor = textColor.copy(alpha = 0.65f)
    val cardBgColor = if (isSystemInDarkTheme()) visit.status.color.copy(alpha = if (isSelected) 0.3f else 0.2f)
        else visit.status.color.copy(alpha = if (isSelected) 0.2f else 0.1f)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onToggleExpanded() },
        colors = CardDefaults.cardColors(containerColor = cardBgColor),
        shape = RoundedCornerShape(12.dp),
        border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, if (isSystemInDarkTheme()) Color.White else Purple40) else null
    ) {
        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
            Surface(
                modifier = Modifier.width(6.dp).fillMaxHeight(),
                color = visit.status.color
            ) {}

            Column(Modifier.padding(16.dp)) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = visit.diagnosis,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = textColor,
                        )
                        StatusChip(visit.status)
                    }

                    IconButton(onClick = onBookmark) {
                        Icon(
                            imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "Bookmark",
                            tint = if (isBookmarked) Color(0xFFFBC02D) else textColor.copy(alpha = 0.4f)
                        )
                    }
                }
                Spacer(Modifier.height(8.dp))
                Text(visit.doctor,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Black
                    ),
                    color = subTextColor
                )
                Text(visit.date.toString(),
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Black,
                        fontSize = 14.sp
                    ),
                    color = subTextColor
                )
            }
        }
    }
}