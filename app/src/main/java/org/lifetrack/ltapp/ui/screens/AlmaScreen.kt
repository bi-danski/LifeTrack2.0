package org.lifetrack.ltapp.ui.screens

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddComment
import androidx.compose.material.icons.filled.ArrowCircleLeft
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.lifetrack.ltapp.presenter.ChatPresenter
import org.lifetrack.ltapp.ui.components.chatscreen.BBarMessage
import org.lifetrack.ltapp.ui.components.homescreen.LifeTrackTopBar
import org.lifetrack.ltapp.ui.components.medicalcharts.MessageBubble
import org.lifetrack.ltapp.ui.navigation.LTNavDispatcher
import org.lifetrack.ltapp.ui.theme.Purple40

@Composable
fun AlmaScreen(presenter: ChatPresenter) {
    val almaMessages by presenter.almaChats.collectAsState()
    val chatHistory by presenter.chatHistory.collectAsState()
    val inputText by presenter.chatInput.collectAsState()
    val isLoading by presenter.isLoading.collectAsState()
    val activeChatId by presenter.chatId.collectAsState()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

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
//                    DrawerDefaults.modalContainerColor
            ) {
                Spacer(Modifier.height(12.dp))
                Text(
                    "CHAT HISTORY",
                    modifier = Modifier.padding(16.dp),
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium
                )
                HorizontalDivider()

                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(chatHistory) { session ->
                        NavigationDrawerItem(
                            label = {
                                Text(
                                    text = if (session.text.isBlank() || session.text.isEmpty()) "Zero Chats" else session.text.take(30),
                                    maxLines = 1,
                                    color = Color.White,
                                    overflow = TextOverflow.Ellipsis
                                )
                            },
                            selected = session.chatId == activeChatId,
                            onClick = {
                                presenter.setChatSession(session.chatId)
                                scope.launch { drawerState.close() }
                            },
                            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                        )
                    }
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                LifeTrackTopBar(
                    title = "ALMA Healthcare Assistant",
                    navigationIcon = Icons.Default.ArrowCircleLeft,
                    backCallback = { LTNavDispatcher.navigateBack()  },
                    actionIcon = Icons.Default.AddComment,
                    actionCallback = { presenter.startNewChat() },
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
                    .padding(innerPadding),
            ) {
                if (isLoading) {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                }

                if (almaMessages.isEmpty() && !isLoading) {
                    Box(
                        modifier = Modifier.weight(1f).fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "How can i assist you today?",
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
}