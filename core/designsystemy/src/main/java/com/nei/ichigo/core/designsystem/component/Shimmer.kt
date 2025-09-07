package com.nei.ichigo.core.designsystem.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape

// @Alfa
@Composable
fun Modifier.shimmerEffect(
    color: Color = MaterialTheme.colorScheme.surface,
    shape: Shape = MaterialTheme.shapes.small,
    intensity: Float = DEFAULT_INTENSITY,
    duration: Int = DEFAULT_DURATION
): Modifier = composed {
    val shimmerColors = createShimmerColors(
        baseColor = color,
        intensity = intensity
    )
    val brush = createShimmerBrush(
        colors = shimmerColors,
        duration = duration
    )
    this.background(
        brush = brush,
        shape = shape
    )
}

@Composable
private fun createShimmerBrush(
    colors: List<Color>,
    duration: Int,
): Brush {
    val transition = rememberInfiniteTransition(label = "Shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = ANIMATION_TARGET_VALUE,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = duration,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        )
    )
    return Brush.linearGradient(
        colors = colors,
        start = Offset(SHIMMER_START_OFFSET, SHIMMER_START_OFFSET),
        end = Offset(translateAnim, translateAnim)
    )
}

fun createShimmerColors(
    baseColor: Color,
    intensity: Float,
): List<Color> {
    val maxAlpha = MAX_ALPHA_RATIO * intensity
    val minAlpha = MIN_ALPHA_RATIO * intensity
    return listOf(
        baseColor.copy(alpha = maxAlpha),
        baseColor.copy(alpha = minAlpha),
        baseColor.copy(alpha = maxAlpha)
    )
}

const val DEFAULT_DURATION = 800
const val DEFAULT_INTENSITY = 1f
const val ANIMATION_TARGET_VALUE = 1000f
const val SHIMMER_START_OFFSET = 10f
const val MAX_ALPHA_RATIO = 0.6f
const val MIN_ALPHA_RATIO = 0.2f

