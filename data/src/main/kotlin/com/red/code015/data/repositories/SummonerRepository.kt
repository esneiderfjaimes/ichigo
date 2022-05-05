package com.red.code015.data.repositories

import android.util.Log
import com.red.code015.data.APIDataSource
import com.red.code015.data.LocalSummonerDataSource
import com.red.code015.data.RemoteRiotGamesDataSource
import com.red.code015.data.util.TAG
import com.red.code015.data.util.getDataSource
import com.red.code015.data.util.loopDataSources
import com.red.code015.data.util.requireRemoteFetch
import com.red.code015.domain.PlatformID
import com.red.code015.domain.Summoner
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.experimental.ExperimentalTypeInference

@OptIn(ExperimentalTypeInference::class)
@Singleton
class SummonerRepository @Inject constructor(
    source: APIDataSource,
    private val remote: RemoteRiotGamesDataSource,
    private val local: LocalSummonerDataSource,
) : API(source) {

    companion object {
        var count = 0
    }

    init {
        count++
        Log.w("$TAG:SM", "instance of SummonerRepository($count)")
    }

    fun summonerByPuuID(puuID: String, forceFetch: Boolean)
            : Flow<Summoner> = flow {
        val lastCheckDate = local.getLastCheckDateByPuuID(puuID)
        loopDataSources(
            name = "ByPuuID",
            withDataSource = getDataSource(
                forceFetching = forceFetch,
                requiresFetching = requireRemoteFetch(lastCheckDate, forceFetch)
            ),
            blockApi = {
                val summoner = remote.summonerByPuuID(puuID)
                emit(summoner)
                local.insertSummoner(summoner)
            },
            blockDatabase = {
                emit(local.summonerByPuuID(puuID))
            },
            flowCollector = this
        )
    }

    fun summonerByName(platformID: PlatformID, name: String, forceFetch: Boolean)
            : Flow<Summoner> = flow {
        val lastCheckDate = local.getLastCheckDateByName(platformID, name)
        loopDataSources(
            name = "ByName",
            withDataSource = getDataSource(
                forceFetching = forceFetch,
                requiresFetching = requireRemoteFetch(lastCheckDate, forceFetch)
            ),
            blockApi = {
                val summoner = remote.summonerByName(name)
                emit(summoner)
                local.insertSummoner(summoner)
            },
            blockDatabase = {
                emit(local.summonerByName(platformID, name))
            },
            flowCollector = this
        )
    }

}
