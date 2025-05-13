package com.red.code015.data.model

import com.red.code015.domain.Account
import com.red.code015.domain.DataSources
import com.red.code015.domain.PlatformID
import java.util.*

data class SummonerSummaryUI(
    val name: String,
    val profileIconId: Int,
    val level: Int,
    val account: Account?,
    var leagues: List<LeagueSummaryUI> = emptyList(),
)

data class SummonerUI(
    val id: String,
    val accountId: String,
    val puuId: String,
    val platformID: PlatformID,
    val name: String,
    val profileIconId: Int,
    val level: Int,
    val account: Account? = null,
    val leagues: List<LeagueUI> = emptyList(),
    val lastCheckDate: Long = Date().time,
    val dataSource: DataSources,
)