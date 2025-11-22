package org.lifetrack.ltapp.presenter.mock

import org.lifetrack.ltapp.presenter.AlmaPresenter
import org.lifetrack.ltapp.ui.view.AlmaView

val almaMockPresenter = AlmaPresenter(
    view = object: AlmaView {
        override fun displayAlmaResponse() {

        }
        override fun showError() {

        }

        override fun showLoading() {

        }

        override fun hideLoading() {

        }
    }
)