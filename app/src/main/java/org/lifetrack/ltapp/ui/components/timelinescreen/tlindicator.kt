// TimelineIndicator.kt (No major changes needed, but here's the full code with imports)
package org.lifetrack.ltapp.ui.components.timelinescreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.lifetrack.ltapp.model.data.dclass.VisitStatus

@Composable
fun TimelineIndicator(status: VisitStatus, isLast: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .background(status.color, CircleShape)
        )
        if (!isLast) {
            Spacer(
                modifier = Modifier
                    .width(2.dp)
                    .height(80.dp)
                    .background(MaterialTheme.colorScheme.outlineVariant)
            )
        }
    }
}