package com.thomas.apps.noteapp.utils.view

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

object NetWorkUtils {
    fun Context.isNetworkConnected(): Boolean {
        val connectivityManager = getSystemService(ConnectivityManager::class.java)
        val currentNetwork = connectivityManager.activeNetwork

        val caps = connectivityManager.getNetworkCapabilities(currentNetwork)

        val isNetworkConnected = caps?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)

        if (isNetworkConnected == true)
            return true
        return false
    }
}