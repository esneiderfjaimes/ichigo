package com.red.code015.database

import com.red.code015.domain.DataSource
import com.red.code015.domain.SummonerSummary

fun SummonerEntity.toDomain() = SummonerSummary(
    id,
    accountId,
    puuId,
    name,
    profileIconId,
    revisionDate,
    level,
    lastCheckDate,
    DataSource.LOCAL
)

fun SummonerSummary.toEntity() = SummonerEntity(
    id,
    accountId,
    puuId,
    name,
    profileIconId,
    revisionDate,
    level,
    lastCheckDate
)
