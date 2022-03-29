package com.red.code015.data.api

import com.red.code015.domain.SummonerSummary

fun SummonerResponseServer.toDomain() = SummonerSummary(name, profileIconId, level)