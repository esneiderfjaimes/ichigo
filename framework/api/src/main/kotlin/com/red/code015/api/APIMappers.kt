package com.red.code015.api

import com.red.code015.api.retrofit.*
import com.red.code015.domain.*
import java.util.*

fun SummonerResponseServer.toProfile(platformID: PlatformID) = Profile(
    platformID = platformID,
    puuID = puuId,
    summonerID = id,
    accountID = accountId,
    name = name,
    profileIconID = profileIconId
)

object SummonerMapper {
    fun toDomain(
        platformID: PlatformID,
        summoner: SummonerResponseServer,
        account: AccountResponseServer?,
        leagues: List<LeagueResponseServer>,
    ): Summoner = summoner.run {
        Summoner(
            id = id,
            accountId = accountId,
            puuId = puuId,
            platformID = platformID,
            name = name,
            profileIconId = profileIconId,
            level = level,
            account = account?.toDomain(),
            leagues = leagues.toDomain(),
            dataSource = DataSources.API
        )
    }
}

fun AccountResponseServer.toDomain() = Account(gameName, tagLine)

fun List<LeagueResponseServer>.toDomain() = map { it.toDomain() }

fun LeagueResponseServer.toDomain() = League(
    queueType = queueType.toQueueType(),
    tier = tier?.toTier(),
    rank = rank,
    points = points,
    wins = wins,
    losses = losses,
    veteran = veteran,
    inactive = inactive,
    freshBlood = freshBlood,
    hotStreak = hotStreak
)

fun String.toQueueType() = when (this) {
    LeagueAPIConstants.QueueTypeRankedSolo -> QueueType.RankedSolo
    LeagueAPIConstants.QueueTypeRankedFlex -> QueueType.RankedFlex
    else -> QueueType.Unknown
}

fun String.toTier() = when (this) {
    LeagueAPIConstants.TierIron -> Tier.Iron
    LeagueAPIConstants.TierBronze -> Tier.Bronze
    LeagueAPIConstants.TierSilver -> Tier.Silver
    LeagueAPIConstants.TierGold -> Tier.Gold
    LeagueAPIConstants.TierPlatinum -> Tier.Platinum
    LeagueAPIConstants.TierDiamond -> Tier.Diamond
    LeagueAPIConstants.TierMaster -> Tier.Master
    LeagueAPIConstants.TierGrandMaster -> Tier.GrandMaster
    LeagueAPIConstants.TierChallenger -> Tier.Challenger
    else -> Tier.Unknown
}

fun Map<String, ChampionResponseServer>.toList() =
    map {
        ChampListItem(id = it.value.id,
            key = it.value.key,
            name = it.value.name,
            image = it.value.image,
            tags = it.value.tags)
    }

fun Map<String, ChampionResponseServer>.toDomain(): Map<String, Champion> =
    map { it.key to it.value.toDomain() }.toMap()

fun ChampionResponseServer.toDomain() = Champion(
    version = version,
    id = id,
    key = key,
    name = name,
    title = title,
    image = image,
    blurb = blurb,
    info = info,
    tags = tags,
    partype = parType,
    stats = stats,
    skins = skins ?: emptyList(),
    lore = lore,
    allytips = allyTips ?: emptyList(),
    enemytips = enemyTips ?: emptyList(),
    spells = spells ?: emptyList(),
    passive = passive,
    recommended = recommended ?: emptyList()
)

fun ChampionsRotationResponseServer.toDomain() = ChampionsRotation(
    freeChampionIds = freeChampionIds.map { it.toString() },
    freeChampionIdsForNewPlayers = freeChampionIdsForNewPlayers.map { it.toString() },
    maxNewPlayerLevel = maxNewPlayerLevel,
    dataSource = DataSource(DataSources.API, Date().time)
)