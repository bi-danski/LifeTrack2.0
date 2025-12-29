package org.lifetrack.ltapp.presenter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.lifetrack.ltapp.model.data.LtMockData
import org.lifetrack.ltapp.model.data.dclass.MedicalVisit
import org.lifetrack.ltapp.model.data.dclass.VisitStatus

class TLinePresenter : ViewModel() {

    private val originalVisits = LtMockData.allMedicalVisits.sortedByDescending { it.date }

    private val _allUserMedicalVisits = MutableStateFlow(originalVisits)
    val allUserMedicalVisits: StateFlow<List<MedicalVisit>> = _allUserMedicalVisits

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _isSearchActive = MutableStateFlow(false)
    val isSearchActive: StateFlow<Boolean> = _isSearchActive

    private val _selectedFilter = MutableStateFlow<VisitStatus?>(null)
    val selectedFilter: StateFlow<VisitStatus?> = _selectedFilter

    private val _showFilterSheet = MutableStateFlow(false)
    val showFilterSheet: StateFlow<Boolean> = _showFilterSheet

    val filteredVisits: StateFlow<List<MedicalVisit>> = combine(
        allUserMedicalVisits,
        searchQuery,
        selectedFilter
    ) { visits, query, filter ->
        visits.filter { visit ->
            val matchesSearch = query.isBlank() ||
                    visit.diagnosis.contains(query, ignoreCase = true) ||
                    visit.treatment.contains(query, ignoreCase = true) ||
                    visit.doctor.contains(query, ignoreCase = true) ||
                    visit.notes.contains(query, ignoreCase = true)

            val matchesFilter = filter == null || visit.status == filter

            matchesSearch && matchesFilter
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), originalVisits)

    private val _expandedVisitId = MutableStateFlow<Int?>(null)
    val expandedVisitId: StateFlow<Int?> = _expandedVisitId

    private val _showShareSheet = MutableStateFlow(false)
    val showShareSheet: StateFlow<Boolean> = _showShareSheet

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    private val _selectedVisitIds = MutableStateFlow<Set<Int>>(emptySet())
    val selectedVisitIds: StateFlow<Set<Int>> = _selectedVisitIds

    private val _isSelectionMode = MutableStateFlow(false)
    val isSelectionMode: StateFlow<Boolean> = _isSelectionMode

    private val _bookmarkedVisits = MutableStateFlow<Set<Int>>(emptySet())
    val bookmarkedVisits: StateFlow<Set<Int>> = _bookmarkedVisits

    private val _patientNotes = MutableStateFlow<Map<Int, String>>(emptyMap())
    val patientNotes: StateFlow<Map<Int, String>> = _patientNotes


    fun updateNote(visitId: Int, note: String) {
        _patientNotes.value = _patientNotes.value.toMutableMap().apply { this[visitId] = note.trim() }
    }

    fun startSelection(initialId: Int) {
        _isSelectionMode.value = true
        toggleSelection(initialId)
    }

    fun toggleSelection(visitId: Int) {
        _selectedVisitIds.value = if (_selectedVisitIds.value.contains(visitId)) {
            _selectedVisitIds.value - visitId
        } else {
            _selectedVisitIds.value + visitId
        }
        if (_selectedVisitIds.value.isEmpty()) {
            _isSelectionMode.value = false
        }
    }

    fun toggleBookmark(visitId: Int) {
        _bookmarkedVisits.value = if (_bookmarkedVisits.value.contains(visitId)) {
            _bookmarkedVisits.value - visitId
        } else {
            _bookmarkedVisits.value + visitId
        }
    }

    fun clearSelection() {
        _selectedVisitIds.value = emptySet()
        _isSelectionMode.value = false
    }

    fun toggleExpanded(visitId: Int) {
        _expandedVisitId.value = if (_expandedVisitId.value == visitId) null else visitId
    }

    fun openShareSheet() { _showShareSheet.value = true }
    fun closeShareSheet() { _showShareSheet.value = false }

    fun openSearch() { _isSearchActive.value = true }

    fun onSearchQueryChange(query: String) { _searchQuery.value = query }

    fun clearSearch() {
        _searchQuery.value = ""
        _isSearchActive.value = false
    }

    fun openFilterSheet() { _showFilterSheet.value = true }
    fun closeFilterSheet() { _showFilterSheet.value = false }

    fun setFilter(status: VisitStatus?) { _selectedFilter.value = status }
    fun clearFilter() { _selectedFilter.value = null }

    fun refreshData() {
        viewModelScope.launch {
            _isRefreshing.value = true
            delay(1500)
            _allUserMedicalVisits.value = LtMockData.allMedicalVisits.shuffled()
            _isRefreshing.value = false
        }
    }

    fun shareSelectedRecords() {
        clearSelection()
        closeShareSheet()
    }

    fun downloadSelectedAsPdf() {
        clearSelection()
        closeShareSheet()
    }
}