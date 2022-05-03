package com.red.code015.database

import com.red.code015.database.room.MasteriesEntity
import com.red.code015.database.room.MasteryEntity
import com.red.code015.database.room.SummonerEntity
import com.red.code015.domain.*

fun SummonerEntity.toDomain() = Summoner(
    id = id,
    accountId = accountId,
    puuId = puuId,
    platformID = platformID,
    name = name,
    profileIconId = profileIconId,
    level = level,
    account = account,
    leagues = leagues,
    lastCheckDate = lastCheckDate,
    dataSource = DataSources.DATABASE
)

fun Summoner.toEntity() = SummonerEntity(
    accountId = accountId,
    puuId = puuId,
    id = id,
    platformID = platformID,
    name = name,
    profileIconId = profileIconId,
    level = level,
    account = account,
    leagues = leagues,
    lastCheckDate = lastCheckDate
)

fun MasteriesEntity.toDomain() = Masteries(
    summonerId = summonerId,
    platformID = platformID,
    dataSource = DataSource(DataSources.DATABASE, lastCheckDate),
    data = data.map { it.toDomain() }
)

fun Masteries.toEntity() = MasteriesEntity(
    summonerId = summonerId,
    platformID = platformID,
    lastCheckDate = dataSource.time,
    data = data.map { it.toEntity() }
)

fun MasteryEntity.toDomain() = Mastery(
    championId = championId,
    championLevel = championLevel,
    championPoints = championPoints,
    lastPlayTime = lastPlayTime,
    championPointsSinceLastLevel = championPointsSinceLastLevel,
    championPointsUntilNextLevel = championPointsUntilNextLevel,
    chestGranted = chestGranted,
    tokensEarned = tokensEarned,
)

fun Mastery.toEntity() = MasteryEntity(
    championId = championId,
    championLevel = championLevel,
    championPoints = championPoints,
    lastPlayTime = lastPlayTime,
    championPointsSinceLastLevel = championPointsSinceLastLevel,
    championPointsUntilNextLevel = championPointsUntilNextLevel,
    chestGranted = chestGranted,
    tokensEarned = tokensEarned
)
