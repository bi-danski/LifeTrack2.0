package org.lifetrack.ltapp.presenter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.lifetrack.ltapp.core.events.AuthUiEvent
import org.lifetrack.ltapp.model.data.dclass.AuthResult
import org.lifetrack.ltapp.model.data.dclass.LoginInfo
import org.lifetrack.ltapp.model.data.dclass.ProfileInfo
import org.lifetrack.ltapp.model.data.dclass.SignUpInfo
import org.lifetrack.ltapp.model.data.dto.SessionInfo
import org.lifetrack.ltapp.model.data.dto.UserDataResponse
import org.lifetrack.ltapp.model.repository.AuthRepository
import org.lifetrack.ltapp.model.repository.PreferenceRepository
import org.lifetrack.ltapp.model.repository.UserRepository
import org.lifetrack.ltapp.service.SessionManager
import org.lifetrack.ltapp.ui.navigation.LTNavDispatch
import org.lifetrack.ltapp.ui.state.UIState
import org.lifetrack.ltapp.utility.toProfileInfo
import org.lifetrack.ltapp.utility.toUserPreferences
import org.lifetrack.ltapp.utility.toUserProfileInfo

class AuthPresenter(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val sessionManager: SessionManager,
    private val prefRepository: PreferenceRepository
): ViewModel() {

    val profileInfo = prefRepository.userPreferences
        .map { it.toProfileInfo() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = ProfileInfo()
        )

    private val _uiState = MutableStateFlow<UIState>(UIState.Idle)
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = Channel<AuthUiEvent>(Channel.BUFFERED)
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _loginInfo = MutableStateFlow(LoginInfo())
    val loginInfo = _loginInfo.asStateFlow()

    private val _signupInfo = MutableStateFlow(SignUpInfo())
    val signupInfo = _signupInfo.asStateFlow()

    fun onLoginInfoUpdate(info: LoginInfo) {
        _loginInfo.value = info
    }
    fun onSignupInfoUpdate(info: SignUpInfo) {
        _signupInfo.value = info
    }
    fun resetUIState() {
        viewModelScope.launch {
            _uiState.emit(UIState.Idle)
        }
    }

    fun signUp() {
        viewModelScope.launch {
            _uiState.emit(UIState.Loading)
            when (val result = authRepository.signUp(_signupInfo.value)) {
                is AuthResult.Success -> {
                    _uiEvent.send(AuthUiEvent.SignupSuccess)
                }
//                is AuthResult.SuccessWithData<*> -> {
//                    _uiState.emit(UIState.Success(result.data as? String))
//                    _uiEvent.send(AuthUiEvent.SignupSuccess)
//                }
                is AuthResult.Error -> _uiState.emit(
                    UIState.Error(
                        result.message,
                        isNetworkError = result.isNetworkError
                    )
                )
                else -> _uiState.emit(UIState.Idle)
            }
        }
    }

    fun login() {
        viewModelScope.launch {
            _uiState.emit(UIState.Loading)
            when (val result = authRepository.login(_loginInfo.value)) {
                is AuthResult.SuccessWithData<*> -> {
                    sessionManager.updateSessionPreferences(result.data as SessionInfo)
                    _uiState.emit(UIState.Success("Welcome back!"))
                }
                is AuthResult.Error -> {
                    _uiState.emit(UIState.Error(result.message, result.isNetworkError))
                }
                else -> _uiState.emit(UIState.Idle)
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            _uiState.emit(UIState.Loading)
            sessionManager.logout()
            _uiState.emit(UIState.Success("Logout Success"))
            LTNavDispatch.navigate("login", clearBackstack = true)
        }
    }

    fun loadUserProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            if (prefRepository.userPreferences.first().isDefault) {
                _uiState.emit(UIState.Loading)
                when (val result = userRepository.getCurrentUserInfo()) {
                    is AuthResult.SuccessWithData<*> -> {
                        val data = result.data as? UserDataResponse
                        data?.let { user ->
                            prefRepository.updateUserPreferences {
                                user.toUserProfileInfo().toUserPreferences()
                            }
                        }
                        _uiState.emit(UIState.Idle)
                    }
                    is AuthResult.Error -> {
                        _uiState.emit(UIState.Error(result.message, result.isNetworkError))
                    }
                    else -> _uiState.emit(UIState.Idle)
                }
            }
        }
    }
}