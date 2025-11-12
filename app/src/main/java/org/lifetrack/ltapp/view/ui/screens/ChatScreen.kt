package org.lifetrack.ltapp.view.ui.screens.patient.chatscreen
//
//import android.util.Log
//import android.widget.Toast
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.automirrored.filled.ArrowBack
//import androidx.compose.material.icons.automirrored.filled.Send
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.input.TextFieldValue
//import androidx.compose.ui.unit.dp
//import androidx.navigation.NavController
//import com.example.lifetrack.presenter.ChatPresenter
//import com.example.lifetrack.view.AIChatView
//import kotlinx.coroutines.launch
//import com.example.lifetrack.model.data.ChatMessage
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun ChatScreen(
//    navController: NavController,
//    presenter: ChatPresenter
//) {
//    val coroutineScope = rememberCoroutineScope()
//    val context = LocalContext.current
//    var userInput by remember { mutableStateOf(TextFieldValue("")) }
//    var messages by remember { mutableStateOf(listOf<ChatMessage>()) }
//    var isLoading by remember { mutableStateOf(false) }
//    LaunchedEffect(Unit) {
//        presenter.attachView(object : AIChatView {
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
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text("Coral Healthcare Assistant") },
//                navigationIcon = {
//                    IconButton(onClick = { navController.popBackStack() }) {
//                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
//                    }
//                }
//            )
//        },
//        bottomBar = {
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(8.dp),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                TextField(
//                    value = userInput,
//                    onValueChange = { userInput = it },
//                    modifier = Modifier
//                        .weight(1f)
//                        .padding(end = 8.dp),
//                    placeholder = { Text("Ask Coral AI...") },
//                    shape = RoundedCornerShape(20.dp),
//                    colors = OutlinedTextFieldDefaults.colors(
//                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
//                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant
//                    )
//                )
//                IconButton(
//                    onClick = {
//                        if (userInput.text.isNotBlank()) {
//                            val userMessage = ChatMessage(userInput.text, true)
//                            messages = messages + userMessage
//
//                            Log.d("ChatScreen", "User message: ${userMessage.text}")
//                            coroutineScope.launch {
//                                presenter.sendMessageToAI(userMessage.text)
//
//                            }
//                            userInput = TextFieldValue("")
//                        }
//                    }
//                ) {
//                    Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send")
//                }
//            }
//        }
//    ) { innerPadding ->
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(innerPadding)
//        ) {
//            if (isLoading) {
//                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
//            }
//
//            LazyColumn(
//                modifier = Modifier
//                    .weight(1f)
//                    .padding(12.dp),
//                reverseLayout = true
//            ) {
//                items(messages.reversed()) { message ->
//                    ChatBubble(message)
//                    Spacer(modifier = Modifier.height(8.dp))
//                }
//            }
//        }
//    }
//
//}
//
//@Composable
//fun ChatBubble(message: ChatMessage) {
//    val bubbleColor = if (message.isUser) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
//    val textColor = if (message.isUser) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
//
//    Row(
//        modifier = Modifier.fillMaxWidth(),
//        horizontalArrangement = if (message.isUser) Arrangement.End else Arrangement.Start
//    ) {
//        Box(
//            modifier = Modifier
//                .background(bubbleColor, shape = RoundedCornerShape(12.dp))
//                .padding(12.dp)
//                .widthIn(max = 260.dp)
//        ) {
//            Text(
//                text = message.text,
//                color = textColor,
//                style = MaterialTheme.typography.bodyMedium
//            )
//        }
//    }
//
//}
