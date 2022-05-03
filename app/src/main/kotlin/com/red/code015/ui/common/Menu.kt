package com.red.code015.ui.common

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.red.code015.ui.pages.home.screens.summoner.masteries.Action
import com.red.code015.ui.pages.home.screens.summoner.masteries.itemsMore

@Composable
fun <E : Enum<E>> DropdownMenuToggleItem(
    text: @Composable () -> Unit,
    items: Map<E, ChipData2>,
    selected: E,
    onClick: (E) -> Unit,
) {
    DropdownMenuItem(
        text = {
            Column(Modifier
                .padding(bottom = margin4, top = margin)
                .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)) {

                text()

                items.Toggle(selected, onClick)
            }
        },
        onClick = { },
        enabled = false,
    )
}


@SuppressLint("UnrememberedMutableState")
@Composable
fun Menu() { // TODO
    var expanded by remember { mutableStateOf(true) }
    var expandedShow by remember { mutableStateOf(true) }

    val yoffset = DpOffset(4.dp, 4.dp)

    var asd by mutableStateOf(Size.Zero)
    var asd2 by mutableStateOf(Size.Zero)
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
                expandedShow = false
            },
            modifier = Modifier
                .defaultMinSize(minWidth = 100.dp)
                .onGloballyPositioned {
                    asd = it.size.toSize()
                }, offset = yoffset
        ) {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {

                DropdownMenuItem(text = {
                    Text(text = Action.SeeHow.name)
                }, onClick = { expandedShow = true },

                    leadingIcon = {
                        Box(Modifier.size(24.dp))
                    },
                    trailingIcon = {
                        Icon(imageVector = Icons.Rounded.ArrowRight, contentDescription = null)
                    }
                )
                MenuDefaults.Divider()

                itemsMore.forEach { action ->
                    if (action == Action.Refresh) MenuDefaults.Divider()
                    DropdownMenuItem(
                        text = {
                            Text(text = action.name)
                        },
                        onClick = {
                            expanded = false
                        },
                        leadingIcon = {
                            action.icon?.let {
                                Icon(imageVector = it, contentDescription = null)
                            }
                        },
                    )
                }

            }
        }
        DropdownMenu(
            expanded = expandedShow,
            onDismissRequest = { expandedShow = false },
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
                    expandedShow = true

                })
            }
        }
    }
}