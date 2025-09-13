package com.nei.ichigo.common.layout

import androidx.annotation.PluralsRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.nei.ichigo.R
import com.nei.ichigo.core.designsystem.component.ShimmerScope

@Composable
fun ShimmerScope.BaseShimmer(
    innerPadding: PaddingValues,
    @PluralsRes
    idPlural: Int = R.plurals.number_of_items,
    sizeItems: Int = 100,
    minSize: Dp,
    itemContent: @Composable (LazyGridItemScope.(Int) -> Unit)
) {
    val items = (1..sizeItems).toList()
    Grid(
        minSize = minSize,
        innerPadding = innerPadding,
        userScrollEnabled = false,
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            Box {
                Text(
                    text = pluralStringResource(
                        id = idPlural,
                        count = items.size,
                        items.size
                    ).let { " ".repeat(it.length) },
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(8.dp)
                        .shimmerEffect(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                        ),
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                )
            }
        }

        items(
            items = items,
            key = { it },
            contentType = { it },
            itemContent = itemContent
        )
    }
}