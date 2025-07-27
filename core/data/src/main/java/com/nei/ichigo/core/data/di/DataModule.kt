package com.nei.ichigo.core.data.di

import com.nei.ichigo.core.data.repository.AppConfigRepository
import com.nei.ichigo.core.data.repository.AppConfigRepositoryImpl
import com.nei.ichigo.core.data.repository.DDragonRepository
import com.nei.ichigo.core.data.repository.OfflineUserSettingsRepository
import com.nei.ichigo.core.data.repository.OnlineRealmRepository
import com.nei.ichigo.core.data.repository.UserSettingsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DataModule {

    @Provides
    @Singleton
    fun provideChampionsRepository(repository: OnlineRealmRepository): DDragonRepository {
        return repository
    }

    @Provides
    fun provideUserSettingsRepository(repository: OfflineUserSettingsRepository): UserSettingsRepository {
        return repository
    }

    @Provides
    fun provideAppConfigRepository(repository: AppConfigRepositoryImpl): AppConfigRepository {
        return repository
    }
}