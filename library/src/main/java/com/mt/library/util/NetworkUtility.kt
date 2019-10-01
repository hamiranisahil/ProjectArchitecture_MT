package com.mt.library.util

import android.content.Context
import android.net.ConnectivityManager

class NetworkUtility(val context: Context) {

    fun isOnline(): Boolean {
        val connectivityManager: ConnectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo?.isConnected ?: false
    }

}