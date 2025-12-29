package org.lifetrack.ltapp.presenter

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.lifetrack.ltapp.model.data.dclass.*
import org.lifetrack.ltapp.model.data.LtMockData

class FUVPresenter : ViewModel() {

    private val _hospitalData = MutableStateFlow<List<HospitalVisit>>(emptyList())
    val hospitalData = _hospitalData.asStateFlow()

    private val _upcomingVisits = MutableStateFlow<List<UpcomingVisit>>(emptyList())
    val upcomingVisits = _upcomingVisits.asStateFlow()

    private val _isUpcomingExpanded = MutableStateFlow(false)
    val isUpcomingExpanded = _isUpcomingExpanded.asStateFlow()

    private val _showFilterSheet = MutableStateFlow(false)
    val showFilterSheet = _showFilterSheet.asStateFlow()

    private val _selectedFilter = MutableStateFlow<VisitFilter>(VisitFilter.Recent)
    val selectedFilter = _selectedFilter.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        _hospitalData.value = LtMockData.allHospitalVisits
        _upcomingVisits.value = LtMockData.upcomingData
    }

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
        _hospitalData.update { current ->
            when (filter) {
                is VisitFilter.Alphabetical -> current.sortedBy { it.hospitalName }
                is VisitFilter.Oldest -> current.sortedBy { it.subVisits.firstOrNull()?.timestamp }
                is VisitFilter.Recent -> current.sortedByDescending { it.subVisits.firstOrNull()?.timestamp }
            }
        }
    }
}