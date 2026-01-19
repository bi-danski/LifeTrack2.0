//package org.lifetrack.ltapp.ui.screens
//
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.itemsIndexed
//import androidx.compose.foundation.lazy.rememberLazyListState
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowCircleLeft
//import androidx.compose.material3.LinearProgressIndicator
//import androidx.compose.material3.Scaffold
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import androidx.navigation.NavController
//import kotlinx.coroutines.launch
//import org.lifetrack.ltapp.presenter.ChatPresenter
//import org.lifetrack.ltapp.ui.components.chatscreen.BBarMessage
//import org.lifetrack.ltapp.ui.components.homescreen.LifeTrackTopBar
//import org.lifetrack.ltapp.ui.components.medicalcharts.MessageBubble
//
//
//@Composable
//fun AlmaScreen(
//    navController: NavController,
//    presenter: ChatPresenter
//) {
//    val almaMessages by presenter.almaChats.collectAsState()
//    val inputText by presenter.chatInput.collectAsState()
//    val isLoading by presenter.isLoading.collectAsState()
//
//    val listState = rememberLazyListState()
//    val scope = rememberCoroutineScope()
//
//    LaunchedEffect(almaMessages.size, isLoading) {
//        if (almaMessages.isNotEmpty()) {
//            scope.launch {
//                listState.animateScrollToItem(0)
//            }
//        }
//    }
//
//    Scaffold(
//        topBar = {
//            LifeTrackTopBar(
//                title = "ALMA Healthcare Assistant",
//                navigationIcon = Icons.Default.ArrowCircleLeft,
//                backCallback = { navController.popBackStack() },
//                actionCallback = {}
//            )
//        },
//        bottomBar = {
//            BBarMessage(
//                value = inputText,
//                onValueChange = { presenter.onMessageInput(it) },
//                onSend = { presenter.chatWithAlma() }
//            )
//        },
//    ) { innerPadding ->
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(innerPadding),
//        ) {
//            if (isLoading) {
//                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
//            }
//            LazyColumn(
//                modifier = Modifier
//                    .weight(1f)
//                    .fillMaxWidth(),
//                state = listState,
//                verticalArrangement = Arrangement.spacedBy(10.dp),
//                horizontalAlignment = Alignment.CenterHorizontally,
//                reverseLayout = true
//            ) {
//                itemsIndexed(
//                    items = almaMessages,
//                    key = { _, message -> message.id }
//                ) { _, message ->
//                    MessageBubble(message)
//                }
//            }
//        }
//    }
//}
//
//
package org.lifetrack.ltapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddComment
import androidx.compose.material.icons.filled.ArrowCircleLeft
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import org.lifetrack.ltapp.presenter.ChatPresenter
import org.lifetrack.ltapp.ui.components.chatscreen.BBarMessage
import org.lifetrack.ltapp.ui.components.homescreen.LifeTrackTopBar
import org.lifetrack.ltapp.ui.components.medicalcharts.MessageBubble

@Composable
fun AlmaScreen(
    navController: NavController,
    presenter: ChatPresenter
) {
    val almaMessages by presenter.almaChats.collectAsState()
    val inputText by presenter.chatInput.collectAsState()
    val isLoading by presenter.isLoading.collectAsState()
    val activeChatId by presenter.chatId.collectAsState()

    val listState = rememberLazyListState()
//    val scope = rememberCoroutineScope()

    LaunchedEffect(almaMessages.size) {
        if (almaMessages.isNotEmpty()) {
            listState.animateScrollToItem(0)
        }
    }

    LaunchedEffect(activeChatId) {
        listState.scrollToItem(0)
    }

    Scaffold(
        topBar = {
            LifeTrackTopBar(
                title = "ALMA Healthcare Assistant",
                navigationIcon = Icons.Default.ArrowCircleLeft,
                backCallback = { navController.popBackStack() },
                actionIcon = Icons.Default.AddComment,
                actionCallback = { presenter.startNewChat() }
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
                        text = "How can Alma help you today?",
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