package com.nei.ichigo.core.designsystem.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nei.ichigo.core.designsystem.utils.animateScrollSelected

@Composable
inline fun <T> SelectListContent(
    selectedItem: T,
    items: List<T>,
    noinline itemLabel: @Composable (T) -> String,
    crossinline onSelectItem: (T) -> Unit,
) {
    SelectListContent(
        selectedItem = selectedItem,
        items = items,
        itemLabelNull = null,
        itemLabel = itemLabel,
        onSelectItem = {
            onSelectItem(it!!)
        }
    )
}

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
        //  withContext(Dispatchers.IO) {
        lazyListState.animateScrollSelected(selectedItem, items)
        //   }
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