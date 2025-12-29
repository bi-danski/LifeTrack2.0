package org.lifetrack.ltapp.ui.components.timelinescreen

import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import org.lifetrack.ltapp.model.data.dclass.VisitStatus

@Composable
fun StatusChip(status: VisitStatus) {
    AssistChip(
        onClick = {},
        label = { Text(status.label) },
        colors = AssistChipDefaults.assistChipColors(
            containerColor = status.color.copy(alpha = 0.15f),
            labelColor = status.color
        )
    )
}