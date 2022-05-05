package com.red.code015.api.di

import android.content.Context
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.red.code015.api.APIDataSources
import com.red.code015.api.RiotGamesRetrofitDataSource
import com.red.code015.api.host.HostInterceptor
import com.red.code015.api.host.Platform
import com.red.code015.api.host.Region
import com.red.code015.api.retrofit.DataDragonRequest
import com.red.code015.api.retrofit.LoLRequest
import com.red.code015.api.retrofit.RiotRequest
import com.red.code015.data.APIDataSource
import com.red.code015.data.RemoteRiotGamesDataSource
import com.red.code015.domain.RemoteConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class APIModule {

    // Remote configuration

    @Provides
    @Singleton
    fun remoteConfigProvider(@ApplicationContext context: Context): RemoteConfig = context.run {
        val sharedPref = getSharedPreferences("api_preferences", Context.MODE_PRIVATE)
        val riotApiKey = sharedPref.getString("RIOT_API_KEY", null) ?: "NO API FOUND"
        RemoteConfig(riotApiKey)
    }

    @Provides
    @Singleton
    fun firebaseRemoteConfigProvider() = FirebaseRemoteConfig.getInstance()

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
        @ApplicationContext context: Context,
        remoteConfig: RemoteConfig,
        firebaseRemoteConfig: FirebaseRemoteConfig,
        hostRegion: HostInterceptor<Region>,
        hostPlatform: HostInterceptor<Platform>,
        loLRequest: LoLRequest,
        riotRequest: RiotRequest,
        dataDragonRequest: DataDragonRequest,
    ): RemoteRiotGamesDataSource = RiotGamesRetrofitDataSource(
        context,
        remoteConfig = remoteConfig,
        firebaseRemoteConfig = firebaseRemoteConfig,
        hostRegion,
        hostPlatform,
        loLRequest,
        riotRequest,
        dataDragonRequest
    )

    @Provides
    @Singleton
    fun remoteAPIDataSourceProvider(
        @ApplicationContext context: Context,
        remoteConfig: RemoteConfig,
        firebaseRemoteConfig: FirebaseRemoteConfig,
    ): APIDataSource = APIDataSources(
        context,
        remoteConfig = remoteConfig,
        firebase = firebaseRemoteConfig,
    )
}

