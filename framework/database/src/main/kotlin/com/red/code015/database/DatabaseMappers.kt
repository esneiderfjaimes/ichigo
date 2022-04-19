package com.red.code015.database

import com.red.code015.database.room.SummonerEntity
import com.red.code015.domain.DataSources
import com.red.code015.domain.Summoner

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
    accountId = id,
    puuId = accountId,
    id = puuId,
    platformID = platformID,
    name = name,
    profileIconId = profileIconId,
    level = level,
    account = account,
    leagues = leagues,
    lastCheckDate = lastCheckDate
)
