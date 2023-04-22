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
import org.koin.core.qualifier.named
import org.koin.dsl.module

val apiModule = module {

    // Remote configuration

    single {
        val sharedPref =
            get<Context>().getSharedPreferences("api_preferences", Context.MODE_PRIVATE)
        val riotApiKey = sharedPref.getString("RIOT_API_KEY", null) ?: "NO API FOUND"
        RemoteConfig(riotApiKey)
    }

    single { FirebaseRemoteConfig.getInstance() }

    // Host

    single(named("hostRegion")) { HostInterceptor(Region.AMERICAS) }

    single(named("hostPlatform")) { HostInterceptor(Platform.LA1) }

    // Requests

    single { RiotRequest(get(named("hostRegion"))) }

    single { LoLRequest(get(named("hostPlatform"))) }

    single { DataDragonRequest() }

    // Data Source

    single<RemoteRiotGamesDataSource> {
        RiotGamesRetrofitDataSource(
            get(),
            remoteConfig = get(),
            firebaseRemoteConfig = get(),
            regionHost = get(named("hostRegion")),
            platformHost = get(named("hostPlatform")),
            loLRequest = get(),
            riotRequest = get(),
            dataDragonRequest = get()
        )
    }

    single<APIDataSource> {
        APIDataSources(
            get(),
            remoteConfig = get(),
            firebase = get()
        )
    }
}
