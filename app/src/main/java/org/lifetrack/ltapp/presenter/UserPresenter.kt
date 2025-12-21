package org.lifetrack.ltapp.presenter

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.lifetrack.ltapp.model.data.dclass.MenuItemData
import org.lifetrack.ltapp.model.data.dclass.ProfileInfo
import org.lifetrack.ltapp.model.data.dclass.menuListItems


class UserPresenter : ViewModel() {

    private val _profileInfo = MutableStateFlow(ProfileInfo())
    val profileInfo = _profileInfo.asStateFlow()

    val menuItems = mutableStateListOf<MenuItemData>()

    var appNotificationToggleState by mutableStateOf(false)
        private set
    var emailNotificationToggleState by mutableStateOf(false)
        private set
    var patientInfoConsentToggleState by mutableStateOf(false)
        private set

    init {
        menuItems.addAll(menuListItems)
        loadUserProfile()
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            val fetchedUserName = "Dr. Najma"
            val fetchedUserEmail = "najma@lifetrack.org"
            val userPhoneNumber = "+250788888888"
            val initials = fetchedUserName.split(" ")
                .filter { it.isNotBlank() }
                .map { it.first() }
                .joinToString("")

            _profileInfo.value = ProfileInfo(
                userName = fetchedUserName,
                userEmail = fetchedUserEmail,
                userInitials = initials,
                userPhoneNumber = userPhoneNumber
            )
        }
    }

    fun onMenuItemAction(navController: NavController, route: String) {
        navController.navigate(route) {
            launchSingleTop = true
        }
    }

    fun onUserNotificationsUpdate() {
        appNotificationToggleState = !appNotificationToggleState
    }

    fun onEmailNotificationsUpdate() {
        emailNotificationToggleState = !emailNotificationToggleState
    }

    fun onPatientInfoConsentUpdate(){
        patientInfoConsentToggleState = !patientInfoConsentToggleState
    }
}
