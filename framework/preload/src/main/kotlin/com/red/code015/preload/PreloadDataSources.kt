package com.red.code015.preload

import android.app.Application
import android.content.Context
import com.google.gson.Gson
import com.red.code015.data.PreloadDataSource
import com.red.code015.domain.Champion
import com.red.code015.domain.DataSource
import com.red.code015.domain.DataSources
import com.red.code015.domain.EncyclopediaChampion
import com.red.code015.preload.assets.*
import java.util.*

class DataDragonAssetsDataSource(
    val application: Application,
    val gson: Gson,
    private val ctx: Context = application.applicationContext,
) : PreloadDataSource {

    override suspend fun lastVersion() = readAssets("data/version")

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
                data = championsJSON.data.toChampsList(ctx),
                dataSource = DataSource(
                    DataSources.PRELOAD,
                    Date().time
                ),
            )
        }

    override suspend fun fillBitmaps(encyclopediaChampion: EncyclopediaChampion) = try {
        encyclopediaChampion.copy(
            data = encyclopediaChampion.data.map {
                it.copy(bitmap = ctx.assetsToBitmap("$FolderChampsThumbnail/${it.id}.webp"))
            }
        )
    } catch (e: Exception) {
        e.printStackTrace()
        encyclopediaChampion
    }

}


