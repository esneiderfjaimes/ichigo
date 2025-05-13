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
import com.red.code015.ui.pages.home.screens.summoner.paddingHorizontal

data class ChipsDataE<T>(val id: T, val text: String)

@Composable
fun <T> List<ChipsDataE<T>>.ChipsBase(
    selected: T? = null,
    title: String? = null,
    nullOption: Boolean = true,
    onFilterChipClick: (T?) -> Unit = {},
) {
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
                if (nullOption && indexOfFirst { it.id == null } == -1) {
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

@Preview
@Composable
fun Preview3() {

    var string by rememberSaveable { mutableStateOf("Tag 1") }
    var boolean by rememberSaveable { mutableStateOf(true) }
    var boolean2: Boolean? by rememberSaveable { mutableStateOf(true) }
    var integer by rememberSaveable { mutableStateOf(0) }

    Surface {
        Column(Modifier.verticalScroll(rememberScrollState())) {
            Column {

                listOf<ChipsDataE<Boolean?>>(
                    ChipsDataE(true, "is true"),
                    ChipsDataE(null, "is null"),
                    ChipsDataE(false, "is false")
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
            }

        }
    }
}

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
fun <E> Collection<E>.ChipsList(
    selected: E? = null,
    title: String? = null,
    nullOption: Boolean = true,
    onFilterChipClick: (E?) -> Unit = {},
) {
    map { ChipsDataE(it, it.toString()) }.ChipsBase(selected, title, nullOption, onFilterChipClick)
}

@Composable
fun <E> Collection<E>.ChipsListSafe(
    selected: E,
    title: String? = null,
    onFilterChipClick: (E) -> Unit = {},
) {
    map { ChipsDataE(it, it.toString()) }.ChipsBase(selected, title, false) {
        it?.let(onFilterChipClick)
    }
}

*/
