package com.red.code015

import android.app.Application
import androidx.room.Room
import com.red.code015.api.di.apiModule
import com.red.code015.data.di.appModule
import com.red.code015.data.di.userCaseModule
import com.red.code015.data.di.viewModelModule
import com.red.code015.database.di.databaseModule
import com.red.code015.database.room.IchigoDatabase
import com.red.code015.preload.di.preloadModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        val database = IchigoDatabase.getDatabase(this)
        startKoin {
            // Log Koin into Android logger
            androidLogger()
            // Reference Android context
            androidContext(this@App)
            // Load modules
            modules(
                appModule,
                preloadModule,
                apiModule,
                databaseModule,
                viewModelModule,
                userCaseModule
            )
        }

    }
}