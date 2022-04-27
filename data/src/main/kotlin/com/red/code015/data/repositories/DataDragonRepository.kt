package com.red.code015.data.repositories

import android.util.Log
import com.red.code015.data.ForbiddenException
import com.red.code015.data.PreloadDataSource
import com.red.code015.data.RemoteRiotGamesDataSource
import com.red.code015.data.util.TAG_LOGS
import com.red.code015.data.util.tryFlow
import com.red.code015.domain.ChampionsRotation
import com.red.code015.domain.EncyclopediaChampion
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.experimental.ExperimentalTypeInference

@OptIn(ExperimentalTypeInference::class)
@Singleton
class DataDragonRepository @Inject constructor(
    private val remote: RemoteRiotGamesDataSource,
    private val preload: PreloadDataSource,
) {

    companion object {
        const val TAG = "$TAG_LOGS:ProfileRep"
        var count = 0
    }

    init {
        count++
        Log.w(TAG, "instance of ProfileRepository($count)")
    }

    fun encyclopediaChampion(lang: String = "en_US"): Flow<EncyclopediaChampion> = tryFlow {
        try {
            emit(preload.encyclopediaChampion(lang))
        } catch (e: Exception) {
            e.printStackTrace()
            try {
                emit(remote.encyclopediaChampion(lang))
            } catch (e2: Exception) {
                throw e2
            }
        }
    }

    fun championsRotations(): Flow<ChampionsRotation> = tryFlow {
        tryRemote(this) {
            emit(remote.championsRotations())
        }
    }

    private suspend fun <T> tryRemote(
        flowCollector: FlowCollector<T>,
        @BuilderInference block: suspend FlowCollector<T>.() -> Unit,
    ) {
        try {
            block.invoke(flowCollector)
        } catch (e: Exception) {
            if (e is ForbiddenException) {
                remote.fetchApiKey(flowCollector, block)
            } else throw e
        }
    }

}

