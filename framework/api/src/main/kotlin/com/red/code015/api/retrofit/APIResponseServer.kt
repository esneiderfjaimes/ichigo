package com.red.code015.api.retrofit

import com.google.gson.annotations.SerializedName
import com.red.code015.domain.*

data class SummonerResponseServer(
    @SerializedName("id") val id: String,
    @SerializedName("accountId") val accountId: String,
    @SerializedName("puuid") val puuId: String,
    @SerializedName("name") val name: String,
    @SerializedName("profileIconId") val profileIconId: Int,
    @SerializedName("revisionDate") val revisionDate: Long,
    @SerializedName("summonerLevel") val level: Int,
)

data class AccountResponseServer(
    @SerializedName("puuid") val puuid: String,
    @SerializedName("gameName") val gameName: String?,
    @SerializedName("tagLine") val tagLine: String?,
)

data class LeagueResponseServer(
    @SerializedName("leagueId") val leagueId: String,
    @SerializedName("queueType") val queueType: String,
    @SerializedName("tier") val tier: String?,
    @SerializedName("rank") val rank: String?,
    @SerializedName("summonerId") val summonerId: String,
    @SerializedName("summonerName") val summonerName: String,
    @SerializedName("leaguePoints") val points: Long,
    @SerializedName("wins") val wins: Int,
    @SerializedName("losses") val losses: Int,
    @SerializedName("veteran") val veteran: Boolean,
    @SerializedName("inactive") val inactive: Boolean,
    @SerializedName("freshBlood") val freshBlood: Boolean,
    @SerializedName("hotStreak") val hotStreak: Boolean,
)

data class ChampionsRotationResponseServer(
    @SerializedName("freeChampionIds") val freeChampionIds: List<Int>,
    @SerializedName("freeChampionIdsForNewPlayers") val freeChampionIdsForNewPlayers: List<Int>,
    @SerializedName("maxNewPlayerLevel") val maxNewPlayerLevel: Int,
)

data class MasteriesResponseServer(
    @SerializedName("championId") val championId: Int,
    @SerializedName("championLevel") val championLevel: Int,
    @SerializedName("championPoints") val championPoints: Long,
    @SerializedName("lastPlayTime") val lastPlayTime: Long,
    @SerializedName("championPointsSinceLastLevel") val championPointsSinceLastLevel: Long,
    @SerializedName("championPointsUntilNextLevel") val championPointsUntilNextLevel: Long,
    @SerializedName("chestGranted") val chestGranted: Boolean,
    @SerializedName("tokensEarned") val tokensEarned: Int,
    @SerializedName("summonerId") val summonerId: String,
)

data class ChampionsResponseServer(
    @SerializedName("version") val version: String,
    @SerializedName("data") val data: Map<String, ChampionResponseServer>,
)

data class ChampionResponseServer(
    @SerializedName("version") val version: String,
    @SerializedName("id") val id: String,
    @SerializedName("key") val key: String,
    @SerializedName("name") val name: String,
    @SerializedName("title") val title: String,
    @SerializedName("image") val image: Image,
    @SerializedName("blurb") val blurb: String,
    @SerializedName("info") val info: Info,
    @SerializedName("tags") val tags: List<String>,
    @SerializedName("partype") val parType: String,
    @SerializedName("stats") val stats: Map<String, Double>,
    // Extra values
    @SerializedName("skins") val skins: List<Skin>?,
    @SerializedName("lore") val lore: String?,
    @SerializedName("allytips") val allyTips: List<String>?,
    @SerializedName("enemytips") val enemyTips: List<String>?,
    @SerializedName("spells") val spells: List<Spell>?,
    @SerializedName("passive") val passive: Passive?,
    @SerializedName("recommended") val recommended: List<Recommended>?,
)