package com.red.code015.preload

import android.app.Application
import com.google.gson.Gson
import com.red.code015.data.PreloadDataSource
import com.red.code015.domain.Champion
import com.red.code015.domain.DataSource
import com.red.code015.domain.DataSources
import com.red.code015.domain.EncyclopediaChampion
import com.red.code015.preload.assets.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class DataDragonAssetsDataSource(
    val application: Application,
    val gson: Gson,
) : PreloadDataSource {

    override suspend fun championsOriginal(lang: String): Map<String, Champion> {
        val fileName = championsOriginalFileName(lang)
        return assetsToType<BaseSchema<ChampJson>>(fileName).data.toDomain()
    }

    override suspend fun encyclopediaChampion(lang: String): EncyclopediaChampion =
        tryCoroutineScope("encyclopediaChampion") {
            val fileName = championsFileName(lang)
            val championsJSON = assetsToType<BaseSchema<ChampSummaryJson>>(fileName)
            EncyclopediaChampion(
                version = championsJSON.version,
                data = championsJSON.data.toChampsList(application.applicationContext),
                dataSource = DataSource(
                    DataSources.PRELOAD,
                    Date().time
                ),
            )
        }

    suspend fun encyclopediaChampion2(lang: String): EncyclopediaChampion {
        return withContext(Dispatchers.IO) {
            val fileName = championsFileName(lang)
            val championsJSON = assetsToType<BaseSchema<ChampSummaryJson>>(fileName)
            return@withContext EncyclopediaChampion(
                version = championsJSON.version,
                data = championsJSON.data.toChampsList(application.applicationContext),
                dataSource = DataSource(
                    DataSources.PRELOAD,
                    Date().time
                ),
            )
        }
    }
}


