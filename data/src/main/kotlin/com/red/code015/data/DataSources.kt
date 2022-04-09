package com.red.code015.data

import com.red.code015.domain.*

interface RemoteRiotGamesDataSource {

    fun updateHost(platformID: PlatformID)
    fun updateHost(regionID: RegionID)

    suspend fun profileBySummonerName(name: String): Profile
    suspend fun profileByRiotID(gameName: String, tagLine: String): Profile

    suspend fun summonerByPuuID(puuID: String): Summoner
    suspend fun summonerByName(name: String): Summoner
    suspend fun summonerByRiotId(gameName: String, tagLine: String): Summoner

}

interface LocalSummonerDataSource {
    suspend fun insertSummoner(summoner: Summoner)
    suspend fun getLastCheckDateByPuuID(puuID: String): Long?
    suspend fun getLastCheckDateByName(name: String): Long?
    suspend fun getLastCheckDateByRiotId(gameName: String, tagLine: String): Long?
    suspend fun summonerByPuuID(puuID: String): Summoner
    suspend fun summonerByName(name: String): Summoner
    suspend fun summonerByRiotId(gameName: String, tagLine: String): Summoner
}
