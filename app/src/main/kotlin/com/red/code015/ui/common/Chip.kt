package com.red.code015.ui.common

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp

@Composable
fun ChipIcon(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    selected: Boolean = true,
    color: Color = if (selected) colorScheme.primary else colorScheme.background,
    contentColor: Color = contentColorFor(color),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable RowScope.() -> Unit,
) {
    CompositionLocalProvider(
        LocalContentColor provides contentColor,
    ) {
        Row(
            modifier = modifier
                .clip(RoundedCornerShape(33))
                .background(color)
                .clickable(
                    interactionSource = interactionSource,
                    indication = rememberRipple(),
                    enabled = enabled,
                    role = Role.Button,
                    onClick = onClick
                )
                .padding(6.dp)
                .animateContentSize(),
            verticalAlignment = Alignment.CenterVertically) {
            content(this)
        }
    }
}

data class ChipData2(val text: String, val icon: ImageVector)

@Composable
fun <E : Enum<E>> Map<E, ChipData2>.Toggle(selected: E, onClick: (E) -> Unit) {
    Row(
        Modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        entries.forEach { entry ->
            val (e, item) = entry
            ChipIcon({ onClick(e) },
                selected = e == selected) {
                Icon(imageVector = item.icon, contentDescription = null)
                Text(text = if (e == selected) item.text else "")
            }
        }
    }
}