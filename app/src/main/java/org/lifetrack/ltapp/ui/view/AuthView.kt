package org.lifetrack.ltapp.ui.view

interface AuthView {
    fun showLoading(isLoading: Boolean, msg: String? = null)
    fun onAuthSuccess()
    fun onAuthSuccessWithData(data: String)
    fun showError(msg: String)
}