package org.lifetrack.ltapp.presenter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.lifetrack.ltapp.model.data.dclass.AuthResult
import org.lifetrack.ltapp.model.data.dclass.LoginInfo
import org.lifetrack.ltapp.model.data.dclass.SignUpInfo
import org.lifetrack.ltapp.model.repository.AuthRepository
import org.lifetrack.ltapp.model.repository.PreferenceRepository
import org.lifetrack.ltapp.ui.state.UIState

class AuthPresenter(
    private val authRepository: AuthRepository,
    prefRepository: PreferenceRepository,
): ViewModel() {

    val isLoggedIn = prefRepository.tokenPreferences
        .map { !it.accessToken.isNullOrBlank() }
        .distinctUntilChanged()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            null
        )

    private val _uiState = MutableStateFlow<UIState>(UIState.Idle)
    val uiState = _uiState.asStateFlow()

    private val _loginInfo = MutableStateFlow(LoginInfo())
    val loginInfo = _loginInfo.asStateFlow()

    private val _signupInfo = MutableStateFlow(SignUpInfo())
    val signupInfo = _signupInfo.asStateFlow()

    fun onLoginInfoUpdate(info: LoginInfo) { _loginInfo.value = info }
    fun onSignupInfoUpdate(info: SignUpInfo) { _signupInfo.value = info }

    fun login(navController: NavController) {
        viewModelScope.launch {
            _uiState.value = UIState.Loading
            when (val result = authRepository.login(_loginInfo.value)) {
                is AuthResult.Success, is AuthResult.SuccessWithData<*> -> {
                    _uiState.value = UIState.Success("Welcome back!")
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                }
                is AuthResult.Error -> _uiState.value = UIState.Error(result.message)
                else -> _uiState.value = UIState.Idle
            }
        }
    }

    fun signUp(navController: NavController) {
        viewModelScope.launch {
            _uiState.value = UIState.Loading
            when (val result = authRepository.signUp(_signupInfo.value)) {
                is AuthResult.Success -> {
                    _uiState.value = UIState.Success("Account created!")
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

            navController.navigate("login") {
                popUpTo(0) { inclusive = true }
            }
        }
    }
}