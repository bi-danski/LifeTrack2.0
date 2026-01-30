package org.lifetrack.ltapp.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Password
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import org.lifetrack.ltapp.core.events.AuthUiEvent
import org.lifetrack.ltapp.presenter.AuthPresenter
import org.lifetrack.ltapp.ui.navigation.LTNavDispatch
import org.lifetrack.ltapp.ui.state.UIState

@Composable
fun SignupScreen(authPresenter: AuthPresenter) {
    val signUpInfo by authPresenter.signupInfo.collectAsState()
    val uiState by authPresenter.uiState.collectAsStateWithLifecycle(UIState.Idle)

    val coroutineScope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }
    var passwordVisibility by remember { mutableStateOf(false) }
    val isVisible by remember { mutableStateOf(true) }

    LaunchedEffect(uiState) {
        if (uiState is UIState.Error) {
            snackBarHostState.showSnackbar((uiState as UIState.Error).msg)
            authPresenter.resetUIState()
        }
    }

    LaunchedEffect(Unit) {
        authPresenter.uiEvent.collect { uiEvent ->
            when(uiEvent) {
                is AuthUiEvent.SignupSuccess -> {
                    LTNavDispatch.navigate("login")
                }
                else -> {}
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState) {
                Snackbar(
                    snackbarData = it,
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    shape = MaterialTheme.shapes.medium
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedVisibility(visible = isVisible, enter = fadeIn(tween(600))) {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = "User Icon",
                    modifier = Modifier
                        .size(100.dp)
                        .padding(bottom = 16.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            AnimatedVisibility(visible = isVisible, enter = fadeIn(tween(800))) {
                Text(
                    text = "Let's Get You Started",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 24.dp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            AnimatedVisibility(visible = isVisible, enter = fadeIn(tween(1000))) {
                OutlinedTextField(
                    value = signUpInfo.fullName,
                    onValueChange = { authPresenter.onSignupInfoUpdate(signUpInfo.copy(fullName = it)) },
                    label = { Text("Full Name") },
                    leadingIcon = { Icon(Icons.Outlined.Person, contentDescription = null) },
                    enabled = uiState !is UIState.Loading,
                    singleLine = true,
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                )
            }

            AnimatedVisibility(visible = isVisible, enter = fadeIn(tween(1000))) {
                OutlinedTextField(
                    value = signUpInfo.userName,
                    onValueChange = { authPresenter.onSignupInfoUpdate(signUpInfo.copy(userName = it)) },
                    label = { Text("User Name") },
                    leadingIcon = { Icon(Icons.Outlined.Badge, contentDescription = null) },
                    enabled = uiState !is UIState.Loading,
                    singleLine = true,
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                )
            }

            AnimatedVisibility(visible = isVisible, enter = fadeIn(tween(1200))) {
                OutlinedTextField(
                    value = signUpInfo.emailAddress,
                    onValueChange = { authPresenter.onSignupInfoUpdate(signUpInfo.copy(emailAddress = it)) },
                    label = { Text("Email Address") },
                    leadingIcon = { Icon(Icons.Outlined.Email, contentDescription = null) },
                    enabled = uiState !is UIState.Loading,
                    singleLine = true,
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                )
            }

            AnimatedVisibility(visible = isVisible, enter = fadeIn(tween(1400))) {
                OutlinedTextField(
                    value = signUpInfo.phoneNumber,
                    onValueChange = { authPresenter.onSignupInfoUpdate(signUpInfo.copy(phoneNumber = it)) },
                    label = { Text("Phone Number") },
                    leadingIcon = { Icon(Icons.Outlined.Phone, contentDescription = null) },
                    enabled = uiState !is UIState.Loading,
                    singleLine = true,
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                )
            }

            AnimatedVisibility(visible = isVisible, enter = fadeIn(tween(1600))) {
                OutlinedTextField(
                    value = signUpInfo.password,
                    onValueChange = { authPresenter.onSignupInfoUpdate(signUpInfo.copy(password = it)) },
                    label = { Text("Password") },
                    leadingIcon = { Icon(Icons.Outlined.Password, contentDescription = null) },
                    enabled = uiState !is UIState.Loading,
                    visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                            Icon(
                                imageVector = if (passwordVisibility) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                contentDescription = "Toggle Password Visibility"
                            )
                        }
                    },
                    singleLine = true,
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            AnimatedVisibility(visible = isVisible, enter = fadeIn(tween(1800))) {
                Button(
                    onClick = {
                        if (signUpInfo.fullName.isNotEmpty() && signUpInfo.emailAddress.isNotEmpty() &&
                            signUpInfo.phoneNumber.isNotEmpty() && signUpInfo.password.isNotEmpty()
                        ) {
                            authPresenter.signUp()
                        } else {
                            coroutineScope.launch {
                                snackBarHostState.showSnackbar("All fields are required.")
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .clip(MaterialTheme.shapes.medium),
                    enabled = uiState !is UIState.Loading,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                    )
                ) {
                    if (uiState is UIState.Loading) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(22.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = "Sign Up",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }

            AnimatedVisibility(visible = isVisible, enter = fadeIn(tween(2000))) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Already have an account?",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    TextButton(
                        onClick = { LTNavDispatch.navigate("login") },
                        enabled = uiState !is UIState.Loading
                    ) {
                        Text(
                            text = "Sign In",
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