@file:OptIn(ExperimentalMaterial3Api::class)

package com.red.code015.ui.components.red.menu

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.toSize


/*@Composable
fun <T> Menu(items: List<T>, expanded: Boolean, onExpandedChange: (Boolean) -> Unit) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { onExpandedChange(false) },
        offset = DpOffset(with(LocalDensity.current) { asd.width.toDp() + yoffset.x },
            yoffset.y),
        modifier = Modifier
            .defaultMinSize(minWidth = 100.dp)
            .onGloballyPositioned {
                asd2 = it.size.toSize()
            }
    ) {
        listOf("Grid", "List").forEach { action ->
            DropdownMenuItem(text = {
                Text(text = action)
            }, onClick = {
                onExpandedChange(false)
            })
        }
    }
}*/

data class ItemUI<T>(val id: T, val text: String)

class MenuStateHolder<T>(val items: List<ItemUI<T>>) {

    var selectedText by mutableStateOf("")
    var expanded by mutableStateOf(false)
    var selectedIndex by mutableStateOf(-1)
    var size by mutableStateOf(Size.Zero)


    fun onSelectedIndex(int: Int) {
        selectedIndex = int
    }

    fun onSelectedIndex(size: Size) {
        this.size = size
    }

    fun show() {
        expanded = true
    }

    fun hide() {
        expanded = false
    }
}

@Composable
fun <T> rememberState(items: List<ItemUI<T>>) = remember {
    MenuStateHolder(items)
}


@Composable
fun <T> Menu(items: Map<T, String>, selected: T, onClick: (T) -> Unit = {}) {
    Menu(items = items.map { ItemUI(it.key, it.value) }, selected = selected, onClick)
}

@Composable
fun <T> Menu(items: List<ItemUI<T>>, selected: T, onClick: (T) -> Unit = {}) {
    val state = rememberState(items)

    val indexOfFirst = items.indexOfFirst { it.id == selected }

    FilledTonalButton(
        onClick = { state.show() },
        modifier = Modifier
            .fillMaxWidth()
            .onGloballyPositioned { state.onSelectedIndex(it.size.toSize()) }
            .animateContentSize(),
        colors = ButtonDefaults.filledTonalButtonColors(
            containerColor = MaterialTheme.colorScheme.primary.copy(0.125f)
        ),
    ) {
        Text(
            text = if (indexOfFirst != -1) items[indexOfFirst].text
            else "No selected",
            modifier = Modifier,
            style = MaterialTheme.typography.bodyMedium
        )

        DropdownMenu(expanded = state.expanded,
            onDismissRequest = { state.hide() },
            modifier = Modifier
            // TODO
            // .width(with(LocalDensity.current) { state.size.width.toDp() })
        ) {
            state.items.forEach {
                val (id, text) = it
                DropdownMenuItem(
                    text = { Text(text = text, style = MaterialTheme.typography.bodyMedium) },
                    onClick = {
                        onClick(id)
                        state.hide()
                    }
                )
            }
        }
    }

}