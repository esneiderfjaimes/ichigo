package com.red.code015.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.red.code015.ui.components.material_modifications.IconButtonModified

@Composable
fun MyIcon(
    icon: ImageVector,
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current,
) {
    Icon(imageVector = icon, contentDescription = null, modifier, tint = tint)
}

@Composable
fun MyIconButton(icon: ImageVector, onClick: () -> Unit = {}) {
    IconButton(onClick = onClick) { MyIcon(icon) }
}

fun myIcon(icon: ImageVector): @Composable () -> Unit = { MyIcon(icon) }

fun myIconButton(icon: ImageVector, onClick: () -> Unit = {})
        : @Composable () -> Unit = { MyIconButton(icon, onClick) }

@Composable
fun IcButton(
    icon: ImageVector,
    containerColor: Color = LocalContentColor.current,
    tint: Color = contentColorFor(containerColor),
    onClick: () -> Unit = {},
) {
    IconButtonModified(
        onClick = onClick,
        modifier = Modifier
            .padding(start = 4.dp)
            .padding(4.dp)
            .clip(RoundedCornerShape(33))
            .background(containerColor),
    ) { MyIcon(icon,tint =tint) }
}

