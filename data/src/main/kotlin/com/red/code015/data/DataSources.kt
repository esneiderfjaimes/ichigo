package com.red.code015.data

import com.red.code015.domain.SummonerSummary
import io.reactivex.Maybe

interface RemoteSummonerDataSource {
    fun getSummonerByName(name: String): Maybe<SummonerSummary>
}

interface LocalSummonerDataSource {
    fun insertSummoner(summonerSummary: SummonerSummary)
    fun getSummonerByName(name: String): Maybe<SummonerSummary>
    suspend fun getLastCheckDateByName(name: String): Long?
}
