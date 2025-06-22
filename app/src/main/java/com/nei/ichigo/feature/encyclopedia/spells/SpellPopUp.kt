package com.nei.ichigo.feature.encyclopedia.spells

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nei.ichigo.R
import com.nei.ichigo.core.model.Spell

@Composable
fun SpellPopUp(
    currentItemId: String?,
    item: Spell,
    onDismissRequest: () -> Unit
) {
    DropdownMenu(
        expanded = currentItemId == item.id,
        onDismissRequest = onDismissRequest,
        shape = RoundedCornerShape(8.dp),
        tonalElevation = 8.dp
    ) {
        SpellPopUpContent(item)
    }
}

@Composable
private fun SpellPopUpContent(item: Spell) {
    Column(
        Modifier
            .padding(8.dp)
            .sizeIn(maxWidth = 200.dp)
    ) {
        Text(
            text = item.name,
            style = MaterialTheme.typography.titleMedium
                .copy(fontWeight = FontWeight.Bold),
        )
        Text(
            text = stringResource(R.string.summoner_level, item.summonerLevel),
            style = MaterialTheme.typography.bodySmall,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = item.description,
            style = MaterialTheme.typography.bodySmall,
        )
        if (item.cooldown > 0.0) {
            Spacer(Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.cooldown_seconds, item.cooldown.toPrettyString()),
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}

fun Double.toPrettyString(): String {
    return if (this % 1.0 == 0.0) {
        this.toInt().toString()
    } else {
        this.toString()
    }
}

@Preview
@Composable
private fun SpellPopUpPreview() {
    Surface {
        SpellPopUpContent(
            item = Spell(
                id = "test",
                name = "test",
                description = "Lorem ipsum dolor sit amet consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
                tooltip = "test",
                image = "test",
                summonerLevel = 1,
                cooldown = 100.0,
                modes = emptyList()
            )
        )
    }
}