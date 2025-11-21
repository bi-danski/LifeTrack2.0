package org.lifetrack.ltapp.ui.screens

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import org.lifetrack.ltapp.model.dto.AlmaMessage
import org.lifetrack.ltapp.presenter.AlmaPresenter
import org.lifetrack.ltapp.presenter.mock.almaMockPresenter
import org.lifetrack.ltapp.ui.theme.LTAppTheme
import org.lifetrack.ltapp.ui.components.chatscreen.ChatBubble


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    navController: NavController,
    presenter: AlmaPresenter
) {
    val coroutineScope = rememberCoroutineScope()
//    val context = LocalContext.current
    var userInput by remember { mutableStateOf(TextFieldValue("")) }
    var messages by remember { mutableStateOf(listOf<AlmaMessage>()) }
    var isLoading by remember { mutableStateOf(false) }
//    LaunchedEffect(Unit) {
//        presenter.attachView(object : AlmaView {
//            override fun showLoading() {
//                isLoading = true
//            }
//            override fun hideLoading() {
//                isLoading = false
//            }
//            override fun displayAIResponse(response: String) {
//                messages = messages + ChatMessage("AI: $response", false)
//            }
//            override fun showError(message: String) {
//                Toast.makeText(context, "Error: $message", Toast.LENGTH_LONG).show()
//            }
//        })
//    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ALMA Healthcare Assistant") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
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
                    onValueChange = { userInput = it },
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
                    onClick = {
                        if (userInput.text.isNotBlank()) {
                            val userMessage = AlmaMessage(userInput.text, true)
                            messages = messages + userMessage

                            Log.d("ChatScreen", "User message: ${userMessage.text}")
                            coroutineScope.launch {
                                presenter.sendMessage(userMessage.text)
                            }
                            userInput = TextFieldValue("")
                        }
                    }
                ) {
                    Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send")
                }
            }
        }
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
                reverseLayout = true
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
            almaMockPresenter
        )
    }
}