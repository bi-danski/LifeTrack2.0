package org.lifetrack.ltapp.presenter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.lifetrack.ltapp.model.data.dclass.*
import org.lifetrack.ltapp.model.data.LtMockData


class UserPresenter : ViewModel() {

    private val _profileInfo = MutableStateFlow(ProfileInfo())
    val profileInfo = _profileInfo.asStateFlow()

    private val _allAppointments = MutableStateFlow(LtMockData.dummyAppointments)

    val nextUpcomingAppointment = _allAppointments.map { list ->
        list.filter { it.status.equals("Upcoming", ignoreCase = true) }.minByOrNull { it.time }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _selectedFilter = MutableStateFlow("Upcoming")
    val selectedFilter = _selectedFilter.asStateFlow()

    private val _userAppointments = MutableStateFlow<List<Appointment>>(emptyList())
    val userAppointments = _userAppointments.asStateFlow()

    private val _selectedDoctorProfile = MutableStateFlow<DoctorProfile?>(null)
    val selectedDoctorProfile = _selectedDoctorProfile.asStateFlow()

    init {
        loadUserProfile()
        observeAppointments()
    }

    private fun observeAppointments() {
        viewModelScope.launch {
            combine(_allAppointments, _selectedFilter) { appointments, filter ->
                appointments.filter { it.status.equals(filter, ignoreCase = true) }
            }.collect { _userAppointments.value = it }
        }
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            val fetchedUserName = "Dr. Najma"
            val fetchedUserEmail = "najma@lifetrack.org"
            val userPhoneNumber = "+250788888888"

            val initials = fetchedUserName.split(" ")
                .filter { it.isNotBlank() }
                .map { it.first() }
                .joinToString("")
                .uppercase()

            _profileInfo.value = ProfileInfo(
                userName = fetchedUserName,
                userEmail = fetchedUserEmail,
                userInitials = initials,
                userPhoneNumber = userPhoneNumber
            )
        }
    }

    fun onMenuItemAction(navController: NavController, route: String) {
        navController.navigate(route) { launchSingleTop = true }
    }

    fun onFilterChanged(newFilter: String) {
        _selectedFilter.value = newFilter
    }

    fun getCountForStatus(status: String): Int {
        return _allAppointments.value.count { it.status.equals(status, ignoreCase = true) }
    }

    fun bookAppointment() {
        val selectedDoc = _selectedDoctorProfile.value ?: return
        val date = "Today"
        val time = selectedDoc.availability.split("-").first().trim()
        val isDuplicate = _allAppointments.value.any {
            it.doctor == selectedDoc.name && it.date == date && it.time == time
        }

        if (!isDuplicate) {
            val newAppointment = Appointment(
                doctor = selectedDoc.name,
                date = date,
                time = time,
                hospital = selectedDoc.hospital,
                status = "Recently Booked"
            )
            _allAppointments.value += newAppointment
            onFilterChanged("Recently Booked")
            _selectedDoctorProfile.value = null
        }
    }

    fun dismissAppointment(appointment: Appointment) {
        _allAppointments.value = _allAppointments.value.map {
            if (it == appointment) it.copy(status = "Dismissed") else it
        }
    }

    fun undoDismiss(appointment: Appointment, originalStatus: String) {
        _allAppointments.value = _allAppointments.value.map {
            if (it == appointment) it.copy(status = originalStatus) else it
        }
    }

    fun restoreAppointment(appointment: Appointment) {
        _allAppointments.value = _allAppointments.value.map {
            if (it == appointment) it.copy(status = "Upcoming") else it
        }
    }

    fun onSelectDoctor(doctor: DoctorProfile) {
        _selectedDoctorProfile.value = doctor
    }
}

