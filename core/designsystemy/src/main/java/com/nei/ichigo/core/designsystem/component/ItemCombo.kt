package com.nei.ichigo.core.designsystem.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ItemCombo(
    value: String,
    selected: Boolean,
    textAlign: TextAlign = TextAlign.Start,
    onClick: () -> Unit
) {
    Surface(
        tonalElevation = if (selected) 4.dp else 0.dp,
        onClick = onClick,
        enabled = !selected,
        shape = CircleShape,
    ) {
        Box(
            Modifier
                .minimumInteractiveComponentSize()
                .fillMaxWidth(),
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = 24.dp)
                    .padding(vertical = 8.dp)
                    .fillMaxWidth(),
                textAlign = textAlign
            )
            if (selected) {
                Icon(
                    Icons.Rounded.Check,
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 24.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun ItemComboPreview() {
    ItemCombo(value = "test", selected = false) {}
}

@Preview
@Composable
fun ItemComboPreview2() {
    ItemCombo(value = "test", selected = true) {}
}