package org.lifetrack.ltapp.model.data.dclass

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.NewReleases
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

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

enum class UIAppointmentStatus(
    val status: String,
    val color: Color,
    val icon: ImageVector
) {
    UPCOMING("Upcoming", Color(0xFF6200EE), Icons.Default.CalendarMonth),
    ATTENDED("Attended", Color(0xFF2ECC71), Icons.Default.CheckCircle),
    RECENTLY_BOOKED("Recently Booked", Color(0xFF7E57C2), Icons.Default.NewReleases),
    RESCHEDULED("Rescheduled", Color(0xFFE67E22), Icons.Default.History),
    DISMISSED("Dismissed", Color.Gray, Icons.Default.Cancel),
    CANCELLED("Cancelled", Color(0xFFE74C3C), Icons.Default.Block);
}

enum class SessionStatus {
    INITIALIZING, LOGGED_IN, LOGGED_OUT
}

enum class Intensity {
    LOW, MODERATE, VIGOROUS
}

enum class AttachmentType {
    PDF, IMAGE, LAB_RESULT
}

