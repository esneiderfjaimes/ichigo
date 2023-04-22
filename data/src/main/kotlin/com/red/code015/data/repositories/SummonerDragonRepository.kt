package com.red.code015.data.repositories

import android.util.Log
import com.red.code015.data.LocalMasteriesDataSource
import com.red.code015.data.PreloadDataSource
import com.red.code015.data.RemoteRiotGamesDataSource
import com.red.code015.data.util.TAG_LOGS
import com.red.code015.data.util.tryFlow
import com.red.code015.domain.*
import kotlinx.coroutines.flow.Flow




class SummonerDragonRepository constructor(
    private val remote: RemoteRiotGamesDataSource,
    private val local: LocalMasteriesDataSource,
    private val preload: PreloadDataSource,
    private val dataDragonRepository: DataDragonRepository,
) {

    companion object {
        const val tag = "$TAG_LOGS:SummonerRep"
        var count = 0
    }

    init {
        count++
        Log.w(tag, "instance of SummonerRepository($count)")
    }

    fun championMasteries(platformID: PlatformID, summonerId: String, limit: Int)
            : Flow<List<Mastery>> = tryFlow("championMasteries") {
        // API
        val (vPreload, vRemote) = dataDragonRepository.versions()

        var masteries: List<Mastery> = try {
            local.masteries(platformID, summonerId).data
        } catch (e: Exception) {
            e.printStackTrace()
            remote.championMasteries(summonerId)
                .also { local.insertSummoner(it) }
                .data
        }

        // get the information of the champions
        var champsItems: List<ChampListItem> = dataDragonRepository.champsItems().data

        if (limit > 0) {
            val masteriesLimit = masteries.take(limit)
            val championKeys = masteriesLimit.map { it.championId.toString() }

            // filter by only the number of champions required
            champsItems = champsItems.filter { championKeys.contains(it.key) }

            // get champion ids and search for more up-to-date and complete information
            val championIds: List<String> = champsItems.map { it.id }
            val champions: List<Champion> = championIds.mapNotNull {
                dataDragonRepository.champion(it, vRemote ?: vPreload ?: "", "en_US")
            }

            // update list if possible
            champsItems = champsItems.map { item ->
                champions.firstOrNull { it.id == item.id }?.getItem() ?: item
            }
        }

        // get local thumbnail if possible
        champsItems = champsItems.map {
            it.copy(bitmap = preload.getThumbChamp(it.id))
        }

        // avoid adding champion masteries that you don't have information
        masteries = masteries.mapNotNull { mastery ->
            champsItems.firstOrNull { it.key == mastery.championId.toString() }?.let { champ ->
                mastery.copy(champListItem = champ)
            }
        }

        emit(masteries)
    }


}

