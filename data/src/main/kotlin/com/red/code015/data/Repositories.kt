package com.red.code015.data

import com.red.code015.data.util.requiresRemoteUpdate
import com.red.code015.domain.SummonerSummary
import io.reactivex.Maybe
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SummonerRepository @Inject constructor(
    private val remote: RemoteSummonerDataSource,
    private val local: LocalSummonerDataSource,
) {

    suspend fun getSummonerByName(name: String): Maybe<SummonerSummary> {
        val lastCheckDate = local.getLastCheckDateByName(name)
        return if (lastCheckDate == null || requiresRemoteUpdate(lastCheckDate)) {
            remote.getSummonerByName(name)
                .observeOn(Schedulers.io())
                .doOnSuccess { local.insertSummoner(it) }
        } else {
            local.getSummonerByName(name)
        }
    }

}
