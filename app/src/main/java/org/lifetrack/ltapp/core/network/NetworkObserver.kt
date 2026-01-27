package org.lifetrack.ltapp.core.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NetworkObserver(
    context: Context,
    private val koinScope: CoroutineScope
) : ConnectivityObserver {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    override val status: StateFlow<ConnectivityObserver.NetworkStatus> = callbackFlow {
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) { launch { send(ConnectivityObserver.NetworkStatus.Available) } }
            override fun onLost(network: Network) { launch { send(ConnectivityObserver.NetworkStatus.Lost) } }
            override fun onUnavailable() { launch { send(ConnectivityObserver.NetworkStatus.Unavailable) } }
        }

        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
            .build()

        connectivityManager.registerNetworkCallback(request, callback)

        val initialState = if (isConnected()) ConnectivityObserver.NetworkStatus.Available
        else ConnectivityObserver.NetworkStatus.Lost
        trySend(initialState)

        awaitClose { connectivityManager.unregisterNetworkCallback(callback) }
    }.stateIn(
        scope = koinScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ConnectivityObserver.NetworkStatus.Unavailable
    )

    private fun isConnected(): Boolean {
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }
}