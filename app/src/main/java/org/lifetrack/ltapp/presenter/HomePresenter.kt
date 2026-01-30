package org.lifetrack.ltapp.presenter

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class HomePresenter: ViewModel() {

    var autoRotate2NextCard by mutableStateOf(false)
    private set

    var caroItemsCount by mutableIntStateOf(3)
        private set
}