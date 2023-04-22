package com.red.code015.database.di

import android.content.Context
import com.red.code015.data.LocalMasteriesDataSource
import com.red.code015.data.LocalSummonerDataSource
import com.red.code015.data.RedboxDataSource
import com.red.code015.database.DragonRedboxDataSource
import com.red.code015.database.MasteriesRoomDataSource
import com.red.code015.database.SummonerRoomDataSource
import com.red.code015.database.room.IchigoDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single<IchigoDatabase> { IchigoDatabase.getDatabase(this.androidContext()) }
    single<LocalSummonerDataSource> {
        val database = get<Context>()
        print("here")
        SummonerRoomDataSource(get())
    }
    single<LocalMasteriesDataSource> { MasteriesRoomDataSource(get()) }
    single<RedboxDataSource> { DragonRedboxDataSource(get()) }
}
