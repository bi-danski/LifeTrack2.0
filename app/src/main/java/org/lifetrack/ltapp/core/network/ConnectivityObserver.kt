package org.lifetrack.ltapp.core.network


import kotlinx.coroutines.flow.Flow


interface ConnectivityObserver {
    val status: Flow<NetworkStatus>
    enum class NetworkStatus { Available, Lost, Unavailable }
}

