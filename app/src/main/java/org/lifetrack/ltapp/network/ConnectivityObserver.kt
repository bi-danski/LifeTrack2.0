package org.lifetrack.ltapp.network

import kotlinx.coroutines.flow.StateFlow


interface ConnectivityObserver {
//    enum class NetworkStatus { Available, Lost, Unavailable }
    val isConnected: StateFlow<Boolean>
}

