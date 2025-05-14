package com.nei.ichigo.core.designsystem.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nei.ichigo.R

@Composable
fun IchigoFilterChip(text: String, selected: Boolean, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .clickable(
                interactionSource = null,
                indication = ripple(),
                enabled = !selected,
                onClick = onClick
            ),
        color = if (selected) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.surface,
        tonalElevation = 4.dp,
        shape = RoundedCornerShape(50),
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(vertical = 6.dp, horizontal = 12.dp),
            color = if (selected) MaterialTheme.colorScheme.onPrimary
            else MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Preview
@Composable
fun IchigoFilterChipPreview() {
    IchigoFilterChip(text = stringResource(R.string.all), selected = true) {}
}

@Preview
@Composable
fun IchigoFilterChipPreview2() {
    IchigoFilterChip(text = stringResource(R.string.all), selected = false) {}
}