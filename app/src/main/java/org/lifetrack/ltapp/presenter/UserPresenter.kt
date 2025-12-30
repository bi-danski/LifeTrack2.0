package org.lifetrack.ltapp.presenter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.lifetrack.ltapp.model.data.dclass.*
import org.lifetrack.ltapp.model.data.LtMockData
import org.lifetrack.ltapp.model.data.dto.UserDataResponse
import org.lifetrack.ltapp.model.repository.UserRepository

class UserPresenter(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _profileInfo = MutableStateFlow(ProfileInfo())
    val profileInfo = _profileInfo.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    private val _allAppointments = MutableStateFlow(LtMockData.dummyAppointments)

    val nextUpcomingAppointment = _allAppointments.map { list ->
        list.filter { it.status == AppointmentStatus.UPCOMING }.minByOrNull { it.dateTime }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _selectedFilter = MutableStateFlow(AppointmentStatus.UPCOMING)
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
                appointments.filter { it.status == filter }
            }.collect { _userAppointments.value = it }
        }
    }

    fun loadUserProfile() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            when (val result = userRepository.getCurrentUserInfo()) {
                is AuthResult.SuccessWithData<*> -> {
                    val data = result.data as? UserDataResponse
                    data?.let { user ->
                        _profileInfo.value = ProfileInfo(
                            userName = user.userName,
                            userEmail = user.emailAddress,
                            userInitials = generateInitials(user.fullName ?: "N/A"),
                            userPhoneNumber = user.phoneNumber.toString(),
                            userFullName = user.fullName ?: "N/A"
                        )
                    }
                }
                is AuthResult.Error -> {
                    _errorMessage.value = result.message
                }
                else -> {}
            }
            _isLoading.value = false
        }
    }

    fun deleteAccount(navController: NavController) {
        viewModelScope.launch {
            _isLoading.value = true
            when (val result = userRepository.deleteAccount()) {
                is AuthResult.Success -> {
                    // Navigate to Login and clear backstack
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                }
                is AuthResult.Error -> {
                    _errorMessage.value = result.message
                }
                else -> {}
            }
            _isLoading.value = false
        }
    }

    fun onMenuItemAction(navController: NavController, route: String) {
        navController.navigate(route) { launchSingleTop = true }
    }

    fun onFilterChanged(newFilter: AppointmentStatus) {
        _selectedFilter.value = newFilter
    }

    fun getCountForStatus(status: AppointmentStatus): Int {
        return _allAppointments.value.count { it.status == status }
    }

    // --- Appointment Management ---

    fun bookAppointment() {
        val selectedDoc = _selectedDoctorProfile.value ?: return
        val newAppointment = Appointment(
            doctor = selectedDoc.name,
            dateTime = kotlinx.datetime.LocalDateTime(2025, 12, 29, 10, 0),
            hospital = selectedDoc.hospital,
            status = AppointmentStatus.RECENTLY_BOOKED
        )
        _allAppointments.update { it + newAppointment }
        onFilterChanged(AppointmentStatus.RECENTLY_BOOKED)
        _selectedDoctorProfile.value = null
    }

    fun dismissAppointment(appointment: Appointment) {
        _allAppointments.update { list ->
            list.map { if (it.id == appointment.id) it.copy(status = AppointmentStatus.DISMISSED) else it }
        }
    }

    fun undoDismiss(appointment: Appointment, originalStatus: AppointmentStatus) {
        _allAppointments.update { list ->
            list.map { if (it.id == appointment.id) it.copy(status = originalStatus) else it }
        }
    }

    fun restoreAppointment(appointment: Appointment) {
        _allAppointments.update { list ->
            list.map { if (it.id == appointment.id) it.copy(status = AppointmentStatus.UPCOMING) else it }
        }
    }

    fun onSelectDoctor(doctor: DoctorProfile) {
        _selectedDoctorProfile.value = doctor
    }

    fun clearError() {
        _errorMessage.value = null
    }
}