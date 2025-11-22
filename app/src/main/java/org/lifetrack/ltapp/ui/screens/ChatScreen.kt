package org.lifetrack.ltapp.ui.screens

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.ArrowCircleLeft
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import org.koin.androidx.compose.koinViewModel
import org.lifetrack.ltapp.presenter.AlmaPresenter
import org.lifetrack.ltapp.ui.components.chatscreen.ChatBubble
import org.lifetrack.ltapp.ui.theme.LTAppTheme


@SuppressLint("MutableCollectionMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    navController: NavController,
    presenter: AlmaPresenter
) {
//    val coroutineScope = rememberCoroutineScope()
    val userInput = presenter.userInput
    val messages = presenter.messages
    val isLoading = presenter.isLoading

    Scaffold(
        topBar = {
//            LifeTrackTopBar(
//                title = "ALMA Healthcare Assistant",
//                Icons.Default.ArrowCircleLeft,
//                backCallback = { navController.popBackStack() },
//                actionCallback = {}
//            )
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "ALMA Healthcare Assistant ",
                        style = MaterialTheme.typography.titleLarge.copy(),
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.ArrowCircleLeft,
                            contentDescription = "Back",
//                            tint = Color(0xFF2E5EAA)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = userInput,
                    onValueChange =  presenter::onUserInputChange,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    placeholder = { Text("Ask ALMA Anything...", fontStyle = FontStyle.Italic) },
                    shape = RoundedCornerShape(20.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                )
                IconButton(
                    onClick = presenter::sendMessage
                ) {
                    Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send")
                }
            }
        },

    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)

        ) {
            if (isLoading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(12.dp),
                reverseLayout = true,
            ) {
                items(messages.reversed()) { message ->
                    ChatBubble(message)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.S)
@Preview
@Composable
fun PreviewChatScreen() {
    val navController = rememberNavController()
    LTAppTheme {
        ChatScreen(
            navController,
            koinViewModel<AlmaPresenter>()
        )
    }
}