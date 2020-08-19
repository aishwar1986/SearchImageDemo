package com.demo.searchimagedemo.utils

import android.content.Context
import android.net.ConnectivityManager


object Helper {
    /**
     * Helper method for internet connection check
     */
    fun isInternetOn(pContext: Context?): Boolean {
        val connectivityManager = pContext?.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        val activeNetworkInfo = connectivityManager?.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
}