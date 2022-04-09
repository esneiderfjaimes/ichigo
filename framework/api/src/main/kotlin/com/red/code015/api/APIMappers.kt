package com.red.code015.api

import com.red.code015.api.retrofit.AccountResponseServer
import com.red.code015.api.retrofit.LeagueResponseServer
import com.red.code015.api.retrofit.SummonerResponseServer
import com.red.code015.domain.*

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
        summoner: SummonerResponseServer,
        account: AccountResponseServer?,
        leagues: List<LeagueResponseServer>,
    ): Summoner = summoner.run {
        Summoner(
            id = id,
            accountId = accountId,
            puuId = puuId,
            name = name,
            profileIconId = profileIconId,
            level = level,
            account = account?.toDomain(),
            leagues = leagues.toDomain(),
            dataSource = DataSource.REMOTE
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