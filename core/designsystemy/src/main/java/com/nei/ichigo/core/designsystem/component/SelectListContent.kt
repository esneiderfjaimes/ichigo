package com.nei.ichigo.core.designsystem.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nei.ichigo.core.designsystem.utils.animateScrollSelected

@Composable
fun <T> SelectListContent(
    selectedItem: T?,
    items: List<T>,
    itemLabelNull: (@Composable () -> String)? = null,
    itemLabel: @Composable (T) -> String,
    onSelectItem: (T?) -> Unit,
) {
    val lazyListState = rememberLazyListState()

    LaunchedEffect(Unit) {
        lazyListState.animateScrollSelected(selectedItem, items)
    }

    LazyColumn(
        state = lazyListState,
        contentPadding = PaddingValues(bottom = 12.dp)
    ) {
        if (itemLabelNull != null) {
            item(key = null) {
                ItemCombo(
                    value = itemLabelNull(),
                    selected = selectedItem == null,
                    onClick = { onSelectItem(null) }
                )
            }
        }
        items(items, key = { it.toString() }) { item ->
            ItemCombo(
                value = itemLabel(item),
                selected = item == selectedItem,
                onClick = { onSelectItem(item) }
            )
        }
    }
}

@Composable
fun <T> SelectGridContent(
    selectedItem: T?,
    items: List<T>,
    columns: Int = 2,
    itemLabelNull: (@Composable () -> String)? = null,
    itemLabel: @Composable (T) -> String,
    onSelectItem: (T?) -> Unit,
) {
    val lazyGridState = rememberLazyGridState()

    LaunchedEffect(Unit) {
        lazyGridState.animateScrollSelected(selectedItem, items)
    }

    LazyVerticalGrid(
        state = lazyGridState,
        columns = GridCells.Fixed(columns),
        contentPadding = PaddingValues(bottom = 12.dp)
    ) {
        if (itemLabelNull != null) {
            item(key = null) {
                ItemCombo(
                    value = itemLabelNull(),
                    selected = selectedItem == null,
                    onClick = { onSelectItem(null) },
                    textAlign = TextAlign.Center
                )
            }
        }
        items(items, key = { it.toString() }) { item ->
            ItemCombo(
                value = itemLabel(item),
                selected = item == selectedItem,
                onClick = { onSelectItem(item) },
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview
@Composable
fun SelectListContentPreview() {
    val items = listOf("Item 1", "Item 2", "Item 3")
    var selectedItem: String? by remember { mutableStateOf(items.firstOrNull()) }
    SelectListContent(
        selectedItem = selectedItem,
        items = items,
        itemLabelNull = { "null" },
        itemLabel = { it },
        onSelectItem = { selectedItem = it },
    )
}

@Preview
@Composable
fun SelectGridContentPreview() {
    val items = listOf("Item 1", "Item 2", "Item 3")
    var selectedItem: String? by remember { mutableStateOf(items.firstOrNull()) }
    SelectGridContent(
        selectedItem = selectedItem,
        items = items,
        itemLabelNull = { "null" },
        itemLabel = { it },
        onSelectItem = { selectedItem = it },
    )
}