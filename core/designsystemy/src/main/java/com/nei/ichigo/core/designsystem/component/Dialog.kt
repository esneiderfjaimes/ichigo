package com.nei.ichigo.core.designsystem.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun IchigoDialogContent(
    title: @Composable RowScope.() -> Unit,
    onCloseRequest: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        shape = MaterialTheme.shapes.extraLarge,
        modifier = Modifier.padding(32.dp)
    ) {
        Column(
            Modifier
                .sizeIn(maxHeight = 600.dp),
        ) {
            Row(modifier = Modifier.padding(12.dp)) {
                title()
                IconButton(onClick = onCloseRequest) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = null
                    )
                }
            }

            content()
        }
    }
}

@Composable
fun RowScope.IchigoTitleDialog(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleLarge
            .copy(fontWeight = FontWeight.Bold),
        modifier = modifier
            .weight(1f)
            .padding(12.dp)
    )
}