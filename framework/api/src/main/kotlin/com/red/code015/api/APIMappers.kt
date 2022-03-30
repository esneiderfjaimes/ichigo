package com.red.code015.api

import com.red.code015.domain.DataSource
import com.red.code015.domain.SummonerSummary
import java.util.*

fun SummonerResponseServer.toDomain() =
    SummonerSummary(
        id,
        accountId,
        puuId,
        name,
        profileIconId,
        revisionDate,
        level,
        Date().time,
        DataSource.REMOTE
    )