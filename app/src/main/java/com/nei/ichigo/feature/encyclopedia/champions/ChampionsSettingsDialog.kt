@file:OptIn(ExperimentalMaterial3Api::class)

package com.nei.ichigo.feature.encyclopedia.champions

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ChampionsSettingsDialog(onDismiss: () -> Unit) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        modifier = Modifier.padding(24.dp),
        shape = MaterialTheme.shapes.extraLarge,
    ) {
        ChampionsSettingsDialogContent()
    }
}

@Composable
private fun ChampionsSettingsDialogContent() {
    Column(
        Modifier
            .padding(horizontal = 24.dp)
            .padding(bottom = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
        )
        Spacer(Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                "Language",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(Modifier.weight(1f))
            TextButton(onClick = {}) {
                Text("English")
            }
        }
    }
}

@Preview
@Composable
fun ChampionsSettingsDialogContentPreview() {
    Surface {
        ChampionsSettingsDialogContent()
    }
}