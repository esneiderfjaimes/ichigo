package com.red.code015.data.repositories

import android.util.Log
import com.red.code015.data.ForbiddenException
import com.red.code015.data.PreloadDataSource
import com.red.code015.data.RedboxDataSource
import com.red.code015.data.RemoteRiotGamesDataSource
import com.red.code015.data.util.*
import com.red.code015.domain.ChampionsRotation
import com.red.code015.domain.DataSources
import com.red.code015.domain.EncyclopediaChampion
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.experimental.ExperimentalTypeInference

@OptIn(ExperimentalTypeInference::class)
@Singleton
class DataDragonRepository @Inject constructor(
    private val remote: RemoteRiotGamesDataSource,
    private val local: RedboxDataSource,
    private val preload: PreloadDataSource,
) {

    companion object {
        const val TAG = "$TAG_LOGS:DragonRep"
        var count = 0
    }

    init {
        count++
        Log.w(TAG, "instance of DataDragonRepository($count)")
    }

    suspend fun lastVersion() = remote.lastVersion() ?: preload.lastVersion() ?: "..."

    private suspend fun versions() = coroutineScope {
        val vPreload = async { preload.lastVersion() }
        val vRemote = async { remote.lastVersion() }
        Versions(vPreload.await(), vRemote.await())
    }

    // TODO: convert in generic fun
    fun encyclopediaChampion(lang: String = "ko_KR")
            : Flow<EncyclopediaChampion> = tryFlow("encyclopediaChampion") {
        val (vPreload, vRemote) = versions()

        var errors = DataSourcesErrors()
        var dataSources = getDataSource(vPreload, vRemote, errors)

        Log.i(TAG, "encyclopediaChampion: vPreload:$vPreload vRemote:$vRemote")
        loop@ while (dataSources != null) {
            Log.i(TAG, "encyclopediaChampion: DataSource:$dataSources errors:$errors")
            try {
                when (dataSources) {
                    DataSources.PRELOAD -> {
                        emit(preload.encyclopediaChampion(lang))
                    }
                    DataSources.DATABASE -> {
                        val eLocal = local.readEncyclopediaChampion(lang)
                        emit(preload.fillBitmaps(eLocal!!))
                    }
                    DataSources.API -> {
                        tryRemote(this) {
                            val eApi = remote.encyclopediaChampion(vRemote!!, lang)
                            emit(preload.fillBitmaps(eApi))
                            local.insetEncyclopediaChampion(eApi, lang)
                        }
                    }
                }
                break@loop
            } catch (e: Exception) {
                errors = errors.let {
                    when (dataSources) {
                        DataSources.API -> it.copy(api = false)
                        DataSources.DATABASE -> it.copy(local = false)
                        DataSources.PRELOAD -> it.copy(preload = false)
                        else -> it
                    }
                }
                dataSources = getDataSource(vPreload, vRemote, errors)
            }
        }
    }

    fun championsRotations(): Flow<ChampionsRotation> = tryFlow("championsRotations") {
        val championsRotation = local.readChampionsRotation()
        if (championsRotation != null &&
            !requireRemoteFetch(championsRotation.dataSource.time, 1)
        ) {
            emit(championsRotation)
        } else {
            tryRemote(this) {
                remote.championsRotations().let {
                    emit(it)
                    local.insetChampionsRotation(it)
                }
            }
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

