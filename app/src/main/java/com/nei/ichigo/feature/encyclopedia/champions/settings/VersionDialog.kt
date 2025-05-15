@file:OptIn(ExperimentalMaterial3Api::class)

package com.nei.ichigo.feature.encyclopedia.champions.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun VersionDialog(
    selectedVersion: String?,
    versions: List<String>,
    onVersionSelected: (String?) -> Unit,
    onDismiss: () -> Unit
) {
    BasicAlertDialog(onDismissRequest = onDismiss) {
        VersionDialogContent(
            selectedVersion = selectedVersion,
            versions = versions,
            onVersionSelected = onVersionSelected,
        )
    }
}


@Composable
fun VersionDialogContent(
    selectedVersion: String?,
    versions: List<String>,
    onVersionSelected: (String?) -> Unit,
) {
    Surface(
        shape = MaterialTheme.shapes.extraLarge
    ) {
        Column(
            Modifier
                .sizeIn(maxHeight = 600.dp)
        ) {
            Text(
                text = "Select Version",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(24.dp)
            )
            Column(
                Modifier
                    .verticalScroll(rememberScrollState())
            ) {
                Item(
                    "Last Version",
                    selected = selectedVersion == null,
                    onClick = { onVersionSelected(null) }
                )

                versions.forEach { version ->
                    Item(
                        value = version,
                        selected = version == selectedVersion,
                        onClick = { onVersionSelected(version) }
                    )
                }
            }
        }
    }
}

@Composable
private fun Item(value: String, selected: Boolean, onClick: () -> Unit) {
    Row(
        Modifier
            .clip(RoundedCornerShape(50))
            .clickable(
                enabled = !selected,
                onClick = onClick
            )
            .minimumInteractiveComponentSize()
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = 24.dp)
        )
        if (selected) {
            Spacer(Modifier.weight(1f))
            Icon(
                Icons.Rounded.Check,
                contentDescription = null,
                modifier = Modifier.padding(end = 24.dp)
            )
        }
    }
}

@Preview
@Composable
fun VersionDialogContentPreview() {
    VersionDialogContent(
        selectedVersion = "1.0.0",
        versions = listOf(
            "1.0.0",
            "2.0.0",
            "3.0.0",
            "4.0.0",
            "5.0.0",
            "6.0.0",
            "7.0.0",
            "8.0.0",
            "9.0.0",
            "10.0.0"
        ),
        onVersionSelected = {}
    )
}