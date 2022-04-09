package com.red.code015.data.repositories

import android.util.Log
import com.red.code015.data.LocalSummonerDataSource
import com.red.code015.data.RemoteRiotGamesDataSource
import com.red.code015.data.util.TAG_LOGS
import com.red.code015.data.util.requireRemoteFetch
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


    fun summonerByName(name: String): Flow<Summoner> = flow {
        val lastCheckDate = local.getLastCheckDateByName(name)
        if (requireRemoteFetch(lastCheckDate, forceFetch)) {
            if (isContinue) {
                while (true) {
                    remoteFetch(name, lastCheckDate)
                    delay(15000)
                }
            } else {
                remoteFetch(name, lastCheckDate)
            }
        } else emit(local.summonerByName(name))
    }

    @OptIn(ExperimentalTypeInference::class)
    @BuilderInference
    suspend fun FlowCollector<Summoner>.remoteFetch(name: String, lastCheckDate: Long?) {
        try {
            val summoner = remote.summonerByName(name)
            emit(summoner)
            local.insertSummoner(summoner)
        } catch (e: Exception) {
            e.printStackTrace()
            if (e !is UnknownHostException || lastCheckDate == null) throw e
            emit(local.summonerByName(name))
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
