package org.lifetrack.ltapp.presenter

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.lifetrack.ltapp.model.LtMockData
import org.lifetrack.ltapp.model.data.dclass.HospitalVisit
import org.lifetrack.ltapp.model.data.dclass.UpcomingVisit
import org.lifetrack.ltapp.model.data.dclass.VisitFilter


class FUVPresenter : ViewModel() {
    val hospitalData = mutableStateListOf<HospitalVisit>().apply {
        addAll(LtMockData.allHospitalVisits)
    }
    val upcomingVisits = mutableStateListOf<UpcomingVisit>().apply {
        addAll(LtMockData.upcomingData)
    }
    private val _isUpcomingExpanded = MutableStateFlow(false)
    val isUpcomingExpanded = _isUpcomingExpanded.asStateFlow()

    private val _showFilterSheet = MutableStateFlow(false)
    val showFilterSheet = _showFilterSheet.asStateFlow()

    private val _selectedFilter = MutableStateFlow<VisitFilter>(VisitFilter.Recent)
    val selectedFilter = _selectedFilter.asStateFlow()

    private val _filterOptions = MutableStateFlow(listOf(VisitFilter.Recent, VisitFilter.Oldest, VisitFilter.Alphabetical))
    val filterOptions = _filterOptions.asStateFlow()


    fun toggleUpcomingExpansion() = _isUpcomingExpanded.update { !it }

    fun setFilterSheetVisibility(visible: Boolean) {
        _showFilterSheet.value = visible
    }

    fun onFilterSelected(filter: VisitFilter) {
        _selectedFilter.value = filter
        _showFilterSheet.value = false
        applySorting(filter)
    }

    private fun applySorting(filter: VisitFilter) {
        viewModelScope.launch {
            val sortedList = withContext(Dispatchers.Default) {
                when (filter) {
                    is VisitFilter.Alphabetical -> {
                        hospitalData.sortedBy { it.hospitalName }
                    }
                    is VisitFilter.Oldest -> {
                        hospitalData.sortedBy { it.subVisits.firstOrNull()?.timestamp }
                    }
                    is VisitFilter.Recent -> {
                        hospitalData.sortedByDescending { it.subVisits.firstOrNull()?.timestamp }
                    }
                }
            }
            hospitalData.clear()
            hospitalData.addAll(sortedList)
        }
    }
}