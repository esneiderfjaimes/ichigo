package com.nei.ichigo.core.network.retrofit

import androidx.tracing.trace
import com.nei.ichigo.core.model.Champion
import com.nei.ichigo.core.model.ChampionDetail
import com.nei.ichigo.core.model.ProfileIcon
import com.nei.ichigo.core.model.RuneBranch
import com.nei.ichigo.core.network.Dispatcher
import com.nei.ichigo.core.network.IchigoDispatchers.IO
import com.nei.ichigo.core.network.IchigoNetworkDataSource
import com.nei.ichigo.core.network.model.BranchDto
import com.nei.ichigo.core.network.model.ChampionResponseServer
import com.nei.ichigo.core.network.model.ItemResponseServer
import com.nei.ichigo.core.network.model.PageResponseServer
import com.nei.ichigo.core.network.model.ProfileIconResponseServer
import com.nei.ichigo.core.network.model.RunesDto
import com.nei.ichigo.core.network.model.SummonerResponseServer
import com.nei.ichigo.core.network.model.asExternalModel
import com.nei.ichigo.core.network.model.asExternalModelDetail
import dagger.Lazy
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
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

    @NonCacheable
    @GET("/cdn/languages.json")
    suspend fun languages(): List<String>

    @NonCacheable
    @GET("/api/versions.json")
    suspend fun versions(): List<String>

    @GET("cdn/{version}/data/{lang}/champion.json")
    suspend fun champions(
        @Path("version") version: String,
        @Path("lang") lang: String,
    ): PageResponseServer<ChampionResponseServer>

    @GET("cdn/{version}/data/{lang}/champion/{champKey}.json")
    suspend fun champion(
        @Path("version") version: String,
        @Path("lang") lang: String,
        @Path("champKey") champKey: String,
    ): PageResponseServer<ChampionResponseServer>

    @GET("cdn/{version}/data/{lang}/profileicon.json")
    suspend fun profileIcons(
        @Path("version") version: String,
        @Path("lang") lang: String,
    ): PageResponseServer<ProfileIconResponseServer>

    @GET("cdn/{version}/data/{lang}/item.json")
    suspend fun items(
        @Path("version") version: String,
        @Path("lang") lang: String,
    ): PageResponseServer<ItemResponseServer>

    @GET("cdn/{version}/data/{lang}/summoner.json")
    suspend fun summonerSpells(
        @Path("version") version: String,
        @Path("lang") lang: String
    ): PageResponseServer<SummonerResponseServer>

    @GET("cdn/{version}/data/{lang}/runesReforged.json")
    suspend fun runes(
        @Path("version") version: String,
        @Path("lang") lang: String
    ): RunesDto
}

@Singleton
internal class RetrofitIchigoNetworkDataSource @Inject constructor(
    callFactory: Lazy<Call.Factory>,
    converterFactory: Lazy<Converter.Factory>,
    @param:Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,
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

    override suspend fun getLanguages() = withContext(ioDispatcher) {
        networkApi.languages().also {
            if (it.isEmpty()) {
                throw IllegalStateException("No languages found")
            }
        }
    }

    override suspend fun getVersions() = withContext(ioDispatcher) {
        networkApi.versions()
            .filter { !it.contains("lolpatch") }
            .ifEmpty { throw IllegalStateException("No versions found") }
    }

    override suspend fun getChampions(
        version: String,
        lang: String
    ): List<Champion> = withContext(ioDispatcher) {
        networkApi.champions(version, lang)
            .data!!.values
            .ifEmpty { throw IllegalStateException("No champions found") }
            .map(ChampionResponseServer::asExternalModel)
    }

    override suspend fun getChampion(
        version: String,
        lang: String,
        champKey: String
    ): ChampionDetail = withContext(ioDispatcher) {
        val responseServer = networkApi.champion(version, lang, champKey)
            .data?.get(champKey)
        requireNotNull(responseServer) { "Champion not found: $champKey" }
        responseServer.asExternalModelDetail()
    }

    override suspend fun getProfileIcons(
        version: String,
        lang: String
    ): List<ProfileIcon> = withContext(ioDispatcher) {
        networkApi.profileIcons(version, lang)
            .data!!.values
            .ifEmpty { throw IllegalStateException("No profile icons found") }
            .map(ProfileIconResponseServer::asExternalModel)
    }

    override suspend fun getItems(version: String, lang: String) = withContext(ioDispatcher) {
        networkApi.items(version, lang)
            .data!!
            .ifEmpty { throw IllegalStateException("No items found") }
            .map { (id, item) ->
                item.asExternalModel(id)
            }
    }

    override suspend fun getSummonerSpells(
        version: String, lang: String
    ) = withContext(ioDispatcher) {
        networkApi.summonerSpells(version, lang)
            .data!!.values
            .ifEmpty { throw IllegalStateException("No summoner spells found") }
            .map(SummonerResponseServer::asExternalModel)
    }

    override suspend fun getRunes(
        version: String,
        lang: String
    ): List<RuneBranch> = withContext(ioDispatcher) {
        networkApi.runes(version, lang)
            .ifEmpty { throw IllegalStateException("No runes found") }
            .map(BranchDto::asExternalModel)
    }
}
