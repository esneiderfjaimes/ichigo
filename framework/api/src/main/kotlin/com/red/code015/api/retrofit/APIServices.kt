package com.red.code015.api.retrofit

import com.red.code015.api.APIConstants.KEY
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

const val SERVICE_SUMMONER: String = "lol/summoner/v4"
const val SERVICE_LEAGUE: String = "lol/league/v4"
const val SERVICE_ACCOUNT: String = "riot/account/v1"

interface SummonersService : LoLAPIs {

    @GET("$SERVICE_SUMMONER/summoners/by-name/{summonerName}?api_key=$KEY")
    suspend fun byName(
        @Path("summonerName") name: String,
    ): SummonerResponseServer

    @GET("$SERVICE_SUMMONER/summoners/by-puuid/{encryptedPUUID}?api_key=$KEY")
    suspend fun byPuuID(
        @Path("encryptedPUUID") puuID: String,
    ): SummonerResponseServer
}

interface LeagueService : LoLAPIs {

    @GET("$SERVICE_LEAGUE/entries/by-summoner/{summonerId}?api_key=$KEY")
    suspend fun bySummoner(
        @Path("summonerId") summonerId: String,
    ): List<LeagueResponseServer>

}

interface AccountService : RiotAPI {

    @GET("$SERVICE_ACCOUNT/accounts/by-puuid/{puuid}?api_key=$KEY")
    suspend fun byPuuId(@Path("puuid") puuid: String): AccountResponseServer

    @GET("$SERVICE_ACCOUNT/accounts/by-riot-id/{gameName}/{tagLine}?api_key=$KEY")
    suspend fun byRiotId(
        @Path("gameName") gameName: String,
        @Path("tagLine") tagLine: String,
    ): AccountResponseServer

}

interface LoLService : LoLAPIs {

    // CHAMPION-V3 API
    @GET("lol/platform/v3/champion-rotations")
    suspend fun championsRotations(
        @Header("X-Riot-Token") riotToken: String,
    ): ChampionsRotationResponseServer


    // CHAMPION-MASTERY-V4

    @GET("/lol/champion-mastery/v4/scores/by-summoner/{summonerId}")
    suspend fun masteryScores(
        @Path("summonerId") summonerId: String,
        @Header("X-Riot-Token") riotToken: String,
    ): Int

    @GET("/lol/champion-mastery/v4/champion-masteries/by-summoner/{summonerId}")
    suspend fun championMasteries(
        @Path("summonerId") summonerId: String,
        @Header("X-Riot-Token") riotToken: String,
    ): List<MasteriesResponseServer>
}

interface DataDragonService : DataDragonAPI {

    @GET("cdn/{version}/data/{lang}/champion.json")
    suspend fun champions(
        @Path("version") version: String,
        @Path("lang") lang: String,
    ): ChampionsResponseServer

}