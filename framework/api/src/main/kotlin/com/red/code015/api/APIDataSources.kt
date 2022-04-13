package com.red.code015.api

import android.util.Log
import com.red.code015.api.host.HostInterceptor
import com.red.code015.api.host.Platform
import com.red.code015.api.host.Region
import com.red.code015.api.host.toAPI
import com.red.code015.api.retrofit.*
import com.red.code015.data.ForbiddenException
import com.red.code015.data.RemoteRiotGamesDataSource
import com.red.code015.domain.PlatformID
import com.red.code015.domain.Profile
import com.red.code015.domain.RegionID
import com.red.code015.domain.Summoner
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import retrofit2.HttpException
import kotlin.system.measureTimeMillis

class RiotGamesRetrofitDataSource(
    private val regionHost: HostInterceptor<Region>,
    private val platformHost: HostInterceptor<Platform>,
    private val loLRequest: LoLRequest,
    private val riotRequest: RiotRequest,
) : RemoteRiotGamesDataSource {

    private val platformID = platformHost.host.id

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
    ): Profile = coroutineScope {
        var profile: Profile
        val time = measureTimeMillis {
            val summonerResp = loLRequest.service<SummonersService>().byName(name)
            profile = summonerResp.toProfile(platformHost.host.id)
        }
        Log.d(tag, "profileBySummonerName: time:$time, profile:$profile")
        return@coroutineScope profile
    }

    override suspend fun profileByRiotID(
        gameName: String,
        tagLine: String,
    ): Profile = coroutineScope {
        var profile: Profile
        val time = measureTimeMillis {
            val accountResp = riotRequest.getService<AccountService>().byRiotId(gameName, tagLine)
            val summonerResp = loLRequest.service<SummonersService>().byPuuID(accountResp.puuid)
            profile = summonerResp.toProfile(platformHost.host.id)
        }
        Log.d(tag, "profileByRiotID: time:$time, profile:$profile")
        return@coroutineScope profile
    }

    // endregion
    // region Summoner

    override suspend fun summonerByPuuID(puuID: String): Summoner = coroutineScope {
        var summoner: Summoner
        val time = measureTimeMillis {
            val summonerResp = async { loLRequest.service<SummonersService>().byPuuID(puuID) }
            val account = async { riotRequest.service<AccountService>().byPuuId(puuID) }
            val leagues = loLRequest.service<LeagueService>().bySummoner(summonerResp.await().id)
            summoner = SummonerMapper.toDomain(
                platformID = platformID,
                summoner = summonerResp.await(),
                account = account.await(),
                leagues = leagues
            )
        }
        Log.d("RiotGamesRetrofit", "summonerByName: time:$time")
        return@coroutineScope summoner
    }

    override suspend fun summonerByName(name: String): Summoner = coroutineScope {
        try {
            var summoner: Summoner
            val time = measureTimeMillis {
                val summonerResp = loLRequest.service<SummonersService>().byName(name)
                val account =
                    async { riotRequest.service<AccountService>().byPuuId(summonerResp.puuId) }
                val leagues =
                    async { loLRequest.service<LeagueService>().bySummoner(summonerResp.id) }
                summoner = SummonerMapper.toDomain(
                    platformID = platformID,
                    summoner = summonerResp,
                    account = account.await(),
                    leagues = leagues.await()
                )
            }
            Log.d("RiotGamesRetrofit", "summonerByName: time:$time")
            return@coroutineScope summoner
        } catch (e: Exception) {
            if (e is HttpException) {
                if (e.code() == 403) {
                    throw ForbiddenException(e.message())
                }
                Log.e(tag, "summonerByName: Exception:${e.message()}")
            }
            throw e
        }
    }

    override suspend fun summonerByRiotId(gameName: String, tagLine: String): Summoner =
        coroutineScope {
            var summoner: Summoner
            val time = measureTimeMillis {
                val account = riotRequest.getService<AccountService>().byRiotId(gameName, tagLine)
                val summonerResp = loLRequest.getService<SummonersService>().byPuuID(account.puuid)
                val leagues = loLRequest.getService<LeagueService>().bySummoner(summonerResp.id)
                summoner = SummonerMapper.toDomain(
                    platformID = platformID,
                    summoner = summonerResp,
                    account = account,
                    leagues = leagues
                )
            }
            Log.d("RiotGamesRetrofit", "summonerByName: time:$time")
            return@coroutineScope summoner
        }

    // endregion

}