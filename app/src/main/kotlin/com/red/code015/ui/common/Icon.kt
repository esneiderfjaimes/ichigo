package com.red.code015.ui.common

import androidx.compose.material.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun MyIcon(icon: ImageVector) {
    Icon(imageVector = icon, contentDescription = null, tint = LocalContentColor.current)
}

@Composable
fun MyIconButton(icon: ImageVector, onClick: () -> Unit = {}) {
    IconButton(onClick = onClick) { MyIcon(icon) }
}

fun myIcon(icon: ImageVector): @Composable () -> Unit = { MyIcon(icon) }

fun myIconButton(icon: ImageVector, onClick: () -> Unit = {})
        : @Composable () -> Unit = { MyIconButton(icon, onClick) }
