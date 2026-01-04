package org.lifetrack.ltapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.ArrowCircleLeft
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import org.lifetrack.ltapp.presenter.AuthPresenter
import org.lifetrack.ltapp.presenter.UserPresenter
import org.lifetrack.ltapp.ui.components.profilescreen.CustomProfileMenuItem
import org.lifetrack.ltapp.ui.components.profilescreen.ProfileMenuItem
import org.lifetrack.ltapp.ui.theme.Purple40
import org.lifetrack.ltapp.ui.theme.Purple80
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.rememberCoroutineScope
//import kotlinx.coroutines.launch


//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun ProfileScreen(
//    navController: NavController,
//    authPresenter: AuthPresenter,
//    userPresenter: UserPresenter
//) {
//    val colorScheme = MaterialTheme.colorScheme
//    val profileInfo = authPresenter.profileInfo.collectAsStateWithLifecycle()
//    val snackbarHostState = remember { SnackbarHostState() }
//    val errorMessage by userPresenter.errorMessage.collectAsState()
//    val isRefreshing by authPresenter.isRefreshing.collectAsState()
//    val scope = rememberCoroutineScope()
//
//
//    LaunchedEffect(errorMessage) {
//        errorMessage?.let {
//            snackbarHostState.showSnackbar(
//                message = it,
//                actionLabel = "Dismiss",
//                duration = SnackbarDuration.Short
//            )
//            userPresenter.clearError()
//        }
//    }
//
//
//
//    Scaffold(
//        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
//        topBar = {
//            TopAppBar(
//                title = { Text(text = "Profile") },
//                navigationIcon = {
//                    IconButton({ navController.popBackStack() }) {
//                        Icon(
//                            imageVector = Icons.Default.ArrowCircleLeft,
//                            contentDescription = "Back",
//                            tint = if (isSystemInDarkTheme()) Purple80 else colorScheme.primary
//                        )
//                    }
//                },
//                colors = TopAppBarDefaults.topAppBarColors(
//                    containerColor = if (isSystemInDarkTheme()) colorScheme.primary.copy(0.1f) else Purple40,
//                )
//            )
//        }
//    ) { innerPadding ->
//        Column(
//            horizontalAlignment = Alignment.CenterHorizontally,
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(innerPadding)
//                .background(if (isSystemInDarkTheme()) colorScheme.primary.copy(0.1f) else Purple40)
//        ) {
//            Image(
//                painter = rememberAsyncImagePainter("https://i.pravatar.cc/300"),
//                contentDescription = "Profile Image",
//                contentScale = ContentScale.Crop,
//                modifier = Modifier
//                    .size(100.dp)
//                    .clip(CircleShape)
//            )
//            Spacer(modifier = Modifier.height(8.dp))
//            Text(
//                text = profileInfo.value.userFullName,
//                color = if (isSystemInDarkTheme()) Purple80 else colorScheme.onPrimary,
//                fontSize = 20.sp,
//                fontWeight = FontWeight.Bold
//            )
//            Spacer(modifier = Modifier.height(4.5.dp))
//            Text(
//                text = profileInfo.value.userPhoneNumber,
//                color = if (isSystemInDarkTheme()) Purple80 else colorScheme.onPrimary.copy(alpha = 0.8f),
//                fontSize = 18.sp,
//                fontWeight = FontWeight.Bold
//            )
//            Spacer(modifier = Modifier.height(20.dp))
//
////            Card(
////                shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
////                modifier = Modifier
////                    .fillMaxSize(),
////                colors = CardDefaults.cardColors(containerColor = colorScheme.background),
////                elevation = CardDefaults.cardElevation(8.dp),
////
////            ) {
//            Card(
//                shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
//                modifier = Modifier.fillMaxSize(),
//                colors = CardDefaults.cardColors(containerColor = colorScheme.background),
//                elevation = CardDefaults.cardElevation(8.dp),
//            ) {
//                // 1. Wrap your LazyColumn in PullToRefreshBox
//                PullToRefreshBox(
//                    isRefreshing = isRefreshing,
//                    onRefresh = {
//                        // 2. Call your refresh logic here
//                        userPresenter.refreshUserData()
//                    }
//                ) {
//                LazyColumn(
//                    contentPadding = PaddingValues(20.dp),
//                    modifier = Modifier
//                        .fillMaxSize(),
////                        .background(Color.Red),
//                    horizontalAlignment = Alignment.Start,
//                    verticalArrangement = Arrangement.Top
//
//                ) {
//                    item {
//                        Text(
//                            text = "Account Overview",
//                            fontSize = 18.sp,
//                            fontWeight = FontWeight.Bold,
//                            color = colorScheme.onBackground
//                        )
//                        Spacer(modifier = Modifier.height(20.dp))
//                    }
//                    item {
//                        ProfileMenuItem(
//                            icon = Icons.Default.Person,
//                            title = "Personal Information",
//                            onClick = {}
//                        )
//                    }
//                    item {
//                        ProfileMenuItem(
//                            icon = Icons.AutoMirrored.Filled.List,
//                            title = "My Medical History",
//                            onClick = {}
//                        )
//                    }
//                    item {
//                        ProfileMenuItem(
//                            icon = Icons.Default.Lock,
//                            title = "Change Password",
//                            onClick = { navController.navigate("restore") }
//                        )
//                    }
//                    item {
//                        ProfileMenuItem(
//                            icon = Icons.Default.Language,
//                            title = "Change Language",
//                            onClick = {}
//                        )
//                    }
//                    item {
//                        CustomProfileMenuItem(
//                            icon = Icons.Default.Delete,
//                            leftIconColor = Color.Red,
//                            title = "Delete My Account",
//                            onClick = { userPresenter.deleteAccount(navController) }
//                        )
//                    }
//                    item {
//                        CustomProfileMenuItem(
//                            icon = Icons.AutoMirrored.Filled.Logout,
//                            leftIconColor = Color.Red,
//                            title = "Logout",
//                            onClick = { authPresenter.logout(navController) }
//                        )
//                    }
//                }
//            }
//        }
//    }
//}
//
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    authPresenter: AuthPresenter,
    userPresenter: UserPresenter
) {
    val colorScheme = MaterialTheme.colorScheme
    val profileInfo = authPresenter.profileInfo.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val errorMessage by userPresenter.errorMessage.collectAsState()
    val isRefreshing by authPresenter.isRefreshing.collectAsState()

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
                title = { Text(text = "Profile") },
                navigationIcon = {
                    IconButton({ navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowCircleLeft,
                            contentDescription = "Back",
                            tint = if (isSystemInDarkTheme()) Purple80 else colorScheme.primary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = if (isSystemInDarkTheme()) colorScheme.primary.copy(0.1f) else Purple40,
                )
            )
        }
    ) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = { authPresenter.loadUserProfile() },
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(if (isSystemInDarkTheme()) colorScheme.primary.copy(0.1f) else Purple40)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    Image(
                        painter = rememberAsyncImagePainter("https://i.pravatar.cc/300"),
                        contentDescription = "Profile Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = profileInfo.value.userFullName,
                        color = if (isSystemInDarkTheme()) Purple80 else colorScheme.onPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.5.dp))
                    Text(
                        text = profileInfo.value.userPhoneNumber,
                        color = if (isSystemInDarkTheme()) Purple80 else colorScheme.onPrimary.copy(alpha = 0.8f),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                }

                item {
                    Card(
                        shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
                        modifier = Modifier.fillParentMaxHeight(),
                        colors = CardDefaults.cardColors(containerColor = colorScheme.background),
                        elevation = CardDefaults.cardElevation(8.dp),
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(20.dp)
                                .fillMaxSize()
                        ) {
                            Text(
                                text = "Account Overview",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = colorScheme.onBackground
                            )
                            Spacer(modifier = Modifier.height(20.dp))

                            ProfileMenuItem(
                                icon = Icons.Default.Person,
                                title = "Personal Information",
                                onClick = {}
                            )
                            ProfileMenuItem(
                                icon = Icons.AutoMirrored.Filled.List,
                                title = "My Medical History",
                                onClick = {}
                            )
                            ProfileMenuItem(
                                icon = Icons.Default.Lock,
                                title = "Change Password",
                                onClick = { navController.navigate("restore") }
                            )
                            ProfileMenuItem(
                                icon = Icons.Default.Language,
                                title = "Change Language",
                                onClick = {}
                            )
                            CustomProfileMenuItem(
                                icon = Icons.Default.Delete,
                                leftIconColor = Color.Red,
                                title = "Delete My Account",
                                onClick = { userPresenter.deleteAccount(navController) }
                            )
                            CustomProfileMenuItem(
                                icon = Icons.AutoMirrored.Filled.Logout,
                                leftIconColor = Color.Red,
                                title = "Logout",
                                onClick = { authPresenter.logout(navController) }
                            )
                        }
                    }
                }
            }
        }
    }
}