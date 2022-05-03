/*
package com.red.code015.ui.common

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.flowlayout.FlowRow
import com.red.code015.R
import com.red.code015.domain.RotationChamp
import com.red.code015.ui.pages.home.screens.summoner.masteries.ShowView
import com.red.code015.ui.pages.home.screens.summoner.paddingHorizontal

data class ChipsData<E : Enum<E>>(val id: E, val text: String)

fun <T : Enum<T>> Map<T, String>.toListChips(): List<ChipsData<T>> =
    map { ChipsData(it.key, it.value) }

@Composable
private fun <E : Enum<E>> List<ChipsData<E>>.ChipsEnumBase(
    selected: E? = null,
    title: String? = null,
    nullOption: Boolean = true,
    onFilterChipClick: (E?) -> Unit = {},
) {
    Column {
        map{ ChipsDataE(it.id,it.text)}.ChipsBase(selected, title, nullOption, onFilterChipClick)
        val indexSelected: Int = indexOfFirst { it.id == selected }
        if (isNotEmpty()) {
            Column(
                modifier = Modifier
                    .animateContentSize()
                    .padding(horizontal = paddingHorizontal),
                verticalArrangement = Arrangement.spacedBy(0.dp),
            ) {
                title?.let {
                    Text(text = it,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium,
                        fontStyle = FontStyle.Italic,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f)
                    )
                }

                FlowRow(Modifier.fillMaxWidth()) {
                    if (nullOption) {
                        ChipFilter(
                            text = stringResource(R.string.all),
                            enabled = -1 != indexSelected) {
                            onFilterChipClick(null)
                        }
                    }
                    forEachIndexed { index, data ->
                        ChipFilter(
                            text = data.text,
                            enabled = index != indexSelected) {
                            onFilterChipClick(get(index).id)
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun Preview() {

    var selected by rememberSaveable {
        mutableStateOf(ShowView.List)
    }

    var rotation by rememberSaveable {
        mutableStateOf<RotationChamp?>(RotationChamp.None)
    }

    Surface {
        Column(Modifier.verticalScroll(rememberScrollState())) {
            Column {
                Text(text = "selected ${selected.name}")
                mapOf(
                    ShowView.Grid to "Grid",
                    ShowView.List to "List"
                ).ChipsEnum(selected) {
                    if (it != null) {
                        selected = it
                    }
                }
                listOf(ShowView.Grid,
                    ShowView.List).ChipsEnum(selected) {
                    if (it != null) {
                        selected = it
                    }
                }
                ShowView.values().ChipsEnum(selected) {
                    if (it != null) {
                        selected = it
                    }
                }
            }
            Column {
                Text(text = "selected ${selected.name}")
                mapOf(
                    ShowView.Grid to "Grid",
                    ShowView.List to "List"
                ).ChipsEnumSafe(selected) {
                    selected = it
                }
                listOf(ShowView.Grid,
                    ShowView.List).ChipsEnumSafe(selected) {
                    selected = it
                }
                ShowView.values().ChipsEnumSafe(selected) {
                    selected = it
                }
            }
            Column {
                Text(text = "selected ${rotation?.name}")
                mapOf(
                    RotationChamp.Free to "Free",
                    RotationChamp.FreeForNewPlayers to "Free for new players"
                ).ChipsEnum(rotation) {
                    rotation = it
                }
                listOf(RotationChamp.Free,
                    RotationChamp.FreeForNewPlayers).ChipsEnum(rotation) {
                    rotation = it
                }
                RotationChamp.values().ChipsEnum(rotation) {
                    rotation = it
                }
            }
            Column {
                mapOf(
                    ShowView.Grid to "Grid",
                    ShowView.List to "List"
                ).ChipsEnum(selected, "selected ${selected.name}") {
                    if (it != null) {
                        selected = it
                    }
                }
                listOf(
                    ShowView.Grid,
                    ShowView.List
                ).ChipsEnum(selected, "selected ${selected.name}") {
                    if (it != null) {
                        selected = it
                    }
                }
                ShowView.values()
                    .ChipsEnum(selected, "selected ${selected.name}") {
                        if (it != null) {
                            selected = it
                        }
                    }
            }
            Column {
                mapOf(
                    ShowView.Grid to "Grid",
                    ShowView.List to "List"
                ).ChipsEnumSafe(selected, "selected ${selected.name}") {
                    selected = it
                }
                listOf(
                    ShowView.Grid,
                    ShowView.List
                ).ChipsEnumSafe(selected, "selected ${selected.name}") {
                    selected = it
                }
                ShowView.values()
                    .ChipsEnumSafe(selected, "selected ${selected.name}") {
                        selected = it
                    }
            }
            Column {
                mapOf(
                    RotationChamp.Free to "Free",
                    RotationChamp.FreeForNewPlayers to "Free for new players"
                ).ChipsEnum(rotation, "selected ${selected.name}") {
                    rotation = it
                }
                listOf(RotationChamp.Free,
                    RotationChamp.FreeForNewPlayers
                ).ChipsEnum(rotation, "selected ${selected.name}") {
                    rotation = it
                }
                RotationChamp.values()
                    .ChipsEnum(rotation, "selected ${selected.name}") {
                        rotation = it
                    }
            }
        }
    }
}


@Preview
@Composable
fun Preview2() {

    var selected by rememberSaveable {
        mutableStateOf(ShowView.List)
    }

    Surface {
        Column(Modifier.verticalScroll(rememberScrollState())) {
            Column {
                Text(text = "selected ${selected.name}")

                ShowView.values().ChipsEnum(selected) {
                    if (it != null) {
                        selected = it
                    }
                }

                listOf(
                    ShowView.Grid,
                    ShowView.List
                ).ChipsEnum(selected) {
                    if (it != null) {
                        selected = it
                    }
                }

                mapOf(
                    ShowView.Grid to "Grid",
                    ShowView.List to "List",
                ).ChipsEnum(selected) {
                    if (it != null) {
                        selected = it
                    }
                }

                listOf(
                    ChipsData(ShowView.Grid, "Grid"),
                    ChipsData(ShowView.List, "List"),
                ).ChipsEnumBase(selected) {
                    if (it != null) {
                        selected = it
                    }
                }

            }

        }
    }
}

@Composable
fun <E : Enum<E>> Map<E, String>.ChipsEnum(
    selected: E? = null,
    title: String? = null,
    nullOption: Boolean = true,
    onClick: (E?) -> Unit = {},
) {
    toListChips().ChipsEnumBase(selected, title, nullOption, onClick)
}

@Composable
private fun <E : Enum<E>> Array<E>.ChipsEnum(
    selected: E? = null,
    title: String? = null,
    nullOption: Boolean = true,
    onClick: (E?) -> Unit = {},
) {
    toList().ChipsEnum(selected, title, nullOption, onClick)
}

@Composable
private fun <E : Enum<E>> Collection<E>.ChipsEnum(
    selected: E? = null,
    title: String? = null,
    nullOption: Boolean = true,
    onClick: (E?) -> Unit = {},
) {
    toList().map { ChipsData(it, it.name) }.ChipsEnumBase(selected, title, nullOption, onClick)
}

@Composable
fun <E : Enum<E>> Map<E, String>.ChipsEnumSafe(
    selected: E,
    title: String? = null,
    onClick: (E) -> Unit = {},
) {
    toListChips().ChipsEnumBase(selected, title, false) { it?.let { onClick(it) } }
}

@Composable
private fun <E : Enum<E>> Array<E>.ChipsEnumSafe(
    selected: E,
    title: String? = null,
    onClick: (E) -> Unit = {},
) {
    toList().ChipsEnum(selected, title, false) { it?.let { onClick(it) } }
}

@Composable
private fun <E : Enum<E>> Collection<E>.ChipsEnumSafe(
    selected: E,
    title: String? = null,
    onClick: (E) -> Unit = {},
) {
    toList().map { ChipsData(it, it.name) }
        .ChipsEnumBase(selected, title, false) { it?.let { onClick(it) } }
}
*/
