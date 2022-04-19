package com.red.code015.preload.di

import android.app.Application
import android.content.Context
import com.google.gson.Gson
import com.red.code015.data.PreloadDataSource
import com.red.code015.preload.DataDragonAssetsDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class PreloadModule {

    @Provides
    @Singleton
    fun databaseProvider(@ApplicationContext context: Context): Context = context

    @Provides
    @Singleton
    fun gsonProvide() = Gson()

    // Data Source

    @Provides
    @Singleton
    fun preloadDataSourceProvider(
        application: Application,
        gson: Gson,
    ): PreloadDataSource = DataDragonAssetsDataSource(
        application,
        gson
    )
}
