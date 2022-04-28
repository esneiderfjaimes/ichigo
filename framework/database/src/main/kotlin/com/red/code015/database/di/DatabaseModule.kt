package com.red.code015.database.di

import android.app.Application
import android.content.Context
import com.google.gson.Gson
import com.red.code015.data.LocalSummonerDataSource
import com.red.code015.data.RedboxDataSource
import com.red.code015.database.DragonRedboxDataSource
import com.red.code015.database.SummonerRoomDataSource
import com.red.code015.database.redbox.Redbox
import com.red.code015.database.room.IchigoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun databaseProvider(@ApplicationContext context: Context) = IchigoDatabase.getDatabase(context)

    @Provides
    fun localSummonerDataSourceProvider(
        database: IchigoDatabase,
    ): LocalSummonerDataSource = SummonerRoomDataSource(database)

    @Provides
    fun dragonRedboxDataSourceProvider(
        redbox: Redbox,
    ): RedboxDataSource = DragonRedboxDataSource(redbox)
}
