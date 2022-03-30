package com.red.code015.database.di

import android.content.Context
import com.red.code015.data.LocalSummonerDataSource
import com.red.code015.database.IchigoDatabase
import com.red.code015.database.SummonerRoomDataSource
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
}
