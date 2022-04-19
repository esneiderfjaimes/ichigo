package com.red.code015.api.di

import com.red.code015.api.RiotGamesRetrofitDataSource
import com.red.code015.api.host.HostInterceptor
import com.red.code015.api.host.Platform
import com.red.code015.api.host.Region
import com.red.code015.api.retrofit.DataDragonRequest
import com.red.code015.api.retrofit.LoLRequest
import com.red.code015.api.retrofit.RiotRequest
import com.red.code015.data.RemoteRiotGamesDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class APIModule {

    // Host

    @Provides
    @Singleton
    fun regionHostProvider(): HostInterceptor<Region> = HostInterceptor(Region.AMERICAS)

    @Provides
    @Singleton
    fun platformHostProvider(): HostInterceptor<Platform> = HostInterceptor(Platform.LA1)

    // Requests

    @Provides
    @Singleton
    fun riotRequestProvider(host: HostInterceptor<Region>)
            : RiotRequest = RiotRequest(host)

    @Provides
    @Singleton
    fun lolRequestProvider(host: HostInterceptor<Platform>)
            : LoLRequest = LoLRequest(host)

    @Provides
    @Singleton
    fun dataDragonRequestProvider()
            : DataDragonRequest = DataDragonRequest()

    // Data Source

    @Provides
    @Singleton
    fun remoteSummonerDataSourceProvider(
        hostRegion: HostInterceptor<Region>,
        hostPlatform: HostInterceptor<Platform>,
        loLRequest: LoLRequest,
        riotRequest: RiotRequest,
        dataDragonRequest: DataDragonRequest,
    ): RemoteRiotGamesDataSource = RiotGamesRetrofitDataSource(
        hostRegion,
        hostPlatform,
        loLRequest,
        riotRequest,
        dataDragonRequest
    )

}

