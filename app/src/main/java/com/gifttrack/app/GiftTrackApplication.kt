package com.gifttrack.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GiftTrackApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize application components here
    }
}
