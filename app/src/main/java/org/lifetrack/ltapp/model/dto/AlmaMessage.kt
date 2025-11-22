package org.lifetrack.ltapp.model.dto

data class AlmaMessage(
    val text: String,
    val check: Boolean? = false
){
    val isUser =  true
}


