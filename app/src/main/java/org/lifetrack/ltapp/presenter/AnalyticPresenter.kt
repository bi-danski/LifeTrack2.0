package org.lifetrack.ltapp.presenter

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableStateSetOf
import androidx.lifecycle.ViewModel
import org.lifetrack.ltapp.model.data.Prescription
import org.lifetrack.ltapp.model.data.bPressureData
import org.lifetrack.ltapp.model.data.dLabTests
import org.lifetrack.ltapp.model.data.dPatient
import org.lifetrack.ltapp.model.data.dPrescriptions

class AnalyticPresenter: ViewModel() {

    var dummyBpData = mutableStateOf(bPressureData)
    private set

    var dummyPatient = mutableStateOf(dPatient)
    private set

    var dummyLabTests = mutableStateOf(dLabTests)
    private set

    var dummyPrescriptions: MutableList<Prescription> = mutableStateListOf()
    private set

    init {
        if (dummyPrescriptions.isEmpty()){
            dummyPrescriptions.addAll(dPrescriptions)
        }
    }
}