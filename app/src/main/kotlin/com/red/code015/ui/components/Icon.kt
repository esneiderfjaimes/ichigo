package com.red.code015.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun IconButtonBadge(
    imageVector: ImageVector,
    badgeIsVisible: Boolean,
    visible: Boolean = true,
    contentDescription: String? = null,
    onClick: () -> Unit = {},
) {
    if (visible) IconButton(onClick = onClick) {
        Box {
            Icon(imageVector = imageVector, contentDescription = contentDescription)
            if (badgeIsVisible) {
                Box(Modifier
                    .size(4.dp)
                    .clip(CircleShape)
                    .background(Color.Red)
                    .align(Alignment.TopEnd))
            }
        }
    }
}