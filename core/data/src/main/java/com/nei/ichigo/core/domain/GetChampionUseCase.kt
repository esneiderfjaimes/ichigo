package com.nei.ichigo.core.domain

import android.content.Context
import com.nei.ichigo.core.data.model.ChampionPage
import com.nei.ichigo.core.data.repository.ChampionsRepository
import com.nei.ichigo.core.datastore.IchigoPreferencesDataSource
import com.nei.ichigo.core.network.IchigoNetworkDataSource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetChampionUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    private val networkDataSource: IchigoNetworkDataSource,
    championsRepository: ChampionsRepository,
    ichigoPreferencesDataSource: IchigoPreferencesDataSource,
) : PagerHelper<ChampionPage>(
    context,
    championsRepository,
    ichigoPreferencesDataSource
) {
    private lateinit var key: String

    operator fun invoke(championKey: String): Flow<Result<ChampionPage>> {
        key = championKey
        return flow
    }

    override suspend fun fetchPage(
        version: String, lang: String
    ): Result<ChampionPage> = kotlin.runCatching {
        val champion = networkDataSource.getChampion(version, lang, key)
        ChampionPage(champion = champion, version = version, lang = lang)
    }
}