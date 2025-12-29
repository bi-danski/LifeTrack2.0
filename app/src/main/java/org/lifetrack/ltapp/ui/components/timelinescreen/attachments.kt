package org.lifetrack.ltapp.ui.components.timelinescreen

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.lifetrack.ltapp.model.data.dclass.Attachment
import kotlin.collections.forEach

@Composable
fun AttachmentsSection(attachments: List<Attachment>) {
    if (attachments.isEmpty()) return

    Spacer(Modifier.height(8.dp))

    Text("${attachments.size} attachment(s)",
        style = MaterialTheme.typography.labelLarge.copy(
            fontWeight = FontWeight.SemiBold
        )
    )
    Spacer(Modifier.height(8.dp))
    attachments.forEach {
        ListItem(
            headlineContent = { Text(it.name) },
            leadingContent = { Icon(Icons.Default.AttachFile, null) },
            colors = ListItemDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow
            )

        )
    }
}