package org.lifetrack.ltapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleLeft
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.rounded.LibraryAdd
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.lifetrack.ltapp.presenter.ChatPresenter
import org.lifetrack.ltapp.ui.components.chatscreen.BBarMessage
import org.lifetrack.ltapp.ui.components.homescreen.LifeTrackTopBar
import org.lifetrack.ltapp.ui.components.medicalcharts.MessageBubble
import org.lifetrack.ltapp.ui.components.other.LTSnackbar
import org.lifetrack.ltapp.ui.navigation.LTNavDispatch
import org.lifetrack.ltapp.ui.theme.Purple40

@Composable
fun AlmaScreen(presenter: ChatPresenter) {
    val almaMessages by presenter.almaChats.collectAsState()
    val chatHistory by presenter.chatHistory.collectAsState()
    val inputText by presenter.chatInput.collectAsState()
    val isLoading by presenter.isLoading.collectAsState()
    val activeChatId by presenter.chatId.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    var expandedChatId by remember { mutableStateOf<String?>(null) }
    var showRenameDialog by remember { mutableStateOf<String?>(null) }
    var newTitle by remember { mutableStateOf("") }

    fun showStatus(msg: String) {
        scope.launch {
            snackbarHostState.showSnackbar(
                message = msg,
                duration = SnackbarDuration.Indefinite
            )
        }
    }

    LaunchedEffect(almaMessages.size) {
        if (almaMessages.isNotEmpty()) {
            listState.animateScrollToItem(0)
        }
    }

    LaunchedEffect(activeChatId) {
        listState.scrollToItem(0)
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.background else Purple40
            ) {
                Spacer(Modifier.height(12.dp))
                Text(
                    text = "CHAT HISTORY",
                    modifier = Modifier.padding(16.dp),
                    color = if (isSystemInDarkTheme()) Color.White else MaterialTheme.colorScheme.onPrimaryContainer,
                    fontWeight = FontWeight.Black,
                    style = MaterialTheme.typography.titleMedium
                )
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                )

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    itemsIndexed(
                        items = chatHistory,
                        key = { _, session -> session.chatId }
                    ) { _, session ->
                        NavigationDrawerItem(
                            label = {
                                Text(
                                    text = session.text.ifBlank { "New Conversation" }.take(30),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            },
                            selected = session.chatId == activeChatId,
                            onClick = {
                                presenter.setChatSession(session.chatId)
                                scope.launch { drawerState.close() }
                            },
                            badge = {
                                Box {
                                    IconButton(onClick = { expandedChatId = session.chatId }) {
                                        Icon(
                                            imageVector = Icons.Default.MoreVert,
                                            contentDescription = "Options",
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }

                                    DropdownMenu(
                                        expanded = expandedChatId == session.chatId,
                                        onDismissRequest = { expandedChatId = null }
                                    ) {
                                        DropdownMenuItem(
                                            text = { Text("Rename") },
                                            leadingIcon = { Icon(Icons.Default.Edit, null, modifier = Modifier.size(18.dp)) },
                                            onClick = {
                                                expandedChatId = null
                                                newTitle = session.text
                                                showRenameDialog = session.chatId
                                            }
                                        )
                                        DropdownMenuItem(
                                            text = { Text("Delete", color = MaterialTheme.colorScheme.error) },
                                            leadingIcon = { Icon(Icons.Default.DeleteSweep, null, modifier = Modifier.size(18.dp), tint = MaterialTheme.colorScheme.error) },
                                            onClick = {
                                                expandedChatId = null
                                                presenter.deleteChat(session.chatId)
                                                showStatus("Chat Deleted")
                                            }
                                        )
                                    }
                                }
                            },
                            colors = NavigationDrawerItemDefaults.colors(
                                unselectedTextColor = if (isSystemInDarkTheme()) Color.White else Color.DarkGray,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                unselectedIconColor = if (isSystemInDarkTheme()) Color.LightGray else Color.Gray
                            ),
                            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                        )
                    }
                }
            }
        }
    ) {
        Scaffold(
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState) { data ->
                    LTSnackbar(snackbarData = data)
                }
            },
            topBar = {
                LifeTrackTopBar(
                    title = "Alma Assistant",
                    navigationIcon = Icons.Default.ArrowCircleLeft,
                    backCallback = { LTNavDispatch.navigateBack() },
                    actionIcon = Icons.Rounded.LibraryAdd,
                    actionCallback = {
                        presenter.startNewChat()
                        showStatus("New Session Started")
                    },
                    actionCallbackIngine = { scope.launch { drawerState.open() } }
                )
            },
            bottomBar = {
                BBarMessage(
                    value = inputText,
                    onValueChange = { presenter.onMessageInput(it) },
                    onSend = { presenter.chatWithAlma() }
                )
            },
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(MaterialTheme.colorScheme.background),
            ) {
                if (isLoading) {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                if (almaMessages.isEmpty() && !isLoading) {
                    Box(
                        modifier = Modifier.weight(1f).fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "How can I assist you today?",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        state = listState,
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        reverseLayout = true
                    ) {
                        itemsIndexed(
                            items = almaMessages,
                            key = { _, message -> message.id }
                        ) { _, message ->
                            MessageBubble(message)
                        }
                    }
                }
            }
        }
    }

    if (showRenameDialog != null) {
        AlertDialog(
            modifier = Modifier.width(300.dp),
            onDismissRequest = {  },
            title = { Text("Rename Chat", style = MaterialTheme.typography.titleMedium) },
            text = {
                OutlinedTextField(
                    value = newTitle,
                    onValueChange = { newTitle = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyMedium,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary
                    )
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    showRenameDialog?.let { id ->
                        presenter.renameChat(id, newTitle)
                    }
                }) {
                    Text("Save", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { }) {
                    Text("Cancel")
                }
            }
        )
    }
}