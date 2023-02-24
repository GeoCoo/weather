package coo.apps.meteoray.managers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.annotation.NonNull

class ConnectionManager {

    var sIsConnected = true

    private var receiver: ConnectivityReceiver? = null

    @Synchronized
    fun register(context: Context, listener: ConnectivityListener) {
        receiver = ConnectivityReceiver(listener)

        context.registerReceiver(receiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    @Synchronized
    fun unregister(context: Context) {
        receiver?.apply {
            context.unregisterReceiver(this)
        }
    }

    interface ConnectivityListener {
        /**
         * Called on the UI thread when connection established (network is available).
         */
        fun onConnectionEstablished()

        /**
         * Called on the UI thread when connection lost (network is unavailable).
         */
        fun onConnectionLost()
    }

    inner class ConnectivityReceiver internal constructor(private val mConnectivityListener: ConnectivityListener?) :
        BroadcastReceiver() {

        init {
            if (mConnectivityListener == null) throw NullPointerException()
        }

        override fun onReceive(@NonNull context: Context, @NonNull intent: Intent) {
            if (ConnectivityManager.CONNECTIVITY_ACTION != intent.action) return

            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            val activeNetwork = cm.activeNetworkInfo
            val isConnected = activeNetwork != null && activeNetwork.isConnected

            if (isConnected != sIsConnected) {
                sIsConnected = isConnected

                if (isConnected) {
                    mConnectivityListener?.onConnectionEstablished()
                } else {
                    mConnectivityListener?.onConnectionLost()
                }
            }
        }
    }
}