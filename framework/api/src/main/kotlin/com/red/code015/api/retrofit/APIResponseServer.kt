package com.red.code015.api.retrofit

import com.google.gson.annotations.SerializedName
import com.red.code015.domain.DataValues
import com.red.code015.domain.IfSummonerSpell

// region Summoner

data class SummonerResponseServer(
    @SerializedName("id") val id: String?,
    @SerializedName("accountId") val accountId: String?,
    @SerializedName("puuid") val puuId: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("profileIconId") val profileIconId: Int?,
    @SerializedName("revisionDate") val revisionDate: Long?,
    @SerializedName("summonerLevel") val level: Int?,
)

// endregion
// region Account

data class AccountResponseServer(
    @SerializedName("puuid") val puuid: String?,
    @SerializedName("gameName") val gameName: String?,
    @SerializedName("tagLine") val tagLine: String?,
)

// endregion
// region League

data class LeagueResponseServer(
    @SerializedName("leagueId") val leagueId: String?,
    @SerializedName("queueType") val queueType: String?,
    @SerializedName("tier") val tier: String?,
    @SerializedName("rank") val rank: String?,
    @SerializedName("summonerId") val summonerId: String?,
    @SerializedName("summonerName") val summonerName: String?,
    @SerializedName("leaguePoints") val points: Long?,
    @SerializedName("wins") val wins: Int?,
    @SerializedName("losses") val losses: Int?,
    @SerializedName("veteran") val veteran: Boolean?,
    @SerializedName("inactive") val inactive: Boolean?,
    @SerializedName("freshBlood") val freshBlood: Boolean?,
    @SerializedName("hotStreak") val hotStreak: Boolean?,
    val miniSeries: MiniSeriesRS?,
)

data class MiniSeriesRS(
    val losses: Int?,
    val progress: String?,
    val target: Int?,
    val wins: Int?,
)

// endregion
// region Rotation

data class ChampionsRotationResponseServer(
    @SerializedName("freeChampionIds") val freeChampionIds: List<Int>?,
    @SerializedName("freeChampionIdsForNewPlayers") val freeChampionIdsForNewPlayers: List<Int>?,
    @SerializedName("maxNewPlayerLevel") val maxNewPlayerLevel: Int?,
)

// endregion
// region Masteries

data class MasteriesResponseServer(
    @SerializedName("championId") val championId: Int?,
    @SerializedName("championLevel") val championLevel: Int?,
    @SerializedName("championPoints") val championPoints: Long?,
    @SerializedName("lastPlayTime") val lastPlayTime: Long?,
    @SerializedName("championPointsSinceLastLevel") val championPointsSinceLastLevel: Long?,
    @SerializedName("championPointsUntilNextLevel") val championPointsUntilNextLevel: Long?,
    @SerializedName("chestGranted") val chestGranted: Boolean?,
    @SerializedName("tokensEarned") val tokensEarned: Int?,
    @SerializedName("summonerId") val summonerId: String?,
)

// endregion
// region Data Dragon

data class ChampionsResponseServer(
    @SerializedName("type") val type: String?,
    @SerializedName("format") val format: String?,
    @SerializedName("version") val version: String?,
    @SerializedName("data") val data: Map<String, ChampionResponseServer>?,
)

data class ChampionResponseServer(
    @SerializedName("version") val version: String?,
    @SerializedName("id") val id: String?,
    @SerializedName("key") val key: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("image") val image: ImageRS?,
    @SerializedName("blurb") val blurb: String?,
    @SerializedName("info") val info: InfoRS?,
    @SerializedName("tags") val tags: List<String>?,
    @SerializedName("partype") val parType: String?,
    @SerializedName("stats") val stats: Map<String, Double>?,
    // Extra values
    @SerializedName("skins") val skins: List<SkinRS>?,
    @SerializedName("lore") val lore: String?,
    @SerializedName("allytips") val allyTips: List<String>?,
    @SerializedName("enemytips") val enemyTips: List<String>?,
    @SerializedName("spells") val spells: List<SpellRS>?,
    @SerializedName("passive") val passive: PassiveRS?,
    @SerializedName("recommended") val recommended: List<RecommendedRS>?,
)

data class ImageRS(
    val full: String?,
    val sprite: String?,
    val group: String?,
    val x: Long?,
    val y: Long?,
    val w: Long?,
    val h: Long?,
)

data class InfoRS(
    val attack: Long?,
    val defense: Long?,
    val magic: Long?,
    val difficulty: Long?,
)

data class SkinRS(
    val id: String?,
    val num: Long?,
    val name: String?,
    val chromas: Boolean?,
)

data class SpellRS(
    val id: String?,
    val name: String?,
    val description: String?,
    val tooltip: String?,
    val leveltip: LevelTipRS?,
    val maxrank: Long?,
    val cooldown: List<Double>?,
    val cooldownBurn: String?,
    val cost: List<Long>?,
    val costBurn: String?,
    val datavalues: DataValues?,
    val effect: List<List<Double>?>?,
    val effectBurn: List<String?>?,
    val vars: List<Any?>?,
    val costType: String?,
    val maxammo: String?,
    val range: List<Long>?,
    val rangeBurn: String?,
    val image: ImageRS?,
    val resource: String?,
)

data class LevelTipRS(
    val label: List<String>?,
    val effect: List<String>?,
)

data class PassiveRS(
    val name: String?,
    val description: String?,
    val image: ImageRS?,
)

data class RecommendedRS(
    val champion: String?,
    val title: String?,
    val map: String?,
    val mode: String?,
    val type: String?,
    val customTag: String?,
    val sortrank: Long?,
    val extensionPage: Boolean?,
    val useObviousCheckmark: Boolean?,
    val customPanel: Any?,
    val blocks: List<BlockRS>?,
)

data class BlockRS(
    val type: String?,
    val recMath: Boolean?,
    val recSteps: Boolean?,
    val minSummonerLevel: Long?,
    val maxSummonerLevel: Long?,
    val showIfSummonerSpell: IfSummonerSpell,
    val hideIfSummonerSpell: IfSummonerSpell,
    val appendAfterSection: String?,
    val visibleWithAllOf: List<String>?,
    val hiddenWithAnyOf: List<String>?,
    val items: List<ItemRS>?,
)

data class ItemRS(
    val id: String?,
    val count: Long?,
    val hideCount: Boolean?,
)

// endregion