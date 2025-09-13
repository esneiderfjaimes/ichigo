package com.nei.ichigo.common.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Grid(
    modifier: Modifier = Modifier,
    state: LazyGridState = rememberLazyGridState(),
    minSize: Dp,
    innerPadding: PaddingValues,
    content: LazyGridScope.() -> Unit
) {
    LazyVerticalGrid(
        modifier = modifier,
        state = state,
        columns = GridCells.Adaptive(minSize = minSize),
        horizontalArrangement = Arrangement.SpaceAround,
        contentPadding = defaultPaddingValues(innerPadding),
        content = content
    )
}

@Composable
fun defaultPaddingValues(innerPadding: PaddingValues): PaddingValues {
    val layoutDirection = LocalLayoutDirection.current
    return PaddingValues(
        top = innerPadding.calculateTopPadding(),
        bottom = innerPadding.calculateBottomPadding() + 8.dp,
        start = innerPadding.calculateStartPadding(layoutDirection) + 32.dp,
        end = innerPadding.calculateEndPadding(layoutDirection) + 32.dp
    )
}