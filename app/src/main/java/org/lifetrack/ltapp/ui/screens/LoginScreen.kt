package org.lifetrack.ltapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import org.lifetrack.ltapp.presenter.AuthPresenter
import org.lifetrack.ltapp.presenter.SharedPresenter
import org.lifetrack.ltapp.ui.components.loginscreen.LTBrandAppBar
import org.lifetrack.ltapp.ui.state.UIState

@Composable
fun LoginScreen(
    navController: NavController,
    authPresenter: AuthPresenter,
    sharedPresenter: SharedPresenter,
) {
    val loginInfo by authPresenter.loginInfo.collectAsState()
    val authUiState by authPresenter.uiState.collectAsState()

    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var passwordVisibility by remember { mutableStateOf(false) }

    LaunchedEffect(authUiState) {
        when (authUiState) {
            is UIState.Success -> {
                val msg = (authUiState as UIState.Success).message
                if (!msg.isNullOrBlank()) {
                    snackbarHostState.showSnackbar(msg)
                    authPresenter.resetUIState()
                }
            }

            is UIState.Error -> {
                val errorMsg = (authUiState as UIState.Error).msg
                snackbarHostState.showSnackbar(
                    message = errorMsg,
                    duration = SnackbarDuration.Long
                )
                authPresenter.resetUIState()
            }

            is UIState.Loading -> {
                snackbarHostState.showSnackbar("Loading ...", duration = SnackbarDuration.Short)
                authPresenter.resetUIState()
            }

            else -> {}
        }
    }

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
                                authPresenter.login(navController)
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
                        onClick = { navController.navigate("restore") },
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
                        onClick = { navController.navigate("signup") },
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