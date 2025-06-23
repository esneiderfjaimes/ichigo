package com.nei.ichigo.core.domain

import android.content.Context
import android.util.Log
import com.nei.ichigo.core.data.model.Page
import com.nei.ichigo.core.data.repository.ChampionsRepository
import com.nei.ichigo.core.datastore.IchigoPreferencesDataSource
import com.nei.ichigo.core.model.Spell
import com.nei.ichigo.core.network.IchigoNetworkDataSource
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class GetSpellsUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    private val networkDataSource: IchigoNetworkDataSource,
    championsRepository: ChampionsRepository,
    ichigoPreferencesDataSource: IchigoPreferencesDataSource,
) : PagerHelper<Page<Spell>>(
    context,
    championsRepository,
    ichigoPreferencesDataSource
) {
    operator fun invoke() = flow

    override suspend fun fetchPage(version: String, lang: String) = runCatching {
        val spells = networkDataSource.getSummonerSpells(version, lang)
        Log.d(" GetItemsUseCase", "Fetched $spells items")
        Page(
            version = version,
            lang = lang,
            data = spells,
        )
    }
}