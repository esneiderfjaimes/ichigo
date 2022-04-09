package com.red.code015.database

import com.red.code015.database.room.SummonerEntity
import com.red.code015.domain.DataSource
import com.red.code015.domain.Summoner

fun SummonerEntity.toDomain() = Summoner(
    id,
    accountId,
    puuId,
    name,
    profileIconId,
    level,
    account,
    leagues,
    lastCheckDate,
    DataSource.LOCAL
)


fun Summoner.toEntity() = SummonerEntity(
    id,
    accountId,
    id = puuId,
    name = name,
    profileIconId = profileIconId,
    level = level,
    account = account,
    leagues = leagues,
    lastCheckDate = lastCheckDate
)
