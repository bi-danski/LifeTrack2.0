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
import org.lifetrack.ltapp.core.localization.LocaleManager
import org.lifetrack.ltapp.model.data.dclass.MenuItemData
import org.lifetrack.ltapp.model.data.dclass.menuListItems
import org.lifetrack.ltapp.model.repository.PreferenceRepository
import org.lifetrack.ltapp.utility.makeAutoCall
import org.lifetrack.ltapp.utility.toLtSettings

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

    val currentLanguage = prefRepository.ltPreferences
        .map { it.preferredLanguage }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = "en"
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

    fun updateLanguage(languageCode: String) {
        // Fast path: write a synchronous preference for attachBaseContext and startup usage
        LocaleManager.setPreferredLanguage(languageCode)

        // Persist the change in DataStore asynchronously
        viewModelScope.launch {
            prefRepository.updateLTPreferences { current ->
                current.copy(preferredLanguage = languageCode)
            }
        }
    }

    fun handleEmergencyCall(context: Context) {
        val emergencyNumber = "911"
        context.makeAutoCall(emergencyNumber)
    }
}