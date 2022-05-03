package com.red.code015.data

import android.graphics.Bitmap
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

    suspend fun masteryScores(summonerID: String): Int
    suspend fun championMasteries(summonerID: String): Masteries

    suspend fun lastVersion(): String?
    suspend fun encyclopediaChampion(version: String, lang: String): EncyclopediaChampion
    suspend fun championsDetails(
        version: String,
        lang: String,
        filter: List<String> = listOf(),
    ): List<Champion>

    suspend fun championsRotations(): ChampionsRotation

    @OptIn(ExperimentalTypeInference::class)
    suspend fun <T> fetchApiKey(
        flowCollector: FlowCollector<T>,
        @BuilderInference block: suspend FlowCollector<T>.() -> Unit,
    )

    @OptIn(ExperimentalTypeInference::class)
    suspend fun <T> fetchApiKey2(
        block: suspend () -> T,
    ): T

    suspend fun champion(version: String, lang: String, champKey: String): Champion?
}

interface LocalMasteriesDataSource {

    suspend fun insertSummoner(masteries: Masteries)

    suspend fun getLastCheckDate(platformID: PlatformID, summonerID: String): Long?

    suspend fun masteries(platformID: PlatformID, summonerID: String): Masteries

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
    suspend fun readChampion(suffix: String, vararg prefixes: String): Champion?
    suspend fun insertChampion(champion: Champion, suffix: String, vararg prefixes: String)

}

interface PreloadDataSource {
    suspend fun lastVersion(): String?
    suspend fun fillBitmaps(encyclopediaChampion: EncyclopediaChampion): EncyclopediaChampion
    suspend fun championsOriginal(lang: String): Map<String, Champion>
    suspend fun encyclopediaChampion(lang: String): EncyclopediaChampion
    suspend fun getThumbChamp(champId: String): Bitmap?
}