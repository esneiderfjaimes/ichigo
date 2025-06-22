package com.nei.ichigo.feature.encyclopedia.spells

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nei.ichigo.R
import com.nei.ichigo.core.designsystem.component.IchigoFilterChip

@Composable
fun SpellsFilterDialog(
    modesSelected: Set<String>,
    modes: List<String>,
    onDismiss: () -> Unit,
    onTagSelected: (String?) -> Unit = {},
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        /*
        modifier = Modifier.padding(24.dp),
        shape = MaterialTheme.shapes.extraLarge,
        */
    ) {
        SpellsFilterDialogContent(
            modesSelected = modesSelected,
            modes = modes,
            onModeSelected = onTagSelected
        )
    }
}

@Composable
fun SpellsFilterDialogContent(
    modesSelected: Set<String>,
    modes: List<String>,
    onModeSelected: (String?) -> Unit = {},
) {
    Column(
        Modifier
            .padding(horizontal = 24.dp)
            .padding(bottom = 24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = stringResource(R.string.filter_by_mode),
            style = MaterialTheme.typography.titleLarge
                .copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 12.dp)
        )
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            IchigoFilterChip(
                text = stringResource(R.string.all),
                selected = modesSelected.isEmpty(),
                enabled = modesSelected.isNotEmpty(),
                onClick = {
                    onModeSelected(null)
                }
            )

            modes.forEach { mode ->
                IchigoFilterChip(
                    text = modeToString(mode),
                    selected = modesSelected.contains(mode),
                    enabled = true,
                    onClick = {
                        onModeSelected(mode)
                    }
                )
            }
        }
    }
}

fun modeToString(mode: String): String = mode.lowercase().replaceFirstChar { it.uppercase() }

@Preview
@Composable
fun ChampionsFilterDialogContentPreview() {
    val currentTagSelected = setOf<String>()
    val modes = mutableListOf("Aram", "Classic")
    Surface {
        SpellsFilterDialogContent(
            modesSelected = currentTagSelected,
            modes = modes
        )
    }
}