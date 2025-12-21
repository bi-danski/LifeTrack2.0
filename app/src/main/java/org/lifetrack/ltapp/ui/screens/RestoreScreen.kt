package org.lifetrack.ltapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleLeft
import androidx.compose.material.icons.filled.LockReset
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import org.lifetrack.ltapp.ui.components.homescreen.LifeTrackTopBar
import org.lifetrack.ltapp.ui.theme.Purple80

@Composable
fun RestoreScreen(
    navController: NavController,
//    userRepository: UserRepositoryImpl
) {
    var email by remember { mutableStateOf(TextFieldValue("")) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var resetSuccess by remember { mutableStateOf(false) }
    val snackBarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            LifeTrackTopBar(
                "Reset Password",
                navigationIcon = Icons.Default.ArrowCircleLeft,
                backCallback = { navController.popBackStack() },
                actionCallback = {}
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon (
                    imageVector = Icons.Filled.LockReset,
                    contentDescription = "Password Reset",
                    modifier = Modifier
                        .size(60.dp),
                    tint = Purple80
                )
                Spacer(modifier = Modifier.height(40.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Your Email Address ...", fontStyle = FontStyle.Italic) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(40.dp))

                Button(
                    onClick = {
                        isLoading = true
                        errorMessage = null
                        coroutineScope.launch {
//                            val result = userRepository.sendPasswordReset(email.text)
//                            val result = AuthResult.SuccessWithData("Restore Screen Test")
                            isLoading = false
//                            when (result) {
//                                is AuthResult.Success -> {
//                                    resetSuccess = true
//                                    snackbarHostState.showSnackbar("Password reset link sent to your email.")
//                                }
////                                is AuthResult.Failure -> {
////                                    resetSuccess = false
////                                    errorMessage = result.message
////                                    snackbarHostState.showSnackbar("Error: ${result.message}")
////                                }
//                                else -> {}
//                            }
                        }
                    },
                    enabled = !isLoading && email.text.isNotEmpty(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(text = if (isLoading) "Sending..." else "Send Reset Link")
                }

                if (errorMessage != null) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = errorMessage!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelLarge
                    )
                }

                if (resetSuccess && !isLoading) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Check your email to reset your password.",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.labelLarge
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

