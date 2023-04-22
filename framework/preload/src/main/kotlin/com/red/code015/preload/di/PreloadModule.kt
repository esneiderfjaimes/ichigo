package com.red.code015.preload.di

import com.google.gson.Gson
import com.red.code015.data.PreloadDataSource
import com.red.code015.preload.DataDragonAssetsDataSource
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val preloadModule = module {
    single { Gson() }
    single<PreloadDataSource> {
        DataDragonAssetsDataSource(
            application = this.androidApplication(),
            gson = get()
        )
    }
}