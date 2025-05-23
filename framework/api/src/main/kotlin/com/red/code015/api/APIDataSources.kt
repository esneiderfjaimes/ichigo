package com.red.code015.api

import android.content.Context
import android.util.Log
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.red.code015.api.host.HostInterceptor
import com.red.code015.api.host.Platform
import com.red.code015.api.host.Region
import com.red.code015.api.host.toAPI
import com.red.code015.api.retrofit.*
import com.red.code015.data.APIDataSource
import com.red.code015.data.RemoteRiotGamesDataSource
import com.red.code015.domain.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.*
import java.util.concurrent.ExecutionException

class RiotGamesRetrofitDataSource(
    val context: Context,
    val remoteConfig: RemoteConfig,
    val firebaseRemoteConfig: FirebaseRemoteConfig,
    private val regionHost: HostInterceptor<Region>,
    private val platformHost: HostInterceptor<Platform>,
    private val loLRequest: LoLRequest,
    private val riotRequest: RiotRequest,
    private val dataDragonRequest: DataDragonRequest,
) : RemoteRiotGamesDataSource {

    companion object {
        const val tag = "$TAG_LOGS:RiotGames"
        var count = 0
    }

    init {
        count++
        Log.w(tag, "instance of RiotGamesRetrofitDataSource($count)")
    }

    // region Host Methods

    /**
     * updates the domain to which the query is made,
     * this domain corresponds to the riot games platform hosts
     * @param platformID platform identifier
     */
    override fun updateHost(platformID: PlatformID) {
        // If the host is the same to which you want to change, the change is not made
        if (platformHost.host.id == platformID) return
        Log.w(tag, "domain change from platform to $platformID")
        platformHost.host = platformID.toAPI() // updated platform host
        loLRequest.updateHost(platformHost) // update to all interceptors using the host
    }

    /**
     * updates the domain to which the query is made,
     * this domain corresponds to the riot games region hosts
     * @param regionID region identifier
     */
    override fun updateHost(regionID: RegionID) {
        // If the host is the same to which you want to change, the change is not made
        if (regionHost.host.id == regionID) return
        Log.w(tag, "domain change from platform to $regionID")
        regionHost.host = regionID.toAPI() // updated region host
        loLRequest.updateHost(platformHost) // update to all interceptors using the host
    }

    // endregion
    // region Profile

    override suspend fun profileBySummonerName(
        name: String,
    ): Profile = tryCoroutineScope("profileBySummonerName") {
        val summonerResp = loLRequest.service<SummonersService>().byName(name, remoteConfig.keyApi)
        summonerResp.toProfile(platformHost.host.id)
    }

    override suspend fun profileByRiotID(
        gameName: String,
        tagLine: String,
    ): Profile = tryCoroutineScope("profileByRiotID") {
        val accountResp =
            riotRequest.getService<AccountService>()
                .byRiotId(gameName, tagLine, remoteConfig.keyApi)
        val summonerResp =
            loLRequest.service<SummonersService>()
                .byPuuID(accountResp.puuid!!, remoteConfig.keyApi)
        summonerResp.toProfile(platformHost.host.id)
    }

    // endregion
    // region Summoner

    override suspend fun summonerByPuuID(
        puuID: String,
    ): Summoner = tryCoroutineScope("summonerByPuuID") {
        val summonerResp =
            async { loLRequest.service<SummonersService>().byPuuID(puuID, remoteConfig.keyApi) }
        val accountResp =
            async { riotRequest.service<AccountService>().byPuuId(puuID, remoteConfig.keyApi) }
        val leaguesResp =
            loLRequest.service<LeagueService>()
                .bySummoner(summonerResp.await().id!!, remoteConfig.keyApi)
        SummonerMapper.toDomain(
            platformID = platformHost.host.id,
            summoner = summonerResp.await(),
            account = accountResp.await(),
            leagues = leaguesResp
        )
    }

    override suspend fun summonerByName(
        name: String,
    ): Summoner = tryCoroutineScope("summonerByName") {
        val summonerResp = loLRequest.service<SummonersService>().byName(name, remoteConfig.keyApi)
        val account =
            async {
                riotRequest.service<AccountService>()
                    .byPuuId(summonerResp.puuId!!, remoteConfig.keyApi)
            }
        val leagues =
            async {
                loLRequest.service<LeagueService>()
                    .bySummoner(summonerResp.id!!, remoteConfig.keyApi)
            }

        SummonerMapper.toDomain(
            platformID = platformHost.host.id,
            summoner = summonerResp,
            account = account.await(),
            leagues = leagues.await()
        )
    }

    override suspend fun summonerByRiotId(
        gameName: String,
        tagLine: String,
    ): Summoner = tryCoroutineScope("summonerByRiotId") {
        val accountResp: AccountResponseServer =
            riotRequest.getService<AccountService>()
                .byRiotId(gameName, tagLine, remoteConfig.keyApi)
        val summonerResp: SummonerResponseServer =
            loLRequest.getService<SummonersService>()
                .byPuuID(accountResp.puuid!!, remoteConfig.keyApi)
        val leaguesResp: List<LeagueResponseServer> =
            loLRequest.getService<LeagueService>()
                .bySummoner(summonerResp.id!!, remoteConfig.keyApi)

        SummonerMapper.toDomain(
            platformID = platformHost.host.id,
            summoner = summonerResp,
            account = accountResp,
            leagues = leaguesResp
        )
    }

    override suspend fun masteryScores(summonerID: String)
            : Int = tryCoroutineScope("masteryScores") {
        loLRequest.getService<LoLService>().masteryScores(summonerID, remoteConfig.keyApi)
    }

    override suspend fun championMasteries(summonerID: String)
            : Masteries = tryCoroutineScope("championMasteries") {
        loLRequest.getService<LoLService>()
            .championMasteries(summonerID, remoteConfig.keyApi)
            .toDom2(summonerID, platformHost.host.id)
    }

    // endregion
    // region Data Dragon

    private var lastVersion: String? = null

    override suspend fun lastVersion(): String? = tryCoroutineScope("lastVersion") {
        try {
            if (lastVersion.isNullOrBlank())
                dataDragonRequest.getService<DataDragonService>().versions().first()
                    .also { lastVersion = it }
            else lastVersion
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun encyclopediaChampion(
        version: String,
        lang: String,
    ): EncyclopediaChampion = tryCoroutineScope("encyclopediaChampion") {
        val championsResp: ChampionsResponseServer =
            dataDragonRequest.getService<DataDragonService>()
                .champions(
                    version = version,
                    lang = lang
                )
        EncyclopediaChampion(version = championsResp.version!!,
            data = championsResp.data!!.map { it.value.toDomainItem() },
            dataSource = DataSource(DataSources.API, Date().time))
    }

    override suspend fun championsDetails(
        version: String,
        lang: String,
        filter: List<String>,
    ): List<Champion> {
        val listOf = mutableListOf<Champion>()
        filter.forEach {
            val championsResp =
                dataDragonRequest.getService<DataDragonService>()
                    .champion(
                        version = version,
                        lang = lang,
                        champKey = it
                    )
            championsResp.data?.get(it)
                ?.let { it1 -> listOf.add(it1.copy(version = "XD").toDomain()) }
        }
        return listOf
    }

    override suspend fun champion(version: String, lang: String, champKey: String)
            : Champion? = tryCoroutineScope("champion") {
        val championsResp =
            dataDragonRequest.getService<DataDragonService>()
                .champion(
                    version = version,
                    lang = lang,
                    champKey = champKey
                )
        championsResp.data?.get(champKey)?.toDomain()
    }

    override suspend fun championsRotations()
            : ChampionsRotation = tryCoroutineScope("championsRotations") {
        val championsResp: ChampionsRotationResponseServer =
            loLRequest.getService<LoLService>()
                .championsRotations(remoteConfig.keyApi)
        championsResp.toDomain()
    }

    // TODO: Create other data source, no compatibility with Retrofit
    override suspend fun <T> fetchApiKey(
        flowCollector: FlowCollector<T>,
        block: suspend FlowCollector<T>.() -> Unit,
    ): Unit = coroutineScope {
        Log.d(TAG, ">> fetchApiKey")
        var fetchCompleted = false
        firebaseRemoteConfig.setConfigSettingsAsync(
            FirebaseRemoteConfigSettings.Builder().setMinimumFetchIntervalInSeconds(0).build()
        )
        firebaseRemoteConfig.setDefaultsAsync(mapOf("RIOT_API_KEY" to "?"))
        val task = firebaseRemoteConfig.fetchAndActivate().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                FirebaseRemoteConfig.getInstance().apply {
                    remoteConfig.keyApi = getString("RIOT_API_KEY")
                    val sharedPref =
                        context.getSharedPreferences("api_preferences", Context.MODE_PRIVATE)
                    with(sharedPref.edit()) {
                        putString("RIOT_API_KEY", remoteConfig.keyApi)
                        apply()
                    }
                }
            }
            Log.i(TAG, "fetch (1) result:${task.result} keyApi:${remoteConfig.keyApi}")
            fetchCompleted = true
        }

        try {
            // Block on a task and get the result synchronously. This is generally done
            // when executing a task inside a separately managed background thread. Doing this
            // on the main (UI) thread can cause your application to become unresponsive.
            Log.w(TAG, "waiting for response from remote configuration")
            withContext(Dispatchers.Main) {
                val taskSync: Boolean = task.await()
                Log.w(TAG, "fetch (2) taskSync:$taskSync " +
                        "result:${task.result} keyApi:${remoteConfig.keyApi}")
            }
        } catch (e: ExecutionException) {
            // The Task failed, this is the same exception you'd get in a non-blocking
            // failure handler.
            // ...
            e.printStackTrace()
        } catch (e: InterruptedException) {
            // An interrupt occurred while waiting for the task to complete.
            // ...
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (fetchCompleted) {
            Log.w(TAG, "continue...")
            block.invoke(flowCollector)
            Log.d(TAG, "<< fetchApiKey")
        } else Log.e(TAG, "<< fetchApiKey")
    }

    override suspend fun <T> fetchApiKey2(block: suspend () -> T): T {
        Log.d(TAG, ">> fetchApiKey")
        var fetchCompleted = false
        firebaseRemoteConfig.setConfigSettingsAsync(
            FirebaseRemoteConfigSettings.Builder().setMinimumFetchIntervalInSeconds(0).build()
        )
        firebaseRemoteConfig.setDefaultsAsync(mapOf("RIOT_API_KEY" to "?"))
        val task = firebaseRemoteConfig.fetchAndActivate().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                FirebaseRemoteConfig.getInstance().apply {
                    remoteConfig.keyApi = getString("RIOT_API_KEY")
                    val sharedPref =
                        context.getSharedPreferences("api_preferences", Context.MODE_PRIVATE)
                    with(sharedPref.edit()) {
                        putString("RIOT_API_KEY", remoteConfig.keyApi)
                        apply()
                    }
                }
            }
            Log.i(TAG, "fetch (1) result:${task.result} keyApi:${remoteConfig.keyApi}")
            fetchCompleted = true
        }

        try {
            // Block on a task and get the result synchronously. This is generally done
            // when executing a task inside a separately managed background thread. Doing this
            // on the main (UI) thread can cause your application to become unresponsive.
            Log.w(TAG, "waiting for response from remote configuration")
            withContext(Dispatchers.Main) {
                val taskSync: Boolean = task.await()
                Log.w(TAG, "fetch (2) taskSync:$taskSync " +
                        "result:${task.result} keyApi:${remoteConfig.keyApi}")
            }
        } catch (e: ExecutionException) {
            // The Task failed, this is the same exception you'd get in a non-blocking
            // failure handler.
            // ...
            e.printStackTrace()
        } catch (e: InterruptedException) {
            // An interrupt occurred while waiting for the task to complete.
            // ...
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return block.invoke()
    }

    // endregion
}

class APIDataSources(
    private val ctx: Context,
    private val firebase: FirebaseRemoteConfig,
    private val remoteConfig: RemoteConfig,
) : APIDataSource {
    override suspend fun <T> fetchApiKey(
        flowCollector: FlowCollector<T>,
        block: suspend FlowCollector<T>.() -> Unit,
    ): Unit = coroutineScope {
        Log.d(TAG, ">> fetchApiKey")
        var fetchCompleted = false
        firebase.setConfigSettingsAsync(
            FirebaseRemoteConfigSettings.Builder().setMinimumFetchIntervalInSeconds(0).build()
        )
        firebase.setDefaultsAsync(mapOf("RIOT_API_KEY" to "?"))
        val task = firebase.fetchAndActivate().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                FirebaseRemoteConfig.getInstance().apply {
                    remoteConfig.keyApi = getString("RIOT_API_KEY")
                    val sharedPref =
                        ctx.getSharedPreferences("api_preferences", Context.MODE_PRIVATE)
                    with(sharedPref.edit()) {
                        putString("RIOT_API_KEY", remoteConfig.keyApi)
                        apply()
                    }
                }
            }
            Log.i(TAG, "fetch (1) result:${task.result} keyApi:${remoteConfig.keyApi}")
            fetchCompleted = true
        }

        try {
            // Block on a task and get the result synchronously. This is generally done
            // when executing a task inside a separately managed background thread. Doing this
            // on the main (UI) thread can cause your application to become unresponsive.
            Log.w(TAG, "waiting for response from remote configuration")
            withContext(Dispatchers.Main) {
                val taskSync: Boolean = task.await()
                Log.w(TAG, "fetch (2) taskSync:$taskSync " +
                        "result:${task.result} keyApi:${remoteConfig.keyApi}")
            }
        } catch (e: ExecutionException) {
            // The Task failed, this is the same exception you'd get in a non-blocking
            // failure handler.
            // ...
            e.printStackTrace()
        } catch (e: InterruptedException) {
            // An interrupt occurred while waiting for the task to complete.
            // ...
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (fetchCompleted) {
            Log.w(TAG, "continue...")
            block.invoke(flowCollector)
            Log.d(TAG, "<< fetchApiKey")
        } else Log.e(TAG, "<< fetchApiKey")
    }

    override suspend fun <T> fetchApiKey(block: suspend () -> T): T {
        Log.d(TAG, ">> fetchApiKey")
        firebase.setConfigSettingsAsync(
            FirebaseRemoteConfigSettings.Builder().setMinimumFetchIntervalInSeconds(0).build()
        )
        firebase.setDefaultsAsync(mapOf("RIOT_API_KEY" to "?"))
        val task = firebase.fetchAndActivate().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                FirebaseRemoteConfig.getInstance().apply {
                    remoteConfig.keyApi = getString("RIOT_API_KEY")
                    val sharedPref =
                        ctx.getSharedPreferences("api_preferences", Context.MODE_PRIVATE)
                    with(sharedPref.edit()) {
                        putString("RIOT_API_KEY", remoteConfig.keyApi)
                        apply()
                    }
                }
            }
            Log.i(TAG, "fetch (1) result:${task.result} keyApi:${remoteConfig.keyApi}")
        }

        try {
            // Block on a task and get the result synchronously. This is generally done
            // when executing a task inside a separately managed background thread. Doing this
            // on the main (UI) thread can cause your application to become unresponsive.
            Log.w(TAG, "waiting for response from remote configuration")
            withContext(Dispatchers.Main) {
                val taskSync: Boolean = task.await()
                Log.w(TAG, "fetch (2) taskSync:$taskSync " +
                        "result:${task.result} keyApi:${remoteConfig.keyApi}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return block.invoke()
    }

}