package org.lifetrack.ltapp.presenter

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class PrescPresenter : ViewModel() {

    var selectedFilter by mutableStateOf("Active")
    val filters = listOf("Active", "Refills", "Expired", "History")
    val listState = LazyListState()

    fun getStatusColor(filter: String): Color {
        return when (filter) {
            "Active" -> Color(0xFF2E7D32)
            "Refills" -> Color(0xFFEF6C00)
            "Expired" -> Color(0xFFD32F2F)
            else -> Color(0xFF4A148C)
        }
    }

    fun isDateExpired(endDateStr: String): Boolean {
        return try {
            val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.ENGLISH)
            val endDate = LocalDate.parse(endDateStr, formatter)
            endDate.isBefore(LocalDate.now())
        } catch (_: Exception) {
            false
        }
    }

    fun getIconForFilter(filter: String): ImageVector {
        return when (filter) {
            "Active" -> Icons.Default.CheckCircle
            "Refills" -> Icons.Default.Autorenew
            "Expired" -> Icons.Default.EventBusy
            else -> Icons.Default.History
        }
    }

    var showSuccessSheet by mutableStateOf(false)
    var lastRequestedMedication by mutableStateOf("")

    fun triggerRefillRequest(medicationName: String) {
        lastRequestedMedication = medicationName
        showSuccessSheet = true
    }
}