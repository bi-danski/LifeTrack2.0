package org.lifetrack.ltapp.presenter

import android.annotation.SuppressLint
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.lifetrack.ltapp.model.LtMockData
import org.lifetrack.ltapp.model.data.dclass.Appointment
import org.lifetrack.ltapp.model.data.dclass.AuthResult
import org.lifetrack.ltapp.model.data.dclass.DoctorProfile
import org.lifetrack.ltapp.model.data.dclass.Prescription
import org.lifetrack.ltapp.model.data.dclass.UIAppointmentStatus
import org.lifetrack.ltapp.model.repository.UserRepository
import org.lifetrack.ltapp.service.SessionManager
import org.lifetrack.ltapp.ui.navigation.LTNavDispatch
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class UserPresenter(
    private val userRepository: UserRepository,
    private val sessionManager: SessionManager
) : ViewModel() {
    @SuppressLint("MutableCollectionMutableState")
//    val dummyBpData = mutableStateOf(LtMockData.bPressureData)
//    val dummyPatient = mutableStateOf(LtMockData.dPatient)
//    val dummyLabTests =  mutableStateListOf<LabTest>().apply {
//        addAll(LtMockData.dLabTests)
//    }
    val dummyPrescriptions = mutableStateListOf<Prescription>().apply {
        addAll(LtMockData.dPrescriptions)
    }
    var isBookingExpanded = mutableStateOf(false)

    private val _allAppointments = MutableStateFlow(LtMockData.dummyAppointments)
    private val _selectedFilter = MutableStateFlow(UIAppointmentStatus.UPCOMING)
    val selectedFilter = _selectedFilter.asStateFlow()

    private val _selectedDoctorProfile = MutableStateFlow<DoctorProfile?>(null)
    val selectedDoctorProfile = _selectedDoctorProfile.asStateFlow()

    val nextUpcomingAppointment = _allAppointments.map { list ->
        list.filter { it.status == UIAppointmentStatus.UPCOMING }.minByOrNull { it.scheduledAt }
    }.stateIn(viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        null
    )
    val userAppointments: StateFlow<List<Appointment>> = combine(_allAppointments, _selectedFilter)
    { appointments, filter ->
        appointments.filter { it.status == filter }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    private val _isLoading = MutableStateFlow(false)
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    fun onFilterChanged(newFilter: UIAppointmentStatus) {
        _selectedFilter.value = newFilter
    }
    fun onSelectDoctor(doctor: DoctorProfile) {
        _selectedDoctorProfile.value = doctor
    }
    fun clearError() {
        _errorMessage.value = null
    }
    fun getCountForStatus(status: UIAppointmentStatus): Int {
        return _allAppointments.value.count { it.status == status }
    }

//    fun deleteAccount() {
//        viewModelScope.launch(Dispatchers.IO) {
//            _isLoading.value = true
//            try {
//                when (val result = userRepository.deleteAccount()) {
//                    is AuthResult.Success -> {
//                        sessionManager.logout()
//                        launch(Dispatchers.Main) {
//                            LTNavDispatch.navigate("signup")
//                            }
//                    }
//                    is AuthResult.Error -> {
//                        _errorMessage.value = result.message
//                    }
//                    else -> {}
//                }
//            } finally {
//                _isLoading.value = false
//            }
//        }
//    }

    @OptIn(ExperimentalUuidApi::class)
    fun bookAppointment() {
        val selectedDoc = _selectedDoctorProfile.value ?: return
        val newAppointment = Appointment(
            doctor = selectedDoc.name,
            scheduledAt = kotlinx.datetime.LocalDateTime(2025, 12, 29, 10, 0),
            hospital = selectedDoc.hospital,
            status = UIAppointmentStatus.RECENTLY_BOOKED,
            id = Uuid.random().toHexString()
        )
        _allAppointments.update { it + newAppointment }
        onFilterChanged(UIAppointmentStatus.RECENTLY_BOOKED)
        _selectedDoctorProfile.value = null
    }

    fun dismissAppointment(appointment: Appointment) {
        _allAppointments.update { list ->
            list.map { if (it.id == appointment.id) it.copy(status = UIAppointmentStatus.DISMISSED) else it }
        }
    }

    fun undoDismiss(appointment: Appointment, originalStatus: UIAppointmentStatus) {
        _allAppointments.update { list ->
            list.map { if (it.id == appointment.id) it.copy(status = originalStatus) else it }
        }
    }

    fun restoreAppointment(appointment: Appointment) {
        _allAppointments.update { list ->
            list.map { if (it.id == appointment.id) it.copy(status = UIAppointmentStatus.UPCOMING) else it }
        }
    }

    fun updateUserProfile(
        fullName: String,
        userName: String,
        phoneNumber: String,
        onComplete: () -> Unit = {}
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _isLoading.value = true
                val updateRequest = org.lifetrack.ltapp.model.data.dto.UserDataUpdate(
                    fullName = fullName,
                    userName = userName,
                    phoneNumber = phoneNumber.toLongOrNull(),
                    emailAddress = null,
                    updatedAt = null
                )
                when (val result = userRepository.updateCurrentUserInfo(updateRequest)) {
                    is AuthResult.Success -> {
                        _errorMessage.value = "Profile updated successfully!"
                        onComplete()
                    }
                    is AuthResult.Error -> {
                        _errorMessage.value = result.message
                    }
                    else -> {
                        _errorMessage.value = "Update failed"
                    }
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteUserAccount() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _isLoading.value = true
                when (val result = userRepository.deleteAccount()) {
                    is AuthResult.Success -> {
                        _errorMessage.value = "Account deleted successfully"
                        // Navigate to login after deletion
                        LTNavDispatch.navigate("login", clearBackstack = true)
                    }
                    is AuthResult.Error -> {
                        _errorMessage.value = result.message
                    }
                    else -> {
                        _errorMessage.value = "Account deletion failed"
                    }
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}