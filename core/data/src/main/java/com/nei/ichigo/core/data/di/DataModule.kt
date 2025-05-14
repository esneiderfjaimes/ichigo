package com.nei.ichigo.core.data.di

import com.nei.ichigo.core.data.repository.ChampionsRepository
import com.nei.ichigo.core.data.repository.OnlineChampionsRepository
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
}