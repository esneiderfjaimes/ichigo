package com.red.code015.database

import com.red.code015.data.LocalSummonerDataSource
import com.red.code015.domain.SummonerSummary
import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SummonerRoomDataSource(
    database: IchigoDatabase,
) : LocalSummonerDataSource {

    private val summonerDao by lazy { database.summonerDao() }

    override fun insertSummoner(summonerSummary: SummonerSummary) {
        return summonerDao.insert(summonerSummary.toEntity())
    }

    override fun getSummonerByName(name: String): Maybe<SummonerSummary> {
        return summonerDao.byName(name)
            .map { it.toDomain() }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
    }

    override suspend fun getLastCheckDateByName(name: String) = summonerDao.lastCheckDateByName(name)
}
