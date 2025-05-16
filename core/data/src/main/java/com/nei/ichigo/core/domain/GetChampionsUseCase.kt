package com.nei.ichigo.core.domain

import android.content.Context
import android.util.Log
import com.nei.ichigo.core.data.model.ChampionsPage
import com.nei.ichigo.core.data.model.asEntity
import com.nei.ichigo.core.data.model.asExternalModel
import com.nei.ichigo.core.data.repository.ChampionsRepository
import com.nei.ichigo.core.database.dao.ChampionDao
import com.nei.ichigo.core.database.model.ChampionEntity
import com.nei.ichigo.core.datastore.IchigoPreferencesDataSource
import com.nei.ichigo.core.model.Champion
import com.nei.ichigo.core.network.BuildConfig
import com.nei.ichigo.core.network.IchigoNetworkDataSource
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

private const val FORCE_FETCH_CHAMPIONS = false

class GetChampionsUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    private val networkDataSource: IchigoNetworkDataSource,
    private val championDao: ChampionDao,
    championsRepository: ChampionsRepository,
    ichigoPreferencesDataSource: IchigoPreferencesDataSource,
) : PagerHelper<ChampionsPage>(
    context,
    championsRepository,
    ichigoPreferencesDataSource
) {
    override suspend fun fetchPage(version: String, lang: String) = kotlin.runCatching {
        val champions: List<Champion> = kotlin.runCatching {
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

        ChampionsPage(
            version = version,
            lang = lang,
            champions = champions
        )
    }
}