package org.lifetrack.ltapp.view.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import org.lifetrack.ltapp.view.components.profilescreen.CustomProfileMenuItem
import org.lifetrack.ltapp.view.components.profilescreen.ProfileMenuItem
import org.lifetrack.ltapp.view.ui.theme.LTAppTheme

@Composable
fun ProfileScreen(navController: NavController) {
    val colorScheme = MaterialTheme.colorScheme
    val userFullName = remember { mutableStateOf("Admin Kamau") }
    val userPhoneNumber = remember { mutableStateOf("+254790938365") }

    Scaffold(
        containerColor = colorScheme.primary
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
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
                    text = userFullName.value,
                    color = colorScheme.onPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = userPhoneNumber.value,
                    color = colorScheme.onPrimary.copy(alpha = 0.8f),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
                modifier = Modifier
                    .fillMaxSize(),
                colors = CardDefaults.cardColors(containerColor = colorScheme.background),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Account Overview",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    ProfileMenuItem(icon = Icons.Default.Person, title = "Personal Information", onClick = {})
                    ProfileMenuItem(icon = Icons.AutoMirrored.Filled.List, title = "My Medical History", onClick = {})
//                    ProfileMenuItem(icon = Icons.Default.Refresh, title = "Refund")
                    ProfileMenuItem(icon = Icons.Default.Lock, title = "Change Password", onClick = {})
                    ProfileMenuItem(icon = Icons.Default.Language, title = "Change Language", onClick = {})
                    CustomProfileMenuItem(
                        icon = Icons.Default.Delete,
                        leftIconColor = colorScheme.primary,
                        title = "Delete My Account",
                        onClick = {

                            navController.navigate("login")
                        }
                    )
                    CustomProfileMenuItem(
                        icon = Icons.AutoMirrored.Filled.Logout,
                        leftIconColor = Color.Red,
                        title = "Logout",
                        onClick = { navController.navigate("login")}
                    )
                }
            }
        }
    }
}

//@RequiresApi(Build.VERSION_CODES.S)
//@Preview
//@Composable
//fun PreviewProfileScreen() {
//    val navController = rememberNavController()
//    LTAppTheme {
//        ProfileScreen(
//            navController
//        )
//    }
//}