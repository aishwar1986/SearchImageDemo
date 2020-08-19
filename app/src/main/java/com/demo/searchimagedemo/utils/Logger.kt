package com.demo.searchimagedemo.utils

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.demo.searchimagedemo.BuildConfig

/**
 * Utility class use for log print
 */
object Logger {

    @JvmStatic
    val DEFAULT_TAG = "APP_LOG"

    @JvmStatic
    fun debugLog(pTag: String? = DEFAULT_TAG, pMsg: String) {
        if (BuildConfig.DEBUG) {
            Log.d(pTag, pMsg)
        }
    }

    @JvmStatic
    fun showToast(pContext: Context, pMsg: String) {
        Toast.makeText(pContext, pMsg, Toast.LENGTH_SHORT).show()
    }
}