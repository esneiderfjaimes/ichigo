@file:OptIn(ExperimentalMaterialApi::class)

package com.red.code015.ui.common

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import com.google.accompanist.flowlayout.FlowRow
import com.red.code015.R
import com.red.code015.ui.components.material_modifications.Chip
import com.red.code015.ui.pages.home.screens.summoner.paddingHorizontal

@Composable
fun <T> List<T>.Filter(
    filter: T? = null,
    chipText: List<String>,
    title: String? = null,
    onFilterChipClick: (T?) -> Unit = {},
) {
    var indexSelected by rememberSaveable { mutableStateOf(indexOf(filter)) }
    if (isNotEmpty()) {
        Column(
            modifier = Modifier
                .animateContentSize()
                .padding(horizontal = paddingHorizontal),
            verticalArrangement = Arrangement.spacedBy(0.dp),
        ) {
            title?.let {
                Text(text = it,
                    modifier = Modifier.padding(horizontal = 8.dp),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    fontStyle = FontStyle.Italic,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f)
                )
            }

            FlowRow(Modifier.fillMaxWidth()) {
                ChipFilter(
                    text = stringResource(R.string.all),
                    enabled = -1 != indexSelected) {
                    indexSelected = -1
                    onFilterChipClick(null)
                }
                chipText.forEachIndexed { index, chipText ->
                    ChipFilter(
                        text = chipText,
                        enabled = index != indexSelected) {
                        indexSelected = index
                        onFilterChipClick(this@Filter[index])
                    }
                }
            }
        }
    }
}

@Composable
fun List<String>.Filter(
    filter: String? = null,
    title: String? = null,
    onFilterChipClick: (String?) -> Unit = {},
) {
    Filter(filter = filter, chipText = this, title, onFilterChipClick)
}

@Composable
fun <E : Enum<E>> List<E>.Filter(
    filter: E? = null,
    title: String? = null,
    onFilterChipClick: (E?) -> Unit = {},
) {
    Filter(filter = filter, chipText = this.map { it.name }, title, onFilterChipClick)
}

@Composable
fun ChipFilter(text: String, enabled: Boolean, onClick: () -> Unit) {
    Chip(Modifier.padding(2.dp), enabled, onClick) {
        Text(
            text = text,
            color = if (enabled) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f)
            else MaterialTheme.colorScheme.onPrimary
        )
    }
}

