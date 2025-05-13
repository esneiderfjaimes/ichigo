package com.red.code015.ui.components.red.chips

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun IntRange.ChipsListSafe(
    selected: Int,
    title: String? = null,
    onFilterChipClick: (Int) -> Unit = {},
) {
    toList().ChipsListSafe(selected, title, onFilterChipClick)
}

@Composable
fun IntRange.ChipsList(
    selected: Int? = null,
    title: String? = null,
    nullOption: Boolean = true,
    onFilterChipClick: (Int?) -> Unit = {},
) {
    toList().ChipsList(selected, title, nullOption, onFilterChipClick)
}

@Composable
fun <E> Collection<E>.ChipsListSafe(
    selected: E,
    title: String? = null,
    onFilterChipClick: (E) -> Unit = {},
) {
    map { ChipsData(it, it.toString()) }.ChipsBase(selected, title, false) {
        it?.let(onFilterChipClick)
    }
}

@Composable
fun <E> Collection<E>.ChipsList(
    selected: E? = null,
    title: String? = null,
    nullOption: Boolean = true,
    onFilterChipClick: (E?) -> Unit = {},
) {
    map { ChipsData(it, it.toString()) }.ChipsBase(selected, title, nullOption, onFilterChipClick)
}

@Preview
@Composable
fun PreviewChipsList() {

    var string by rememberSaveable { mutableStateOf("Tag 1") }
    var boolean by rememberSaveable { mutableStateOf(true) }
    var boolean2: Boolean? by rememberSaveable { mutableStateOf(true) }
    var integer by rememberSaveable { mutableStateOf(0) }

    Surface {
        Column(Modifier.verticalScroll(rememberScrollState())) {
            Column {

                listOf<ChipsData<Boolean?>>(
                    ChipsData(true, "is true"),
                    ChipsData(null, "is null"),
                    ChipsData(false, "is false")
                ).ChipsBase(boolean2) {
                    boolean2 = it
                }

                listOf(
                    "Tag 1",
                    "Tag 2",
                    "Tag 3"
                ).ChipsList(string) {
                    if (it != null) {
                        string = it
                    }
                }

                listOf(
                    "Tag 1",
                    "Tag 2",
                    "Tag 3"
                ).ChipsListSafe(string) {
                    string = it
                }

                listOf(true, false).ChipsListSafe(selected = boolean) { boolean = it }
                listOf(1, 2, 3).ChipsListSafe(selected = integer) { integer = it }
                (1..10).ChipsListSafe(selected = integer) { integer = it }
                (1..7).ChipsList(selected = integer) {
                    if (it != null) {
                        integer = it
                    }
                }
            }

        }
    }
}

@Composable
fun <E> Map<E, String>.Chips(
    selected: E,
    title: String? = null,
    onFilterChipClick: (E) -> Unit = {},
) {
    map { ChipsData(it.key, it.value) }.ChipsBase(selected,
        title, false) { it?.let { onFilterChipClick(it) } }
}