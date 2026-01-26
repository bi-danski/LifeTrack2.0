package org.lifetrack.ltapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import org.lifetrack.ltapp.presenter.AuthPresenter
import org.lifetrack.ltapp.presenter.SharedPresenter
import org.lifetrack.ltapp.ui.components.loginscreen.LTBrandAppBar
import org.lifetrack.ltapp.ui.navigation.LTNavDispatcher
import org.lifetrack.ltapp.ui.state.UIState

@Composable
fun LoginScreen(
    authPresenter: AuthPresenter,
    sharedPresenter: SharedPresenter,
) {
    val loginInfo by authPresenter.loginInfo.collectAsState()
    val authUiState by authPresenter.uiState.collectAsStateWithLifecycle(UIState.Idle)

    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var passwordVisibility by remember { mutableStateOf(false) }

    LaunchedEffect(authUiState) {
        when (authUiState) {
            is UIState.Error -> {
                snackbarHostState.showSnackbar(message = (authUiState as UIState.Error).msg,
                    duration = SnackbarDuration.Long
                )
                authPresenter.resetUIState()
            }
            else -> {}
        }
    }
//    LaunchedEffect(Unit) {
//        authPresenter.uiEvent.collect { uiEvent ->
//            when(uiEvent) {
//                is AuthUiEvent.LoginSuccess -> {
//                    authPresenter.loadUserProfile()
//                }
//                else -> {}
//            }
//        }
//    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            item {
                LTBrandAppBar(
                    modifier = Modifier.padding(top = 32.dp),
                    sharedPresenter = sharedPresenter
                )
                Spacer(modifier = Modifier.height(40.dp))
            }

            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedTextField(
                        value = loginInfo.emailAddress,
                        onValueChange = { authPresenter.onLoginInfoUpdate(loginInfo.copy(emailAddress = it)) },
                        label = { Text("Email") },
                        enabled = authUiState !is UIState.Loading,
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth(0.85f)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    OutlinedTextField(
                        value = loginInfo.password,
                        onValueChange = { authPresenter.onLoginInfoUpdate(loginInfo.copy(password = it)) },
                        label = { Text("Password") },
                        enabled = authUiState !is UIState.Loading,
                        visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            val icon = if (passwordVisibility) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                            IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                                Icon(imageVector = icon, contentDescription = "Toggle Password Visibility")
                            }
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth(0.85f)
                    )

                    Spacer(modifier = Modifier.height(48.dp))

                    Button(
                        onClick = {
                            if (loginInfo.emailAddress.isNotEmpty() && loginInfo.password.isNotEmpty()) {
                                authPresenter.login()
                            } else {
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Please fill in all fields.")
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .height(50.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        enabled = authUiState !is UIState.Loading
                    ) {
                        if (authUiState is UIState.Loading) {
                            CircularProgressIndicator(
                                color = Color.Green, //  MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.5.dp
                            )
                        } else {
                            Text(
                                text = "Login",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    TextButton(
                        onClick = { LTNavDispatcher.navigate("restore") },
                        enabled = authUiState !is UIState.Loading
                    ) {
                        Text(
                            text = "Forgot Password?",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Don’t have an account?",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    TextButton(
                        onClick = { LTNavDispatcher.navigate("signup") },
                        enabled = authUiState !is UIState.Loading
                    ) {
                        Text(
                            text = "Sign Up",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}