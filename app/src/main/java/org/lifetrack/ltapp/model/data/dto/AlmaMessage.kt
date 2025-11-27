package org.lifetrack.ltapp.model.data.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AlmaMessage(
    val text: String,
    val check: Boolean? = false,
    val isUser: Boolean = true
): Parcelable