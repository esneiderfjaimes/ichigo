package com.red.code015.api.di

import com.red.code015.api.SummonerRetrofitDataSource
import com.red.code015.api.SummonersRequest
import com.red.code015.data.RemoteSummonerDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class APIModule {

    @Provides
    @Singleton
    @Named("baseUrl")
    fun baseUrlProvider(): String = "https://la1.api.riotgames.com/lol/summoner/v4/summoners/"

    @Provides
    fun summonerRequestProvider(
        @Named("baseUrl") baseUrl: String,
    ) = SummonersRequest(baseUrl)

    @Provides
    fun remoteSummonerDataSourceProvider(
        summonersRequest: SummonersRequest,
    ): RemoteSummonerDataSource = SummonerRetrofitDataSource(summonersRequest)

}

