package com.nei.ichigo.feature.encyclopedia.items

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
fun ItemsFilterDialog(
    modesSelected: String?,
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
        ItemsFilterDialogContent(
            modesSelected = modesSelected,
            modes = modes,
            onModeSelected = onTagSelected
        )
    }
}

@Composable
fun ItemsFilterDialogContent(
    modesSelected: String?,
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
                selected = modesSelected == null,
                enabled = modesSelected != null,
                onClick = {
                    onModeSelected(null)
                }
            )

            modes.forEach { mode ->
                IchigoFilterChip(
                    text = modeToString(mode),
                    selected = modesSelected == mode,
                    enabled = true,
                    onClick = {
                        onModeSelected(mode)
                    }
                )
            }
        }
    }
}

@Composable
fun modeToString(mode: String): String {
    return when (mode) {
        "11" -> stringResource(R.string.summoner_s_rift)
        "12" -> stringResource(R.string.howling_abyss)
        "21" -> stringResource(R.string.nexus_blitz)
        "22" -> stringResource(R.string.teamfight_tactics)
        // ?
        "30" -> stringResource(R.string.arena)
        // ?
        "33" -> stringResource(R.string.swarm)
        "35" -> stringResource(R.string.brawl)
        else -> mode
    }
}

@Preview
@Composable
fun ChampionsFilterDialogContentPreview() {
    val modes = mutableListOf("Aram", "Classic")
    Surface {
        ItemsFilterDialogContent(
            modesSelected = null,
            modes = modes
        )
    }
}