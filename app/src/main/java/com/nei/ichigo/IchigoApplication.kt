package com.nei.ichigo

import android.app.Application
import android.os.StrictMode
import com.nei.ichigo.core.designsystem.BuildConfig
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class IchigoApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(
                StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .penaltyFlashScreen()
                    .build()
            )

            StrictMode.setVmPolicy(
                StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build()
            )
        }
    }
}