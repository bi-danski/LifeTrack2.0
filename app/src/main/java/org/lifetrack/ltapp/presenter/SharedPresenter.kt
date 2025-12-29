package org.lifetrack.ltapp.presenter

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import org.lifetrack.ltapp.core.utils.makeAutoCall

class SharedPresenter: ViewModel() {
    var appAnimationsToggleState by mutableStateOf(true)
        private set
    var version by mutableStateOf("2.0.0")
        private set

    fun onAppAnimationsUpdate(){
        appAnimationsToggleState = !appAnimationsToggleState
    }

//    fun onAppVersionUpdate(value: String){
//        version = value
//    }
    // UserPresenter.kt

    fun handleEmergencyCall(context: Context) {
        val emergencyNumber = "911"
        context.makeAutoCall(emergencyNumber)
    }
}