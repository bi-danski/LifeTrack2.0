package org.lifetrack.ltapp.ui.components.homescreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CommentBank
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.lifetrack.ltapp.ui.navigation.LTNavDispatcher
import org.lifetrack.ltapp.ui.theme.Purple40


@Composable
fun AppTopBar(username: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.background)
                    .border(width = 2.dp,
                        color = MaterialTheme.colorScheme.primary, //Purple40
                        shape = CircleShape),
                contentAlignment = Alignment.Center

            ) {
                IconButton(onClick = { LTNavDispatcher.navigate("menu") },
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.background))
                {
                    Icon(Icons.Filled.Menu, contentDescription = "", tint = Purple40) }
            }
            Spacer(Modifier.width(10.dp))
            Text(
                text = "LIFETRACK",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                ),
                color =  MaterialTheme.colorScheme.primary
            )
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = username,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.ExtraBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text("Patient",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.Bold)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LifeTrackTopBar(
    title: String,
    navigationIcon: ImageVector,
    modifier: Modifier = Modifier,
    backCallback: () -> Unit,
    actionIcon: ImageVector? = null,
    actionCallback: () -> Unit,
    actionCallbackIngine: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontStyle = if (title.lowercase().contains("alma")) FontStyle.Italic else FontStyle.Normal

            )
        },
        navigationIcon = {
            IconButton(onClick = backCallback) {
                Icon(
                    imageVector = navigationIcon,
                    contentDescription = "Back"
                )
            }
        },
        actions = {
            if (actionIcon != null) {
                IconButton(onClick = actionCallback) {
                    Icon(
                        imageVector = actionIcon,
                        contentDescription = "Action"
                    )
                }
            }

            IconButton(
                onClick = actionCallbackIngine
            ) {
                Icon(
                    imageVector = Icons.Default.CommentBank,
                    contentDescription = null
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Purple40,
//                    MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = Color.White, //if (isSystemInDarkTheme()) Color.White else MaterialTheme.colorScheme.onPrimaryContainer,
            navigationIconContentColor = Color.White, //if (isSystemInDarkTheme()) Color.White else MaterialTheme.colorScheme.onPrimaryContainer,
            actionIconContentColor = Color.White, //if (isSystemInDarkTheme()) Color.White else MaterialTheme.colorScheme.onPrimaryContainer,
        ),
        modifier = modifier,
        scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    )
}

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun LTCenterTopBar(
//    title: String,
//    navigationIcon: ImageVector,
//    backCallback: () -> Unit,
//    actionIcon: ImageVector? = null,
//    actionCallback: () -> Unit,
//    actionCallbackIngine: () -> Unit
//
//){
//    CenterAlignedTopAppBar(
//        title = {
//            Text(
//                text = title,
//                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
//            )
//        },
//        navigationIcon = {
//            IconButton(onClick = backCallback) {
//                Icon(
//                    navigationIcon,
//                    contentDescription = "Back",
////                            tint = MaterialTheme.colorScheme.onPrimaryContainer
//                )
//            }
//        },
//        actions = {
//            if (actionIcon != null) {
//                IconButton(onClick = actionCallback) {
//                    Icon(
//                        imageVector = actionIcon,
//                        contentDescription = "Action"
//                    )
//                }
//            }
//
//            IconButton(
//                onClick = actionCallbackIngine
//            ) {
//                Icon(
//                    imageVector = Icons.Default.CommentBank,
//                    contentDescription = null
//                )
//            }
//        },
//
//        colors = TopAppBarDefaults.topAppBarColors(
//            containerColor = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.primaryContainer else Purple40,
//            scrolledContainerColor = Color.Unspecified,
//            navigationIconContentColor = Color.Unspecified,
//            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
//            actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
//        )
//    )
//}
