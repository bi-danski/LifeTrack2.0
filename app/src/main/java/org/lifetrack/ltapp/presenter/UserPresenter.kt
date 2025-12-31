package org.lifetrack.ltapp.presenter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.lifetrack.ltapp.core.utils.toUserProfileInformation
import org.lifetrack.ltapp.model.data.LtMockData
import org.lifetrack.ltapp.model.data.dclass.*
import org.lifetrack.ltapp.model.data.dto.UserDataResponse
import org.lifetrack.ltapp.model.repository.UserRepository


class UserPresenter(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _sessionState = MutableStateFlow(SessionStatus.INITIALIZING)
    val sessionState = _sessionState.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    private val _profileInfo = MutableStateFlow(ProfileInfo())
    val profileInfo = _profileInfo.asStateFlow()

    private val _allAppointments = MutableStateFlow(LtMockData.dummyAppointments)
    private val _selectedFilter = MutableStateFlow(AppointmentStatus.UPCOMING)
    val selectedFilter = _selectedFilter.asStateFlow()

    val nextUpcomingAppointment = _allAppointments.map { list ->
        list.filter { it.status == AppointmentStatus.UPCOMING }
            .minByOrNull { it.dateTime }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val userAppointments: StateFlow<List<Appointment>> = combine(
        _allAppointments,
        _selectedFilter
    ) { appointments, filter ->
        appointments.filter { it.status == filter }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedDoctorProfile = MutableStateFlow<DoctorProfile?>(null)
    val selectedDoctorProfile = _selectedDoctorProfile.asStateFlow()

    init {
        loadUserProfile()
    }

    fun loadUserProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                when (val result = userRepository.getCurrentUserInfo()) {
                    is AuthResult.SuccessWithData<*> -> {
                        val data = result.data as? UserDataResponse
                        data?.let { user ->
                            _profileInfo.value = user.toUserProfileInformation()
                        }
                        _sessionState.value = SessionStatus.LOGGED_IN
                    }
                    is AuthResult.Error -> {
                        _errorMessage.value = result.message
                        _sessionState.value = SessionStatus.LOGGED_OUT
                    }
                    else -> {
                        _sessionState.value = SessionStatus.LOGGED_OUT
                    }
                }
            } catch (e: Exception) {
                _errorMessage.value = e.localizedMessage ?: "Failed to load session"
                _sessionState.value = SessionStatus.LOGGED_OUT
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteAccount(navController: NavController) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            try {
                when (val result = userRepository.deleteAccount()) {
                    is AuthResult.Success -> {
                        _sessionState.value = SessionStatus.LOGGED_OUT
                        launch(Dispatchers.Main) {
                            navController.navigate("login") {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    }
                    is AuthResult.Error -> {
                        _errorMessage.value = result.message
                    }
                    else -> {}
                }
            } finally {
                _isLoading.value = false
            }
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

    fun onSelectDoctor(doctor: DoctorProfile) {
        _selectedDoctorProfile.value = doctor
    }

    fun clearError() {
        _errorMessage.value = null
    }

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
}