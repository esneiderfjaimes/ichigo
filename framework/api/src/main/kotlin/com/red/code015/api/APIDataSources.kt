package com.red.code015.api

import android.util.Log
import com.red.code015.api.host.HostInterceptor
import com.red.code015.api.host.Platform
import com.red.code015.api.host.Region
import com.red.code015.api.host.toAPI
import com.red.code015.api.retrofit.*
import com.red.code015.data.RemoteRiotGamesDataSource
import com.red.code015.domain.*
import kotlinx.coroutines.async
import java.util.*

class RiotGamesRetrofitDataSource(
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
        val summonerResp = loLRequest.service<SummonersService>().byName(name)
        summonerResp.toProfile(platformHost.host.id)
    }

    override suspend fun profileByRiotID(
        gameName: String,
        tagLine: String,
    ): Profile = tryCoroutineScope("profileByRiotID") {
        val accountResp = riotRequest.getService<AccountService>().byRiotId(gameName, tagLine)
        val summonerResp = loLRequest.service<SummonersService>().byPuuID(accountResp.puuid)
        summonerResp.toProfile(platformHost.host.id)
    }

    // endregion
    // region Summoner

    override suspend fun summonerByPuuID(
        puuID: String,
    ): Summoner = tryCoroutineScope("summonerByPuuID") {
        val summonerResp = async { loLRequest.service<SummonersService>().byPuuID(puuID) }
        val accountResp = async { riotRequest.service<AccountService>().byPuuId(puuID) }
        val leaguesResp =
            loLRequest.service<LeagueService>().bySummoner(summonerResp.await().id)
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
        val summonerResp = loLRequest.service<SummonersService>().byName(name)
        val account =
            async { riotRequest.service<AccountService>().byPuuId(summonerResp.puuId) }
        val leagues =
            async { loLRequest.service<LeagueService>().bySummoner(summonerResp.id) }

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
            riotRequest.getService<AccountService>().byRiotId(gameName, tagLine)
        val summonerResp: SummonerResponseServer =
            loLRequest.getService<SummonersService>().byPuuID(accountResp.puuid)
        val leaguesResp: List<LeagueResponseServer> =
            loLRequest.getService<LeagueService>().bySummoner(summonerResp.id)

        SummonerMapper.toDomain(
            platformID = platformHost.host.id,
            summoner = summonerResp,
            account = accountResp,
            leagues = leaguesResp
        )
    }

    // endregion
    // region Data Dragon

    override suspend fun encyclopediaChampion(
        lang: String,
    ): EncyclopediaChampion = tryCoroutineScope("encyclopediaChampion") {
        val championsResp: ChampionsResponseServer =
            dataDragonRequest.getService<DataDragonService>()
                .champions(
                    version = "12.7.1", // TODO: always use the latest version
                    lang = lang
                )
        EncyclopediaChampion(version = championsResp.version,
            data = championsResp.data.toList(),
            dataSource = DataSource(DataSources.API, Date().time))
    }

    // endregion
}