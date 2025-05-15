package com.nei.ichigo.core.data.di

import com.nei.ichigo.core.data.repository.ChampionsRepository
import com.nei.ichigo.core.data.repository.OfflineUserSettingsRepository
import com.nei.ichigo.core.data.repository.OnlineChampionsRepository
import com.nei.ichigo.core.data.repository.UserSettingsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal object DataModule {

    @Provides
    fun provideChampionsRepository(repository: OnlineChampionsRepository): ChampionsRepository {
        return repository
    }

    @Provides
    fun provideUserSettingsRepository(repository: OfflineUserSettingsRepository): UserSettingsRepository {
        return repository
    }
}