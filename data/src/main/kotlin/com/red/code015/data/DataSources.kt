package com.red.code015.data

import com.red.code015.domain.SummonerSummary
import io.reactivex.Single

interface RemoteSummonerDataSource {
    fun getSummonerByName(name: String): Single<SummonerSummary>
}
