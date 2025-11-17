package org.lifetrack.ltapp.model.data.dto

data class AlmaMessage(
    val text: String,
    val check: Boolean? = false
)
{
    val isUser =  true
}


