package org.lifetrack.ltapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleLeft
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import org.lifetrack.ltapp.presenter.ChatPresenter
import org.lifetrack.ltapp.ui.components.chatscreen.BBarMessage
import org.lifetrack.ltapp.ui.components.homescreen.LifeTrackTopBar
import org.lifetrack.ltapp.ui.components.medicalcharts.MessageBubble

@Composable
fun ChatScreen(
    navController: NavController,
    presenter: ChatPresenter
) {
    val chatMessages by presenter.myChats.collectAsState()
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(chatMessages.size) {
        scope.launch {
            listState.animateScrollToItem(chatMessages.size)
//            chatMessages.size - 1)
        }
    }

    Scaffold(
        topBar = {
            LifeTrackTopBar(
                title = "Messages & Referrals",
                navigationIcon = Icons.Default.ArrowCircleLeft,
                modifier = Modifier,
                backCallback = { navController.popBackStack() },
                actionIcon = null,
                actionCallback = {}
            )
        },
        bottomBar = {
            BBarMessage(presenter = presenter)
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            state = listState,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            itemsIndexed(
                chatMessages,
//                key = { it.id }
            ) { _, message ->
                MessageBubble(message)
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}
