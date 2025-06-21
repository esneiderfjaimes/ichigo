package com.nei.ichigo.core.designsystem.utils

import androidx.compose.foundation.lazy.LazyListState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun <T> LazyListState.animateScrollSelected(selected: T?, list: List<T>) {
    if (selected == null) return

    val index = withContext(Dispatchers.IO) {
        val indexOf = list.indexOf(selected)
        if (indexOf < 0) return@withContext null
        val count = layoutInfo.visibleItemsInfo.count()
        val middle = if (count % 2 == 0) (count / 2) - 1 else count / 2
        // plus one cause we want add manually the "automatic" item
        // and extra to calculate middle
        indexOf + 1 - middle
    } ?: return

    if (index >= 0 && index < layoutInfo.totalItemsCount - 1) {
        requestScrollToItem(index)
    }
}