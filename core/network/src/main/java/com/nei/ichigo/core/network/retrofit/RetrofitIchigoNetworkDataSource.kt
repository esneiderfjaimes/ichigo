package com.nei.ichigo.core.network.retrofit

import androidx.tracing.trace
import com.nei.ichigo.core.model.Champion
import com.nei.ichigo.core.network.IchigoNetworkDataSource
import com.nei.ichigo.core.network.model.ChampionsResponseServer
import com.nei.ichigo.core.network.model.asExternalModel
import dagger.Lazy
import okhttp3.Call
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import javax.inject.Inject
import javax.inject.Singleton

private const val BASE_URL = "https://ddragon.leagueoflegends.com/"

/**
 * Retrofit API declaration for Data Dragon Network API
 */
private interface DataDragonApi {

    @GET("/cdn/languages.json")
    suspend fun languages(): List<String>

    @GET("/api/versions.json")
    suspend fun versions(): List<String>

    @GET("cdn/{version}/data/{lang}/champion.json")
    suspend fun champions(
        @Path("version") version: String,
        @Path("lang") lang: String,
    ): ChampionsResponseServer

    @GET("cdn/{version}/data/{lang}/champion/{champKey}.json")
    suspend fun champion(
        @Path("version") version: String,
        @Path("lang") lang: String,
        @Path("champKey") champKey: String,
    ): ChampionsResponseServer
}

@Singleton
internal class RetrofitIchigoNetworkDataSource @Inject constructor(
    callFactory: Lazy<Call.Factory>,
    converterFactory: Lazy<Converter.Factory>,
) : IchigoNetworkDataSource {
    private val networkApi = trace("RetrofitNiaNetwork") {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            // We use callFactory lambda here with dagger.Lazy<Call.Factory>
            // to prevent initializing OkHttp on the main thread.
            .callFactory { callFactory.get().newCall(it) }
            .addConverterFactory(converterFactory.get())
            .build()
            .create(DataDragonApi::class.java)
    }

    override suspend fun getLanguages(): List<String> {
        return networkApi.languages()
    }

    override suspend fun getVersions(): List<String> {
        return networkApi.versions().filter { !it.contains("lolpatch") }
    }

    override suspend fun getChampions(version: String, lang: String): List<Champion> {
        return networkApi.champions(version, lang).data!!.values.map { it.asExternalModel() }
    }
}
