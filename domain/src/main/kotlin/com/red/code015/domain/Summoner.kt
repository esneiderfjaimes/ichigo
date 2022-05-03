package com.red.code015.domain

import java.util.*

data class Summoner(
    val id: String,
    val accountId: String,
    val puuId: String,
    val platformID: PlatformID,
    val name: String,
    val profileIconId: Int,
    val level: Int,
    val account: Account? = null,
    val leagues: List<League> = emptyList(),
    val lastCheckDate: Long = Date().time,
    val dataSource: DataSources,
)

data class Account(
    val gameName: String?,
    val tagLine: String?,
)

data class League(
    val queueType: QueueType,
    val tier: Tier?,
    val rank: String?,
    val points: Long,
    val wins: Int,
    val losses: Int,
    val veteran: Boolean,
    val inactive: Boolean,
    val freshBlood: Boolean,
    val hotStreak: Boolean,
    val miniSeries: MiniSeries? = null,
)

data class MiniSeries(
    val losses: Int,
    val progress: String,
    val target: Int,
    val wins: Int,
)

enum class QueueType {
    Unknown, RankedFlex, RankedSolo
}

enum class Tier {
    Unknown, Iron, Bronze, Silver, Gold, Platinum, Diamond, Master, GrandMaster, Challenger
}


data class Masteries(
    val summonerId: String,
    val platformID: PlatformID,
    val data: List<Mastery>,
    val dataSource: DataSource,
)

data class Mastery(
    val championId: Int,
    val championLevel: Int,
    val championPoints: Long,
    val lastPlayTime: Long,
    val championPointsSinceLastLevel: Long,
    val championPointsUntilNextLevel: Long,
    val chestGranted: Boolean,
    val tokensEarned: Int,
    val champListItem: ChampListItem? = null,
)