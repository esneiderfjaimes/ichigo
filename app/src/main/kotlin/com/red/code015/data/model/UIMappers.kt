package com.red.code015.data.model

import androidx.compose.ui.graphics.Color
import com.red.code015.R
import com.red.code015.domain.*

// region Summoner

fun Summoner.toSummaryUI() = SummonerSummaryUI(
    name = name,
    profileIconId = profileIconId,
    level = level,
    account = account,
    leagues = leagues.toSummaryUI()
)

fun Summoner.toUI() = SummonerUI(
    id = id,
    accountId = accountId,
    puuId = puuId,
    platformID = platformID,
    name = name,
    profileIconId = profileIconId,
    level = level,
    account = account,
    leagues = leagues.toUI(),
    lastCheckDate = lastCheckDate,
    dataSource = dataSource
)

// endregion
// region League

fun List<League>.toSummaryUI() = map { it.toSummaryUI() }

fun League.toSummaryUI() = LeagueSummaryUI(
    queueType = queueType,
    tier = tier ?: Tier.Unknown,
    rank = rank ?: "",
    points = points
)

fun List<League>.toUI() = map { it.toUI() }

fun League.toUI() = LeagueUI(
    queueType = queueType,
    idResQueueType = when (queueType) {
        QueueType.Unknown -> R.string.queue_type_unknown
        QueueType.RankedFlex -> R.string.queue_type_ranked_flex
        QueueType.RankedSolo -> R.string.queue_type_ranked_solo
    },
    tier = tier ?: Tier.Unknown,
    rank = rank ?: "?",
    points = points,
    wins = wins,
    losses = losses,
    hotStreak = hotStreak,
    color = when (tier) {
        Tier.Iron -> Color(0xFF817678)
        Tier.Bronze -> Color(0xFF9F6347)
        Tier.Silver -> Color(0xFF809890)
        Tier.Gold -> Color(0xFFCD8837)
        Tier.Platinum -> Color(0xFF4E9996)
        Tier.Diamond -> Color(0xFF576BCE)
        Tier.Master -> Color(0xFF9D48E0)
        Tier.GrandMaster -> Color(0xFFD94444)
        Tier.Challenger -> Color(0xFFF4C874)
        else -> Color.Black
    },
    idResRank = when (tier) {
        Tier.Iron -> R.mipmap.tier0_iron
        Tier.Bronze -> R.mipmap.tier2_bronze
        Tier.Silver -> R.mipmap.tier1_silver
        Tier.Gold -> R.mipmap.tier3_gold
        Tier.Platinum -> R.mipmap.tier4_platinum
        Tier.Diamond -> R.mipmap.tier5_diamond
        Tier.Master -> R.mipmap.tier6_master
        Tier.GrandMaster -> R.mipmap.tier7_grandmaster
        Tier.Challenger -> R.mipmap.tier8_challenger
        else -> 0
    },
    miniSeries = miniSeries
)

// endregion

fun List<Mastery>.toListMasteryUI() = filter { it.champListItem != null }.map { it.toUI() }

fun Mastery.toUI() = MasteryUI(
    championId = championId,
    championLevel = championLevel,
    championPoints = championPoints,
    lastPlayTime = lastPlayTime,
    championPointsSinceLastLevel = championPointsSinceLastLevel,
    championPointsUntilNextLevel = championPointsUntilNextLevel,
    chestGranted = chestGranted,
    tokensEarned = tokensEarned,
    champListItem = champListItem!!.toUI(),
)

fun ChampListItem.toUI() = ChampListItemUI(
    id = id,
    key = key,
    name = name,
    skins = skins ?: listOf(),
    image = image,
    bitmap = bitmap
)