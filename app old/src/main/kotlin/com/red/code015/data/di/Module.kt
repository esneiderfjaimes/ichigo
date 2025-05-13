package com.red.code015.data.di

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.red.code015.data.AppSettings
import com.red.code015.data.AppSettingsSerializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class Module {

    private val Context.dataStore: DataStore<AppSettings>
            by dataStore("app-settings.json", AppSettingsSerializer)

    @Provides
    @Singleton
    fun dataStore(app: Application): DataStore<AppSettings> = app.applicationContext.dataStore

}