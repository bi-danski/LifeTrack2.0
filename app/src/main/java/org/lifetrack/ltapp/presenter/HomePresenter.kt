package org.lifetrack.ltapp.presenter

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class HomePresenter: ViewModel() {

    var caroItemsCount by mutableIntStateOf(3)
        private set
}