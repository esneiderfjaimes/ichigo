package com.nei.ichigo.core.domain

import android.util.Log
import com.nei.ichigo.core.data.model.asEntity
import com.nei.ichigo.core.data.model.asExternalModel
import com.nei.ichigo.core.data.repository.PagerHelper
import com.nei.ichigo.core.database.dao.ChampionDao
import com.nei.ichigo.core.database.model.ChampionEntity
import com.nei.ichigo.core.network.BuildConfig
import com.nei.ichigo.core.network.IchigoNetworkDataSource
import javax.inject.Inject

private const val FORCE_FETCH_CHAMPIONS = false

class GetChampionsUseCase @Inject constructor(
    private val networkDataSource: IchigoNetworkDataSource,
    private val championDao: ChampionDao,
    private val pagerHelper: PagerHelper,
) {
    operator fun invoke() = pagerHelper.createListPage { version, lang ->
        kotlin.runCatching {
            if (BuildConfig.DEBUG && FORCE_FETCH_CHAMPIONS) {
                throw IllegalStateException("Forced fetch")
            }

            val championEntities =
                championDao.getChampionsByVersionAndLang(version, lang)
            if (championEntities.isEmpty()) {
                throw IllegalStateException("No champions found")
            }
            championEntities.map(ChampionEntity::asExternalModel)
        }.getOrElse { throwable ->
            Log.e("OnlineChampions", "Error getting champions from database", throwable)
            networkDataSource.getChampions(version, lang).also { champions ->
                val championEntities = champions.map { it.asEntity(version, lang) }
                championDao.insertChampions(championEntities)
            }
        }
    }
}