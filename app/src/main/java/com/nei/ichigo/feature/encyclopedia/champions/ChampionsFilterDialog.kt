package com.nei.ichigo.feature.encyclopedia.champions

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
import com.nei.ichigo.core.designsystem.utils.roleToString

@Composable
fun ChampionsFilterDialog(
    currentTagSelected: String?,
    champions: List<String>,
    onDismiss: () -> Unit,
    onTagSelected: (String?) -> Unit = {},
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        modifier = Modifier.padding(24.dp),
        shape = MaterialTheme.shapes.extraLarge,
    ) {
        ChampionsFilterDialogContent(
            currentTagSelected = currentTagSelected,
            tags = champions,
            onTagSelected = onTagSelected
        )
    }
}

@Composable
fun ChampionsFilterDialogContent(
    currentTagSelected: String?,
    tags: List<String>,
    onTagSelected: (String?) -> Unit = {},
) {
    Column(
        Modifier
            .padding(horizontal = 24.dp)
            .padding(bottom = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = stringResource(R.string.filter_by_tag),
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
                selected = currentTagSelected == null,
                onClick = {
                    onTagSelected(null)
                }
            )

            tags.forEach { tag ->
                IchigoFilterChip(
                    text = roleToString(tag),
                    selected = tag == currentTagSelected,
                    onClick = {
                        onTagSelected(tag)
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun ChampionsFilterDialogContentPreview() {
    val currentTagSelected = "Assassin"
    val tags = mutableListOf("Assassin", "Fighter", "Mage", "Marksman", "Support", "Tank")
    Surface {
        ChampionsFilterDialogContent(
            currentTagSelected = currentTagSelected,
            tags = tags
        )
    }
}