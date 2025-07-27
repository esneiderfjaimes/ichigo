package com.nei.ichigo.core.domain

import android.util.Log
import com.nei.ichigo.core.data.model.ItemsPage
import com.nei.ichigo.core.data.repository.PagerHelper
import com.nei.ichigo.core.network.IchigoNetworkDataSource
import javax.inject.Inject

class GetItemsUseCase @Inject constructor(
    private val networkDataSource: IchigoNetworkDataSource,
    private val pagerHelper: PagerHelper
) {
    operator fun invoke() = pagerHelper.createFlow { version, lang ->
        val items = networkDataSource.getItems(version, lang)
        Log.d(" GetItemsUseCase", "Fetched $items items")

        val maps = items.asSequence()
            .map { it.maps }
            .flatten()
            .distinct()

        ItemsPage(
            version = version,
            lang = lang,
            items = items,
            // .sortedByDescending { it.code.toInt() }
            maps = maps.toList()
        )
    }
}