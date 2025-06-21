package com.nei.ichigo.core.domain

import android.content.Context
import android.util.Log
import com.nei.ichigo.core.data.model.ItemsPage
import com.nei.ichigo.core.data.repository.ChampionsRepository
import com.nei.ichigo.core.datastore.IchigoPreferencesDataSource
import com.nei.ichigo.core.network.IchigoNetworkDataSource
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class GetItemsUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    private val networkDataSource: IchigoNetworkDataSource,
    championsRepository: ChampionsRepository,
    ichigoPreferencesDataSource: IchigoPreferencesDataSource,
) : PagerHelper<ItemsPage>(
    context,
    championsRepository,
    ichigoPreferencesDataSource
) {
    operator fun invoke() = flow

    override suspend fun fetchPage(version: String, lang: String) = runCatching {
        val items = networkDataSource.getItems(version, lang)
        Log.d(" GetItemsUseCase", "Fetched $items items")
        ItemsPage(
            version = version,
            lang = lang,
            icons = items,
            // .sortedByDescending { it.code.toInt() }
        )
    }
}