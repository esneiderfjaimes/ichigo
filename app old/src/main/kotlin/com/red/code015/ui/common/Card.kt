@file:OptIn(ExperimentalMaterial3Api::class)

package com.red.code015.ui.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

val padHor = 28.dp
val padVer = padHor / 2
val margin = 4.dp
val margin2 = margin * 2
val margin4 = margin * 4

val modFill = Modifier.padding(horizontal = padHor)

@Composable
fun MyCard(
    modifier: Modifier = Modifier,
    minWith: Dp = 250.dp,
    minHeight: Dp = 80.dp,
    content: @Composable () -> Unit = {},
) {
    Card(modifier = modifier
        .fillMaxWidth()
        .padding(margin)
        .defaultMinSize(minWidth = minWith, minHeight = minHeight),
        contentColor = contentColorFor(colorScheme.background).copy(alpha = 0.75f),
        containerColor = colorScheme.background,
        border = BorderStroke(0.5.dp, colorScheme.outline.copy(alpha = 0.25f)),
        content = content
    )
}

@ExperimentalMaterial3Api
@Composable
fun Card(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(12.0.dp),
    containerColor: Color = colorScheme.background,
    contentColor: Color = contentColorFor(containerColor),
    border: BorderStroke? = null,
    elevation: CardElevation = CardDefaults.cardElevation(),
    content: @Composable () -> Unit,
) {
    Surface(
        modifier = modifier,
        shape = shape,
        color = containerColor,
        contentColor = contentColor,
        tonalElevation = elevation.tonalElevation(interactionSource = null).value,
        shadowElevation = elevation.shadowElevation(interactionSource = null).value,
        border = border,
        content = content
    )
}

@Composable
fun SpaceV(height: Dp) {
    Spacer(Modifier.height(height))
}

@Composable
fun SpaceH(width: Dp) {
    Spacer(Modifier.width(width))
}