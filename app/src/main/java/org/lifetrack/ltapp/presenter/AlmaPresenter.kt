package org.lifetrack.ltapp.presenter

import androidx.lifecycle.ViewModel
import kotlinx.serialization.descriptors.StructureKind
import org.lifetrack.ltapp.ui.view.AlmaView
import kotlin.properties.Delegates

class AlmaPresenter(
    view : AlmaView
): ViewModel() {
    fun sendMessage(message: String){
        // Mdagi mdagi tu
    }

//    fun attachView(view2Attach: StructureKind.OBJECT){
//        view = view2Attach as AlmaView
//    }
}