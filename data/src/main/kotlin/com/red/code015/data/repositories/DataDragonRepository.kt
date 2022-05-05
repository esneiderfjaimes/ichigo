package com.red.code015.data.repositories

import android.util.Log
import com.red.code015.data.APIDataSource
import com.red.code015.data.PreloadDataSource
import com.red.code015.data.RedboxDataSource
import com.red.code015.data.RemoteRiotGamesDataSource
import com.red.code015.data.util.*
import com.red.code015.domain.Champion
import com.red.code015.domain.ChampionsRotation
import com.red.code015.domain.EncyclopediaChampion
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.experimental.ExperimentalTypeInference

@OptIn(ExperimentalTypeInference::class)
@Singleton
class DataDragonRepository @Inject constructor(
    source: APIDataSource,
    private val remote: RemoteRiotGamesDataSource,
    private val local: RedboxDataSource,
    private val preload: PreloadDataSource,
) : API(source) {

    companion object {
        const val TAG = "$TAG_LOGS:DragonRep"
        var count = 0
    }

    init {
        count++
        Log.w(TAG, "instance of DataDragonRepository($count)")
    }

    suspend fun lastVersion() = remote.lastVersion() ?: preload.lastVersion() ?: "..."

    suspend fun versions() = coroutineScope {
        val vPreload = async { preload.lastVersion() }
        val vRemote = async { remote.lastVersion() }
        Versions(vPreload.await(), vRemote.await())
    }

    suspend fun champion(champKey: String, version: String, lang: String): Champion? {
        var champion = local.readChampion(champKey, version, lang, "champion")
        if (champion == null) champion = remote.champion(version, lang, champKey)
            ?.also { local.insertChampion(it, champKey, version, lang, "champion") }
        return champion
    }

    suspend fun champsItems(
        lang: String = "en_US",
    ): EncyclopediaChampion {
        val (vPreload, vRemote) = versions()
        return loopDataSources(
            name = "champsItems",
            withDataSource = getDataSource(vPreload, vRemote),
            blockPreload = {
                return@loopDataSources preload.encyclopediaChampion(lang)
            },
            blockDatabase = {
                val eLocal = local.readEncyclopediaChampion(lang)
                return@loopDataSources preload.fillBitmaps(eLocal!!)
            },
            blockApi = {
                val eApi = remote.encyclopediaChampion(vRemote!!, lang)
                local.insetEncyclopediaChampion(eApi, lang)
                return@loopDataSources preload.fillBitmaps(eApi)
            }
        ) { getDataSource(vPreload, vRemote, it) }
    }

    fun encyclopediaChampion(lang: String = "en_US")
            : Flow<EncyclopediaChampion> = tryFlow("encyclopediaChampion") {
        emit(champsItems(lang))
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

}

