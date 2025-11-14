package org.lifetrack.ltapp.view.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Password
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.lifetrack.ltapp.model.repository.AuthRepositoryImpl
import org.lifetrack.ltapp.presenter.AuthPresenter
import org.lifetrack.ltapp.presenter.state.UIState
import org.lifetrack.ltapp.view.AuthView
import org.lifetrack.ltapp.view.ui.theme.LTAppTheme

@Composable
fun RegistrationScreen(
    navController: NavController,
    presenter: AuthPresenter,
    previewMode: Boolean = false
) {
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var telNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }

    var uiState by remember { mutableStateOf<UIState>(UIState.Idle) }
    var isVisible by remember { mutableStateOf(previewMode) }

    // Only animate when not in preview
    LaunchedEffect(Unit) {
        if (!previewMode) {
            delay(300)
            isVisible = true
        }
    }

    // Presenter Callbacks
    LaunchedEffect(presenter) {
        presenter.view = object : AuthView {
            override fun showLoading(isLoading: Boolean, msg: String?) {
                uiState = if (isLoading) UIState.Loading else UIState.Idle
            }

            override fun showError(msg: String) {
                coroutineScope.launch { snackbarHostState.showSnackbar(msg) }
            }

            override fun onAuthSuccess() {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(
                        message = "Signup successful! Redirecting to login...",
                        duration = SnackbarDuration.Short
                    )
                    delay(1500)
                    navController.navigate("login") {
                        popUpTo("signup") { inclusive = true }
                    }
                }
            }

            override fun onAuthSuccessWithData(data: String) {
                coroutineScope.launch { snackbarHostState.showSnackbar(data) }
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) {
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
                    modifier = Modifier.padding(bottom = 24.dp)
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            AnimatedVisibility(visible = isVisible, enter = fadeIn(tween(1000))) {
                OutlinedTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    label = { Text("Full Name") },
                    leadingIcon = { Icon(Icons.Outlined.Person, contentDescription = null) },
                    singleLine = true,
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                )
            }

            AnimatedVisibility(visible = isVisible, enter = fadeIn(tween(1200))) {
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    leadingIcon = { Icon(Icons.Outlined.Email, contentDescription = null) },
                    singleLine = true,
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                )
            }

            AnimatedVisibility(visible = isVisible, enter = fadeIn(tween(1400))) {
                OutlinedTextField(
                    value = telNumber,
                    onValueChange = { telNumber = it },
                    label = { Text("Phone Number") },
                    leadingIcon = { Icon(Icons.Outlined.Phone, contentDescription = null) },
                    singleLine = true,
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                )
            }

            AnimatedVisibility(visible = isVisible, enter = fadeIn(tween(1600))) {
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    leadingIcon = { Icon(Icons.Outlined.Password, contentDescription = null) },
                    visualTransformation = if (passwordVisibility)
                        VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                            Icon(
                                imageVector = if (passwordVisibility)
                                    Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
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
                        if (fullName.isNotEmpty() && email.isNotEmpty() &&
                            telNumber.isNotEmpty() && password.isNotEmpty()
                        ) {
                            coroutineScope.launch {
                                presenter.signUp(email, password, telNumber, fullName)
                            }
                        } else {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("All fields are required.")
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .clip(MaterialTheme.shapes.medium),
                    enabled = uiState != UIState.Loading,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                    )
                ) {
                    if (uiState == UIState.Loading) {
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
                    TextButton(onClick = { navController.navigate("login") }) {
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

//val mockPresenter = AuthPresenter(null, AuthRepositoryImpl())
//
//@RequiresApi(Build.VERSION_CODES.S)
//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun PreviewRegistrationScreen() {
//    LTAppTheme {
//        RegistrationScreen(rememberNavController(), mockPresenter, previewMode = true)
//    }
//}
