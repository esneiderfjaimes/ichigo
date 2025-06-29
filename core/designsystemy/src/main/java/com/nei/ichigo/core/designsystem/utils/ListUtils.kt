package com.nei.ichigo.core.designsystem.utils

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun <T> LazyListState.animateScrollSelected(selected: T?, list: List<T>) {
    baseScrollSelected(
        selected = selected,
        list = list,
        visibleCount = layoutInfo.visibleItemsInfo.count(),
        totalItemsCount = layoutInfo.totalItemsCount,
        requestScrollToItem = ::requestScrollToItem
    )
}

suspend fun <T> LazyGridState.animateScrollSelected(selected: T?, list: List<T>) {
    baseScrollSelected(
        selected = selected,
        list = list,
        visibleCount = layoutInfo.visibleItemsInfo.count(),
        totalItemsCount = layoutInfo.totalItemsCount,
        requestScrollToItem = ::requestScrollToItem
    )
}

suspend fun <T> baseScrollSelected(
    selected: T?,
    list: List<T>,
    visibleCount: Int,
    totalItemsCount: Int,
    requestScrollToItem: (Int) -> Unit
) {
    if (selected == null) return

    val index = withContext(Dispatchers.IO) {
        val indexOf = list.indexOf(selected)
        if (indexOf < 0) return@withContext null
        val count = visibleCount
        val middle = if (count % 2 == 0) (count / 2) - 1 else count / 2
        // plus one cause we want add manually the "automatic" item
        // and extra to calculate middle
        indexOf + 1 - middle
    } ?: return

    if (index >= 0 && index < totalItemsCount - 1) {
        requestScrollToItem(index)
    }
}