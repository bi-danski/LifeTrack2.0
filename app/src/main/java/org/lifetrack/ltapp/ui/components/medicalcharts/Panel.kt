package org.lifetrack.ltapp.ui.components.medicalcharts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.lifetrack.ltapp.model.dto.Message


//@Composable
//fun ChatPanel(messages: List<Message>) {
//
//    Column {
//        LazyColumn(
//            modifier = Modifier
//                .height(200.dp)
//                .padding(8.dp)
//        ) {
//            items(messages) { message ->
//                MessageBubble(message = message)
//                Spacer(modifier = Modifier.height(4.dp))
//            }
//        }

//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(8.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            BasicTextField(
//                value = newMessage,
//                onValueChange = { newMessage = it },
//                modifier = Modifier
//                    .weight(1f)
//                    .background(Color.LightGray.copy(alpha = 0.2f))
//                    .padding(12.dp),
//                decorationBox = { innerTextField ->
//                    if (newMessage.text.isEmpty()) {
//                        Text(
//                            "Type message...",
//                            color = Color.Gray
//                        )
//                    }
//                    innerTextField()
//                }
//            )
//
//            IconButton(
//                onClick = { newMessage = TextFieldValue("") },
//                modifier = Modifier.padding(start = 8.dp)
//            ) {
//                Icon(
//                    Icons.AutoMirrored.Filled.Send,
//                    contentDescription = "Send",
//                    tint = MaterialTheme.colorScheme.primary
//                )
//            }
//        }
//    }
//}