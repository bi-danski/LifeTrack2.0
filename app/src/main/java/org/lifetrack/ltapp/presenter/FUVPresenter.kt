package org.lifetrack.ltapp.presenter

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.lifetrack.ltapp.model.data.dummyHospitalData

class FUVPresenter: ViewModel() {
    private val _hospitalInfo = MutableStateFlow(dummyHospitalData)
    val hospitalData = _hospitalInfo.asStateFlow()

}