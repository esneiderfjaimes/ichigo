package com.red.code015.data.repositories

import android.util.Log
import com.red.code015.data.ForbiddenException
import com.red.code015.data.LocalSummonerDataSource
import com.red.code015.data.RemoteRiotGamesDataSource
import com.red.code015.data.Result
import com.red.code015.data.util.TAG_LOGS
import com.red.code015.data.util.requireRemoteFetch
import com.red.code015.domain.PlatformID
import com.red.code015.domain.Summoner
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.experimental.ExperimentalTypeInference

@Singleton
class SummonerRepository @Inject constructor(
    private val remote: RemoteRiotGamesDataSource,
    private val local: LocalSummonerDataSource,
) {

    companion object {
        const val tag = "$TAG_LOGS:SummonerRep"
        var count = 0
        const val forceFetch = true
        const val isContinue = false
    }

    init {
        count++
        Log.w(tag, "instance of SummonerRepository($count)")
    }

    fun summonerByPuuID(puuID: String, forceFetch: Boolean): Flow<Summoner> = flow {
        val lastCheckDate = local.getLastCheckDateByPuuID(puuID)
        if (requireRemoteFetch(lastCheckDate, forceFetch)) {
            if (isContinue) {
                while (true) {
                    remoteFetchByPuuID(puuID, lastCheckDate)
                    delay(15000)
                }
            } else {
                remoteFetchByPuuID(puuID, lastCheckDate)
            }
        } else emit(local.summonerByPuuID(puuID))
    }

    // TODO: Implement Result
    fun summonerByName2(
        platformID: PlatformID,
        name: String,
        forceFetch: Boolean,
    ): Flow<Result<Summoner>> =
        flow {
            try {
                val lastCheckDate = local.getLastCheckDateByName(platformID, name)
                if (requireRemoteFetch(lastCheckDate, forceFetch)) {
                    if (isContinue) {
                        while (true) {
                            remoteFetch2(platformID, name, lastCheckDate)
                            delay(15000)
                        }
                    } else {
                        remoteFetch2(platformID, name, lastCheckDate)
                    }
                } else emit(Result.Data(local.summonerByName(platformID, name)))
            } catch (exception: Exception) {
                exception.printStackTrace()
                throw exception
            }
        }

    @OptIn(ExperimentalTypeInference::class)
    @BuilderInference
    suspend fun FlowCollector<Result<Summoner>>.remoteFetch2(
        platformID: PlatformID,
        name: String,
        lastCheckDate: Long?,
    ) {
        try {
            val summoner = remote.summonerByName(name)
            emit(Result.Data(summoner))
            local.insertSummoner(summoner)
        } catch (e: Exception) {
            e.printStackTrace()
            if (e !is UnknownHostException || lastCheckDate == null) throw e
            emit(
                Result.DataWithException(
                    value = local.summonerByName(platformID, name),
                    throwable = e
                )
            )
        }
    }

    fun summonerByName(platformID: PlatformID, name: String, forceFetch: Boolean): Flow<Summoner> =
        flow {
            try {
                val lastCheckDate = local.getLastCheckDateByName(platformID, name)
                if (requireRemoteFetch(lastCheckDate, forceFetch)) {
                    if (isContinue) {
                        while (true) {
                            remoteFetch(platformID, name, lastCheckDate)
                            delay(15000)
                        }
                    } else {
                        remoteFetch(platformID, name, lastCheckDate)
                    }
                } else emit(local.summonerByName(platformID, name))
            } catch (exception: Exception) {
                exception.printStackTrace()
                throw exception
            }
        }

    @OptIn(ExperimentalTypeInference::class)
    @BuilderInference
    suspend fun FlowCollector<Summoner>.remoteFetch(
        platformID: PlatformID,
        name: String,
        lastCheckDate: Long?,
    ) {
        try {
            val summoner = remote.summonerByName(name)
            emit(summoner)
            local.insertSummoner(summoner)
        } catch (e: Exception) {
            e.printStackTrace()
            if (lastCheckDate != null && e is ForbiddenException)
                emit(local.summonerByName(platformID, name))
            throw e
        }
    }

    @OptIn(ExperimentalTypeInference::class)
    @BuilderInference
    suspend fun FlowCollector<Summoner>.remoteFetchByPuuID(puuID: String, lastCheckDate: Long?) {
        try {
            val summoner = remote.summonerByPuuID(puuID)
            emit(summoner)
            local.insertSummoner(summoner)
        } catch (e: Exception) {
            e.printStackTrace()
            if (e !is UnknownHostException || lastCheckDate == null) throw e
            emit(local.summonerByPuuID(puuID))
        }
    }

    fun summonerByRiotID(gameName: String, tagLine: String): Flow<Summoner> = flow {
        val lastCheckDate = local.getLastCheckDateByRiotId(gameName, tagLine)
        if (requireRemoteFetch(lastCheckDate, false)) {
            try {
                val summoner = remote.summonerByRiotId(gameName, tagLine)
                emit(summoner)
                local.insertSummoner(summoner)
            } catch (e: Exception) {
                if (e !is UnknownHostException || lastCheckDate == null) throw e
                e.printStackTrace()
                emit(local.summonerByRiotId(gameName, tagLine))
            }
        } else emit(local.summonerByRiotId(gameName, tagLine))
    }

}
