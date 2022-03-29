package com.red.code015.data

import com.red.code015.domain.SummonerSummary
import io.reactivex.Single
import javax.inject.Inject

class SummonerRepository @Inject constructor(
    private val remoteSummonerDataSource: RemoteSummonerDataSource,
) {

    fun getByName(name: String): Single<SummonerSummary> =
        remoteSummonerDataSource.getSummonerByName(name)

}
