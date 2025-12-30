package org.lifetrack.ltapp.presenter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.lifetrack.ltapp.model.data.dclass.AuthResult
import org.lifetrack.ltapp.model.data.dclass.LoginInfo
import org.lifetrack.ltapp.model.data.dclass.SignUpInfo
import org.lifetrack.ltapp.model.data.dclass.TokenPreferences
import org.lifetrack.ltapp.model.repository.AuthRepository
import org.lifetrack.ltapp.model.repository.PreferenceRepository
import org.lifetrack.ltapp.ui.state.UIState

class AuthPresenter(
    private val authRepository: AuthRepository,
    prefRepository: PreferenceRepository
): ViewModel() {

    val sessionState = prefRepository.tokenPreferences
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            TokenPreferences()
        )

    private val _uiState = MutableStateFlow<UIState>(UIState.Idle)
    val uiState = _uiState.asStateFlow()

    private val _loginInfo = MutableStateFlow(LoginInfo())
    val loginInfo = _loginInfo.asStateFlow()

    private val _signupInfo = MutableStateFlow(SignUpInfo())
    val signupInfo = _signupInfo.asStateFlow()


    fun onLoginInfoUpdate(loginInfo: LoginInfo) {
        _loginInfo.value = loginInfo
    }

    fun onSignupInfoUpdate(signupInfo: SignUpInfo) {
        _signupInfo.value = signupInfo
    }

    fun login(navController: NavController) {
        viewModelScope.launch {
            _uiState.value = UIState.Loading

            when (val result = authRepository.login(_loginInfo.value)) {
                is AuthResult.Success -> {
                    _uiState.value = UIState.Success("Welcome back!")
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                }
                is AuthResult.SuccessWithData<*> -> {
                    _uiState.value = UIState.Success("Great to see you again")
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                }
                is AuthResult.Error -> {
                    _uiState.value = UIState.Error(result.message)
                }
                else -> { _uiState.value = UIState.Idle }
            }
        }
    }

    fun signUp(navController: NavController) {
        viewModelScope.launch {
            _uiState.value = UIState.Loading

            when (val result = authRepository.signUp(_signupInfo.value)) {
                is AuthResult.Success -> {
                    _uiState.value = UIState.Success("Account created successfully!")
                    delay(2000)
                    navController.navigate("login") {
                        popUpTo("signup") { inclusive = true }
                    }
                    _uiState.value = UIState.Success("You can now login with your credentials")
//                    login(navController)
                }
                is AuthResult.Error -> {
                    _uiState.value = UIState.Error(result.message)
                }
                else -> _uiState.value = UIState.Idle
            }
        }
    }

    fun logout(navController: NavController) {
        viewModelScope.launch {
            authRepository.logout()
            _uiState.value = UIState.Idle
            _loginInfo.value = LoginInfo()
            _signupInfo.value = SignUpInfo()
            navController.navigate("login") {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    fun isAuthenticated(): Boolean {
        val token = sessionState.value.accessToken
        return !token.isNullOrBlank()
    }
}