package org.lifetrack.ltapp.presenter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.lifetrack.ltapp.core.utils.toProfileInfo
import org.lifetrack.ltapp.core.utils.toUserPreferences
import org.lifetrack.ltapp.core.utils.toUserProfileInformation
import org.lifetrack.ltapp.model.data.dclass.AuthResult
import org.lifetrack.ltapp.model.data.dclass.LoginInfo
import org.lifetrack.ltapp.model.data.dclass.ProfileInfo
import org.lifetrack.ltapp.model.data.dclass.SessionStatus
import org.lifetrack.ltapp.model.data.dclass.SignUpInfo
import org.lifetrack.ltapp.model.data.dclass.TokenPreferences
import org.lifetrack.ltapp.model.data.dclass.UserPreferences
import org.lifetrack.ltapp.model.data.dto.UserDataResponse
import org.lifetrack.ltapp.model.repository.AuthRepository
import org.lifetrack.ltapp.model.repository.PreferenceRepository
import org.lifetrack.ltapp.model.repository.UserRepository
import org.lifetrack.ltapp.ui.state.UIState


class AuthPresenter(
    private val authRepository: AuthRepository,
    private val prefRepository: PreferenceRepository,
    private val userRepository: UserRepository
): ViewModel() {

    val isLoggedIn = prefRepository.tokenPreferences
        .map { !it.accessToken.isNullOrBlank() }
        .distinctUntilChanged()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            null
        )

    val profileInfo = prefRepository.userPreferences
        .map { it.toProfileInfo() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = ProfileInfo()
        )

    private val _uiState = MutableStateFlow<UIState>(UIState.Idle)
    val uiState = _uiState.asStateFlow()

    private val _loginInfo = MutableStateFlow(LoginInfo())
    val loginInfo = _loginInfo.asStateFlow()



    private val _signupInfo = MutableStateFlow(SignUpInfo())
    val signupInfo = _signupInfo.asStateFlow()

    private val _sessionState = MutableStateFlow(SessionStatus.INITIALIZING)
    val sessionState = _sessionState.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    fun onLoginInfoUpdate(info: LoginInfo) { _loginInfo.value = info }
    fun onSignupInfoUpdate(info: SignUpInfo) { _signupInfo.value = info }

    fun login(navController: NavController) {
        viewModelScope.launch {
            _uiState.value = UIState.Loading
            when (val result = authRepository.login(_loginInfo.value)) {
                 is AuthResult.SuccessWithData<*> -> {
                    _uiState.value = UIState.Success("Welcome back!")
                    _sessionState.value = SessionStatus.LOGGED_IN
                    val sessionTokens = result.data as TokenPreferences
                    prefRepository.updateTokens(sessionTokens.accessToken, sessionTokens.refreshToken)
                     loadUserProfile()
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                    navController.clearBackStack("home")
                }
                is AuthResult.Error -> _uiState.value = UIState.Error(result.message)
                is AuthResult.Success -> _uiState.value = UIState.Success()
                else -> _uiState.value = UIState.Loading
            }
        }
    }

    fun signUp(navController: NavController) {
        viewModelScope.launch {
            _uiState.value = UIState.Loading
            when (val result = authRepository.signUp(_signupInfo.value)) {
                is AuthResult.Success -> {
                    _uiState.value = UIState.Success("Account created successfully!")
                    delay(1500)
                    navController.navigate("login") { popUpTo("signup") { inclusive = true } }
                }
                is AuthResult.Error -> _uiState.value = UIState.Error(result.message)
                else -> _uiState.value = UIState.Idle
            }
        }
    }

    fun logout(navController: NavController) {
        viewModelScope.launch {
            kotlinx.coroutines.withContext(kotlinx.coroutines.NonCancellable) {
                authRepository.logout()
            }
            _uiState.value = UIState.Idle
            _sessionState.value = SessionStatus.LOGGED_OUT
            navController.navigate("login") {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    fun loadUserProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            val currentPrefs = prefRepository.userPreferences.first()

            if (currentPrefs.isDefault) {
                _isLoading.value = true
                _errorMessage.value = null
                try {
                    when (val result = userRepository.getCurrentUserInfo()) {
                        is AuthResult.SuccessWithData<*> -> {
                            val data = result.data as? UserDataResponse
                            data?.let { user ->
                                prefRepository.updateUserPreferences(
                                    {
                                        user
                                            .toUserProfileInformation()
                                            .toUserPreferences()
                                    }
                                )
                            }
                        }
                        is AuthResult.Error -> { _errorMessage.value = result.message }
                        else -> { }
                    }
                } catch (e: Exception) {
                    _errorMessage.value = e.message ?: "Failed to retrieve user profile information"
                } finally {
                    _isLoading.value = false
                }
            }
        }
    }
}