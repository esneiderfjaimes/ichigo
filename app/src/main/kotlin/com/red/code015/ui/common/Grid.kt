package com.red.code015.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CommonChampsGrid(
    itemsSize: Int,
    size: Dp,
    footer: String? = null,
    content: LazyGridScope.() -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = size),
        horizontalArrangement = Arrangement.SpaceAround,
        contentPadding = PaddingValues(
            start = padHor, end = padHor,
            top = 0.dp, bottom = 80.dp
        ),
        content = {
            if (itemsSize > 0) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Box(Modifier
                        .fillMaxWidth()
                        .padding(4.dp)) {
                        Text(text = "$itemsSize champions",
                            modifier = Modifier.align(Alignment.Center),
                            color = colorScheme.onBackground.copy(0.325f),
                            style = typography.bodySmall)
                    }
                }
            }
            content(this)
            footer?.let {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Box(Modifier
                        .fillMaxWidth()
                        .padding(4.dp)) {
                        Text(text = it,
                            modifier = Modifier.align(Alignment.Center),
                            color = colorScheme.onBackground.copy(0.25f),
                            style = typography.bodySmall)
                    }
                }
            }
        }
    )
}

@Composable
fun CommonGrid(
    itemsSize: Int,
    size: Dp,
    footer: String? = null,
    contentPadding: PaddingValues = PaddingValues(
        start = 8.dp, end = 8.dp,
        top = 4.dp, bottom = 80.dp
    ),
    content: LazyGridScope.() -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = size),
        modifier = Modifier.background(colorScheme.surface),
        horizontalArrangement = Arrangement.SpaceAround,
        contentPadding = contentPadding,
        content = {
            if (itemsSize > 0) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Box(Modifier
                        .fillMaxWidth()
                        .padding(4.dp)) {
                        Text(text = "$itemsSize champions",
                            modifier = Modifier.align(Alignment.Center),
                            color = colorScheme.onBackground.copy(0.325f),
                            style = typography.bodySmall)
                    }
                }
            }
            content(this)
            footer?.let {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Box(Modifier
                        .fillMaxWidth()
                        .padding(4.dp)) {
                        Text(text = it,
                            modifier = Modifier.align(Alignment.Center),
                            color = colorScheme.onBackground.copy(0.25f),
                            style = typography.bodySmall)
                    }
                }
            }
        }
    )
}