package com.nei.ichigo.core.designsystem.utils

import androidx.compose.foundation.lazy.LazyListState

suspend fun <T> LazyListState.animateScrollSelected(selected: T?, list: List<T>) {
    if (selected == null) return

    val indexOf = list.indexOf(selected)
    if (indexOf < 0) return

    val middle = layoutInfo.visibleItemsInfo.count() / 2
    // plus one cause we want add manually the "automatic" item
    // and extra to calculate middle
    val index = indexOf - middle + 2
    if (index >= 0) {
        animateScrollToItem(index)
    }
}