package com.red.code015.utils

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.red.code015.ui.common.padHor

fun Modifier.gradientEnd(color: Color, sizeGradient: Dp = padHor) = this.drawWithCache {
    val gradient = Brush.horizontalGradient(
        colors = listOf(
            Color.Transparent,
            color.copy(0.75f),
            color,
        ),
        startX = size.width - sizeGradient.toPx(),
        endX = size.width
    )
    onDrawWithContent {
        drawContent()
        drawRect(
            brush = gradient,
            blendMode = BlendMode.SrcOver,
            topLeft = Offset(size.width - sizeGradient.toPx(), 0.0f),
            size = Size(sizeGradient.toPx(), size.height),
        )
    }
}

fun Modifier.gradientShadow() = this.drawWithCache {
    val gradient = Brush.radialGradient(
        colors = listOf(Color(0xFF000000), Color.Transparent),
        center = Offset(size.width / 2, size.height / 2),
        radius = size.width / 2,
        // tileMode = TileMode.Repeated
    )

    onDrawWithContent {
        drawRect(
            brush = gradient,
        )
        drawContent()
    }
}