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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.lifetrack.ltapp.model.data.dclass.LoginInfo
import org.lifetrack.ltapp.presenter.AuthPresenter
import org.lifetrack.ltapp.presenter.SharedPresenter
import org.lifetrack.ltapp.ui.state.UIState
import org.lifetrack.ltapp.ui.components.loginscreen.LTBrandAppBar

//@Composable
//fun LoginScreen(
//    navController: NavController,
//    presenter: AuthPresenter,
//    sharedPresenter: SharedPresenter
//    ) {
//    val loginInfo = presenter.loginInfo.collectAsState()
//    val coroutineScope = rememberCoroutineScope()
//    val snackBarHostState = remember { SnackbarHostState() }
//    var passwordVisibility by remember { mutableStateOf(false) }
//    var uiState by remember { mutableStateOf<UIState>(UIState.Idle) } // next ni wewe
//
//    Scaffold(
//        snackbarHost = {
//            SnackbarHost(hostState = snackBarHostState) {
//                Snackbar(
//                    snackbarData = it,
//                    containerColor = MaterialTheme.colorScheme.errorContainer,
//                    contentColor = MaterialTheme.colorScheme.onErrorContainer
//                )
//            }
//        }
//    ) { paddingValues ->
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(paddingValues)
//                .background(MaterialTheme.colorScheme.background),
//            contentAlignment = Alignment.Center
//        ) {
//            LTBrandAppBar(
//                modifier = Modifier
//                    .align(Alignment.TopCenter)
//                    .padding(top = 32.dp),
//                sharedPresenter
//            )
//            Spacer(modifier = Modifier.height(50.dp))
//
//            Column(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .fillMaxHeight(0.7f)
//                    .wrapContentHeight()
//                    .align(Alignment.BottomCenter),
//                verticalArrangement = Arrangement.Center,
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                OutlinedTextField(
//                    value = loginInfo.value.emailAddress,
//                    onValueChange = { presenter.onLoginInfoUpdate(LoginInfo(emailAddress = it)) },
//                    label = { Text("Email") },
//                    singleLine = true,
//                    shape = RoundedCornerShape(8.dp),
//                    modifier = Modifier.fillMaxWidth(0.85f) // 85% width
//                )
//                Spacer(modifier = Modifier.height(24.dp))
//
//                OutlinedTextField(
//                    value = loginInfo.value.password,
//                    onValueChange = { presenter.onLoginInfoUpdate(LoginInfo(password = it)) },
//                    label = { Text("Password") },
//                    visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
//                    trailingIcon = {
//                        val icon =
//                            if (passwordVisibility) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
//                        IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
//                            Icon(imageVector = icon, contentDescription = "Toggle Password Visibility")
//                        }
//                    },
//                    singleLine = true,
//                    shape = RoundedCornerShape(8.dp),
//                    modifier = Modifier.fillMaxWidth(0.85f) // 85% width
//                )
//                Spacer(modifier = Modifier.height(48.dp))
//
//                Button(
//                    onClick = {
//                        if (loginInfo.value.password.isNotEmpty() && loginInfo.value.password.isNotEmpty()) {
//                            coroutineScope.launch {
//                                presenter.login(loginInfo.value.emailAddress, loginInfo.value.password, navController)
//                            }
//                        } else {
//                            coroutineScope.launch {
//                                snackBarHostState.showSnackbar("Please fill in all fields.")
//                            }
//                        }
//                    },
//                    modifier = Modifier
//                        .fillMaxWidth(0.85f) // 85% width
//                        .height(50.dp)
//                        .clip(RoundedCornerShape(12.dp)),
//
//                    enabled = uiState != UIState.Loading
//                ) {
//                    if (uiState == UIState.Loading) {
//                        CircularProgressIndicator(
//                            color = MaterialTheme.colorScheme.onPrimary,
//                            modifier = Modifier.size(20.dp),
//                            strokeWidth = 2.dp
//                        )
//                    } else {
//                        Text(
//                            text = "Login",
//                            style = MaterialTheme.typography.labelLarge,
//                            fontWeight = FontWeight.SemiBold
//                        )
//                    }
//                }
////                Spacer(modifier = Modifier.height(24.dp))
//                TextButton(onClick = { navController.navigate("restore") }) {
//                    Text(
//                        text = "Forgot Password?",
//                        style = MaterialTheme.typography.bodyMedium,
//                        color = MaterialTheme.colorScheme.primary,
//                        fontWeight = FontWeight.SemiBold
//                    )
//                }
//            }
//            Spacer(modifier = Modifier.height(24.dp))
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(bottom = 24.dp, top = 24.dp)
//                    .align(Alignment.BottomCenter),
//                horizontalArrangement = Arrangement.Center,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Text(
//                    text = "Don’t have an account?",
//                    style = MaterialTheme.typography.bodyMedium,
//                    color = MaterialTheme.colorScheme.onSurface
//                )
//                Spacer(modifier = Modifier.width(4.dp))
//                TextButton(onClick = { navController.navigate("signup") }) {
//                    Text(
//                        text = "Sign Up",
//                        style = MaterialTheme.typography.bodyMedium,
//                        color = MaterialTheme.colorScheme.primary,
//                        fontWeight = FontWeight.Bold
//                    )
//                }
//            }
//        }
//    }
//}

@Composable
fun LoginScreen(
    navController: NavController,
    presenter: AuthPresenter,
    sharedPresenter: SharedPresenter,
    ltScope: CoroutineScope
) {
    val loginInfo = presenter.loginInfo.collectAsState()
    val emailAddress = loginInfo.value.emailAddress
    val password = loginInfo.value.password
    val coroutineScope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }
    var passwordVisibility by remember { mutableStateOf(false) }
    var uiState by remember { mutableStateOf<UIState>(UIState.Idle) }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState) {
                Snackbar(
                    snackbarData = it,
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
                        value = loginInfo.value.emailAddress,
                        onValueChange = { presenter.onLoginInfoUpdate(email = it, pwd = password) },
                        label = { Text("Email") },
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth(0.85f)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    OutlinedTextField(
                        value = loginInfo.value.password,
                        onValueChange = { presenter.onLoginInfoUpdate(email = emailAddress, pwd = it) },
                        label = { Text("Password") },
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
                            if (loginInfo.value.emailAddress.isNotEmpty() && loginInfo.value.password.isNotEmpty()) {
                                coroutineScope.launch {
                                    presenter.login(loginInfo.value.emailAddress, loginInfo.value.password, navController)
                                }
                            } else {
                                coroutineScope.launch {
                                    snackBarHostState.showSnackbar("Please fill in all fields.")
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
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
                            Text(
                                text = "Login",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    TextButton(onClick = { navController.navigate("restore") }) {
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
                    TextButton(onClick = { navController.navigate("signup") }) {
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