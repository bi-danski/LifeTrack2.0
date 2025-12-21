package org.lifetrack.ltapp.ui.components.homescreen

//import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import org.lifetrack.ltapp.ui.theme.Purple40

@Composable
fun AppTopBar(navController: NavController) {
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
                IconButton(onClick = { navController.navigate("menu") },
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
            Text("Dr. Najma",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.ExtraBold,
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
    actionCallback: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
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
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Purple40,
//                    MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        modifier = modifier,
        scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    )
}