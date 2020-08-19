package com.demo.searchimagedemo.application

import android.app.Application
import com.demo.searchimagedemo.utils.Logger
import com.demo.searchimagedemo.di.AppComponent
import com.demo.searchimagedemo.di.DaggerAppComponent

class ImageApp :Application() {
    val appComponent: AppComponent by lazy { initAppComponent() }

    companion object {
        private val LOG_TAG = ImageApp::class.java.simpleName
    }

    override fun onCreate() {
        super.onCreate()
        Logger.debugLog(LOG_TAG, "OnCreate() call")
    }

    private fun initAppComponent(): AppComponent {
        return DaggerAppComponent.factory().create(applicationContext)
    }
}