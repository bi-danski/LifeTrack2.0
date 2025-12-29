package org.lifetrack.ltapp.model.data.dclass

import androidx.compose.ui.graphics.Color

enum class VisitStatus(
    val label: String,
    val color: Color
) {
    ONGOING("Ongoing", Color(0xFFFBC02D)),
    COMPLETED("Completed", Color(0xFF4CAF50)),
    FOLLOW_UP("Follow-up", Color(0xFF42A5F5))
}

sealed class VisitFilter(val displayName: String) {
    object Recent : VisitFilter("Recent Visits")
    object Oldest : VisitFilter("Oldest First")
    object Alphabetical : VisitFilter("Hospital A-Z")
}

val filterOptions = listOf(
    VisitFilter.Recent,
    VisitFilter.Oldest,
    VisitFilter.Alphabetical
)

enum class AttachmentType {
    PDF, IMAGE, LAB_RESULT
}
