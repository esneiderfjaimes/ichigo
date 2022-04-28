package com.red.code015.data

import com.red.code015.domain.*
import kotlinx.coroutines.flow.FlowCollector
import kotlin.experimental.ExperimentalTypeInference

interface RemoteRiotGamesDataSource {

    fun updateHost(platformID: PlatformID)
    fun updateHost(regionID: RegionID)

    suspend fun profileBySummonerName(name: String): Profile
    suspend fun profileByRiotID(gameName: String, tagLine: String): Profile

    suspend fun summonerByPuuID(puuID: String): Summoner
    suspend fun summonerByName(name: String): Summoner
    suspend fun summonerByRiotId(gameName: String, tagLine: String): Summoner

    suspend fun lastVersion(): String?
    suspend fun encyclopediaChampion(version: String, lang: String): EncyclopediaChampion
    suspend fun championsRotations(): ChampionsRotation

    @OptIn(ExperimentalTypeInference::class)
    suspend fun <T> fetchApiKey(
        flowCollector: FlowCollector<T>,
        @BuilderInference block: suspend FlowCollector<T>.() -> Unit,
    )

}

interface LocalSummonerDataSource {

    suspend fun insertSummoner(summoner: Summoner)

    suspend fun getLastCheckDateByPuuID(puuID: String): Long?
    suspend fun getLastCheckDateByName(platformID: PlatformID, name: String): Long?
    suspend fun getLastCheckDateByRiotId(gameName: String, tagLine: String): Long?

    suspend fun summonerByPuuID(puuID: String): Summoner
    suspend fun summonerByName(platformID: PlatformID, name: String): Summoner
    suspend fun summonerByRiotId(gameName: String, tagLine: String): Summoner

}

interface RedboxDataSource {

    suspend fun insetEncyclopediaChampion(encyclopediaChampion: EncyclopediaChampion, lang: String)
    suspend fun readEncyclopediaChampion(lang: String): EncyclopediaChampion?

    suspend fun insetChampionsRotation(championsRotation: ChampionsRotation)
    suspend fun readChampionsRotation(): ChampionsRotation?

}

interface PreloadDataSource {
    suspend fun lastVersion(): String?
    suspend fun fillBitmaps(encyclopediaChampion: EncyclopediaChampion): EncyclopediaChampion
    suspend fun championsOriginal(lang: String): Map<String, Champion>
    suspend fun encyclopediaChampion(lang: String): EncyclopediaChampion
}