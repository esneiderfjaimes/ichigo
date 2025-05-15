package com.nei.ichigo.core.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.nei.ichigo.core.datastore.IchigoPreferencesDataSource
import com.nei.ichigo.core.datastore.OfflineDataDragonPreferencesDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @IchigoPreferencesDataSource.DataStoreUserSettings
    @Provides
    @Singleton
    internal fun providesUserPreferencesDataStore(
        @ApplicationContext context: Context,
    ): DataStore<Preferences> = IchigoPreferencesDataSource.dataStoreBy(context)

    @OfflineDataDragonPreferencesDataSource.DataStoreDataDragon
    @Provides
    @Singleton
    internal fun providesDataDragonPreferencesDataStore(
        @ApplicationContext context: Context,
    ): DataStore<Preferences> = OfflineDataDragonPreferencesDataSource.dataStoreBy(context)
}
