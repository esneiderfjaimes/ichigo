package com.red.code015.ui.components.red.chips

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.accompanist.flowlayout.FlowRow
import com.red.code015.R
import com.red.code015.ui.common.ChipFilter
import com.red.code015.ui.pages.home.screens.summoner.paddingHorizontal

data class ChipsData<T>(val id: T, val text: String)

@Composable
fun <T> List<ChipsData<T>>.ChipsBase(
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