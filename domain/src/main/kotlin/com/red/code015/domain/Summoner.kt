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
)

enum class QueueType {
    Unknown, RankedFlex, RankedSolo
}

enum class Tier {
    Unknown, Iron, Bronze, Silver, Gold, Platinum, Diamond, Master, GrandMaster, Challenger
}
