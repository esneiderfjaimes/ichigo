package com.red.code015.api

import com.red.code015.data.RemoteSummonerDataSource
import com.red.code015.domain.SummonerSummary
import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SummonerRetrofitDataSource(private val summonersRequest: SummonersRequest) :
    RemoteSummonerDataSource {

    override fun getSummonerByName(name: String): Maybe<SummonerSummary> {
        return summonersRequest
            .getService<SummonersService>()
            .summonerByName(name)
            .map(SummonerResponseServer::toDomain)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
    }
}
