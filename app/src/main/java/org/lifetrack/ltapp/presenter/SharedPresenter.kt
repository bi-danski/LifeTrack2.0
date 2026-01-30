package org.lifetrack.ltapp.presenter

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.lifetrack.ltapp.core.utility.makeAutoCall
import org.lifetrack.ltapp.core.utility.toLtSettings
import org.lifetrack.ltapp.model.data.dclass.MenuItemData
import org.lifetrack.ltapp.model.data.dclass.menuListItems
import org.lifetrack.ltapp.model.repository.PreferenceRepository

class SharedPresenter(
    private val prefRepository: PreferenceRepository
) : ViewModel() {

    val ltSettings = prefRepository.ltPreferences
        .map { it.toLtSettings() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = org.lifetrack.ltapp.model.data.dclass.LtSettings()
        )

    var version by mutableStateOf("2.0.0")
        private set

    val menuItems = mutableStateListOf<MenuItemData>().apply {
        addAll(menuListItems)
    }

    fun onUserNotificationsUpdate() {
        viewModelScope.launch {
            prefRepository.updateLTPreferences { current ->
                current.copy(appNotificationsEnabled = !current.appNotificationsEnabled)
            }
        }
    }

    fun onEmailNotificationsUpdate() {
        viewModelScope.launch {
            prefRepository.updateLTPreferences { current ->
                current.copy(appEmailNotificationsEnabled = !current.appEmailNotificationsEnabled)
            }
        }
    }

    fun onPatientInfoConsentUpdate() {
        viewModelScope.launch {
            prefRepository.updateLTPreferences { current ->
                current.copy(userPatientDataConsentEnabled = !current.userPatientDataConsentEnabled)
            }
        }
    }

    fun onAppAnimationsUpdate() {
        viewModelScope.launch {
            prefRepository.updateLTPreferences { current ->
                current.copy(appAnimationsEnabled = !current.appAnimationsEnabled)
            }
        }
    }

    fun onAppCarouselAnimationsUpdate(){
        viewModelScope.launch {
            prefRepository.updateLTPreferences { current ->
                current.copy(appCarouselAutoRotationEnabled = !current.appCarouselAutoRotationEnabled)
            }
        }
    }

    fun handleEmergencyCall(context: Context) {
        val emergencyNumber = "911"
        context.makeAutoCall(emergencyNumber)
    }
}