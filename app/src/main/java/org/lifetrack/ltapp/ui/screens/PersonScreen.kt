package org.lifetrack.ltapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.lifetrack.ltapp.R
import org.lifetrack.ltapp.presenter.AuthPresenter
import org.lifetrack.ltapp.presenter.UserPresenter
import org.lifetrack.ltapp.ui.navigation.LTNavDispatch
import org.lifetrack.ltapp.ui.theme.Purple40
import org.lifetrack.ltapp.ui.theme.Purple80

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalInfoScreen(authPresenter: AuthPresenter, userPresenter: UserPresenter) {
    val colorScheme = MaterialTheme.colorScheme
    val profileInfo = authPresenter.profileInfo.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val errorMessage by userPresenter.errorMessage.collectAsState()

    var fullName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var userName by remember { mutableStateOf("") }
    var isEditing by remember { mutableStateOf(false) }

    LaunchedEffect(profileInfo.value) {
        fullName = profileInfo.value.userFullName
        phoneNumber = profileInfo.value.userPhoneNumber
        userName = profileInfo.value.userName
    }

    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            snackbarHostState.showSnackbar(
                message = it,
                actionLabel = "Dismiss",
                duration = SnackbarDuration.Short
            )
            userPresenter.clearError()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.personal_information)) },
                navigationIcon = {
                    IconButton(onClick = { LTNavDispatch.navigateBack() }) {
                        Icon(Icons.Default.ArrowCircleLeft, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = if (isSystemInDarkTheme()) colorScheme.primary.copy(0.1f) else Purple40,
                    titleContentColor = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(colorScheme.background)
                .padding(20.dp)
        ) {
            item {
                Text(
                    text = stringResource(R.string.edit_your_profile),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(20.dp))

                // Full Name
                OutlinedTextField(
                    value = fullName,
                    onValueChange = { if (isEditing) fullName = it },
                    label = { Text(stringResource(R.string.full_name)) },
                    enabled = isEditing,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = if (isSystemInDarkTheme()) Purple80 else Purple40,
                        unfocusedBorderColor = colorScheme.outline
                    ),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Username
                OutlinedTextField(
                    value = userName,
                    onValueChange = { if (isEditing) userName = it },
                    label = { Text(stringResource(R.string.username)) },
                    enabled = isEditing,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = if (isSystemInDarkTheme()) Purple80 else Purple40,
                        unfocusedBorderColor = colorScheme.outline
                    ),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Email (Read-only)
                OutlinedTextField(
                    value = profileInfo.value.userEmail,
                    onValueChange = { },
                    label = { Text(stringResource(R.string.email)) },
                    enabled = false,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledBorderColor = colorScheme.outline.copy(alpha = 0.5f),
                        disabledTextColor = colorScheme.onBackground.copy(alpha = 0.6f)
                    ),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Phone Number
                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = { if (isEditing) phoneNumber = it },
                    label = { Text(stringResource(R.string.phone_number)) },
                    enabled = isEditing,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = if (isSystemInDarkTheme()) Purple80 else Purple40,
                        unfocusedBorderColor = colorScheme.outline
                    ),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(30.dp))

                // Edit/Save Button
                Button(
                    onClick = {
                        if (isEditing) {
                            userPresenter.updateUserProfile(
                                fullName = fullName,
                                userName = userName,
                                phoneNumber = phoneNumber
                            ) {
                                isEditing = false
                            }
                        } else {
                            isEditing = true
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(
                        text = if (isEditing) stringResource(R.string.save_changes) else stringResource(R.string.edit_profile),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }

                if (isEditing) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = {
                            isEditing = false
                            // Reset values
                            fullName = profileInfo.value.userFullName
                            phoneNumber = profileInfo.value.userPhoneNumber
                            userName = profileInfo.value.userName
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                            containerColor = Color.Gray
                        )
                    ) {
                        Text(
                            text = stringResource(R.string.cancel),
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))

                Text(
                    text = stringResource(R.string.account_information),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colorScheme.onBackground.copy(alpha = 0.7f)
                )
                Spacer(modifier = Modifier.height(12.dp))

                InfoRow(stringResource(R.string.member_since), profileInfo.value.createdAt?.toString() ?: "N/A", colorScheme)
                Spacer(modifier = Modifier.height(8.dp))
                InfoRow(stringResource(R.string.last_updated), profileInfo.value.updatedAt?.toString() ?: "N/A", colorScheme)
                Spacer(modifier = Modifier.height(8.dp))
                InfoRow(stringResource(R.string.account_role), profileInfo.value.role ?: "User", colorScheme)
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String, colorScheme: androidx.compose.material3.ColorScheme) {
    Column {
        Text(
            text = label,
            fontSize = 12.sp,
            color = colorScheme.onBackground.copy(alpha = 0.6f),
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = value,
            fontSize = 14.sp,
            color = colorScheme.onBackground,
            fontWeight = FontWeight.Medium
        )
    }
}

