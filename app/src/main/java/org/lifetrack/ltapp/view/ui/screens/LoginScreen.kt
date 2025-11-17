package org.lifetrack.ltapp.view.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import org.lifetrack.ltapp.model.repository.AuthRepositoryImpl
import org.lifetrack.ltapp.presenter.AuthPresenter
import org.lifetrack.ltapp.view.ui.state.UIState
import org.lifetrack.ltapp.view.AuthView
import org.lifetrack.ltapp.view.components.loginscreen.LTBrandAppBar
import org.lifetrack.ltapp.view.ui.theme.LTAppTheme

@Composable
fun LoginScreen(navController: NavController, presenter: AuthPresenter) {
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val userRole = remember { mutableStateOf("") }
    var emailAddress by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }
    var uiState by remember { mutableStateOf<UIState>(UIState.Idle) }

    LaunchedEffect(presenter) {
        presenter.view = object : AuthView {
            override fun showLoading(isLoading: Boolean, msg: String?) {
                uiState = if (isLoading) UIState.Loading else UIState.Idle
            }

            override fun showError(msg: String) {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(msg)
                }
            }

            override fun onAuthSuccess() {
                uiState = UIState.Success
            }

            override fun onAuthSuccessWithData(data: String) {
                userRole.value = data
                coroutineScope.launch {
                    when(data){
                        "Kiongos" -> {
                            navController.navigate("kiongozi") {
                                popUpTo("login") { inclusive = true }
                            }
                        }
                        "Patients" -> {
                            navController.navigate("home") {
                                popUpTo("login") { inclusive = true }
                            }
                        }
                        "Practitioners" -> {
                            navController.navigate("expert") {
                                popUpTo("login") { inclusive = true }
                            }
                        }
                        else -> {
                            snackbarHostState.showSnackbar("Unknown role: $data")
                        }
                    }
                }
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("Logging you in...")
                }
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) {
                Snackbar(
                    snackbarData = it,
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            LTBrandAppBar(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 32.dp),

                )
            Spacer(modifier = Modifier.height(50.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.7f)
                    .wrapContentHeight()
                    .align(Alignment.BottomCenter),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = emailAddress,
                    onValueChange = { emailAddress = it },
                    label = { Text("Email") },
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth(0.85f) // 85% width
                )
                Spacer(modifier = Modifier.height(24.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val icon =
                            if (passwordVisibility) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                        IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                            Icon(imageVector = icon, contentDescription = "Toggle Password Visibility")
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth(0.85f) // 85% width
                )
                Spacer(modifier = Modifier.height(48.dp))

                Button(
                    onClick = {
                        if (emailAddress.isNotEmpty() && password.isNotEmpty()) {
                            coroutineScope.launch {
                                presenter.login(emailAddress, password)
                            }
                        } else {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Please fill in all fields.")
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.85f) // 85% width
                        .height(50.dp)
                        .clip(RoundedCornerShape(12.dp)),

                    enabled = uiState != UIState.Loading
                ) {
                    if (uiState == UIState.Loading) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(text = "Login", style = MaterialTheme.typography.labelLarge)
                    }
                }
//                Spacer(modifier = Modifier.height(24.dp))
                TextButton(onClick = { navController.navigate("reset") }) {
                    Text(
                        text = "Forgot Password?",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp, top = 24.dp)
                    .align(Alignment.BottomCenter),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Don’t have an account?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.width(4.dp))
                TextButton(onClick = { navController.navigate("signup") }) {
                    Text(
                        text = "Sign Up",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}
val previewPresenter = AuthPresenter(authRepository = AuthRepositoryImpl(), view  = null)

@RequiresApi(Build.VERSION_CODES.S)
@Preview
@Composable
fun PreviewLoginScreen(){
    val navController = NavController(LocalContext.current)
    LTAppTheme {
        LoginScreen(navController, previewPresenter)
    }

}