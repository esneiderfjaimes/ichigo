package com.nei.ichigo.core.data.repository

import com.nei.ichigo.core.data.model.ChampionsPage
import com.nei.ichigo.core.model.Champion
import com.nei.ichigo.core.network.IchigoNetworkDataSource
import jakarta.inject.Inject

internal class OnlineChampionsRepository @Inject constructor(
    private val dataSource: IchigoNetworkDataSource
) : ChampionsRepository {
    override suspend fun getVersions(): List<String> {
        return dataSource.getVersions()
    }

    override suspend fun getChampions(): List<Champion> {
        val version = getVersions().first()
        val lang = "en_US"
        return dataSource.getChampions(version, lang)
    }

    override suspend fun getChampionsPage(): ChampionsPage {
        val version = getVersions().first()
        val lang = "ko_KR"
        return ChampionsPage(
            version,
            lang,
            dataSource.getChampions(version, lang)
        )
    }
}