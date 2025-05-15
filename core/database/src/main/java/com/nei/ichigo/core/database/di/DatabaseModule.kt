package com.nei.ichigo.core.database.di

import android.content.Context
import androidx.room.Room
import com.nei.ichigo.core.database.IchigoDatabase
import com.nei.ichigo.core.database.dao.ChampionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {
    @Provides
    @Singleton
    fun providesNiaDatabase(
        @ApplicationContext context: Context,
    ): IchigoDatabase = Room.databaseBuilder(
        context,
        IchigoDatabase::class.java,
        "ichigo-database",
    ).build()

    @Provides
    fun providesChampionDao(
        database: IchigoDatabase,
    ): ChampionDao = database.championDao()
}
