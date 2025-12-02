package com.nei.ichigo.core.designsystem.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun Modifier.shimmerEffect(
    color: Color = MaterialTheme.colorScheme.surfaceVariant,
    shape: Shape = MaterialTheme.shapes.small,
    duration: Int = DEFAULT_DURATION
): Modifier = composed {
    val shimmerColors = createShimmerColors(
        baseColor = color,
    )
    val brush = createShimmerBrush(
        colors = shimmerColors,
        duration = duration
    )
    clip(shape = shape)
        .drawBehind {
            drawRect(brush)
        }
    /*
    this.background(
        brush = brush,
        shape = shape
    )
    */
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

@Composable
private fun Modifier.shimmerEffectSync(
    color: Color = MaterialTheme.colorScheme.surfaceVariant,
    shape: Shape = MaterialTheme.shapes.small
): Modifier {
    val shimmerColors = createShimmerColors(
        baseColor = color,
    )
    val brush = createShimmerSyncBrush(
        colors = shimmerColors,
    )
    return clip(shape = shape).drawBehind {
        drawRect(brush)
    }
    /*
     return this.background(
         brush = brush,
         shape = shape
     )
     */
}

@Composable
private fun Modifier.shimmerEffectSync2(
    color: Color,
    shape: Shape
): Modifier = composed {
    val shimmerColors = createShimmerColors(color)
    val progress = LocalShimmerProgress.current
    this
        .clip(shape = shape)
        .drawBehind {
            val brush = Brush.linearGradient(
                colors = shimmerColors,
                start = Offset(SHIMMER_START_OFFSET, SHIMMER_START_OFFSET),
                end = Offset(progress, progress)
            )
            drawRect(brush)
        }
    /*
    this.background(
        brush = Brush.linearGradient(
            colors = shimmerColors,
            start = Offset(SHIMMER_START_OFFSET, SHIMMER_START_OFFSET),
            end = Offset(progress, progress)
        ),
        shape = shape
    )
    */
}

@Composable
private fun createShimmerSyncBrush(
    colors: List<Color>
): Brush {
    val progress = LocalShimmerProgress.current
    return Brush.linearGradient(
        colors = colors,
        start = Offset(SHIMMER_START_OFFSET, SHIMMER_START_OFFSET),
        end = Offset(progress, progress)
    )
}

@Composable
private fun createShimmerColors(
    baseColor: Color,
): List<Color> {
    return listOf(
        baseColor,
        MaterialTheme.colorScheme.surface,
        baseColor
    )
}

private const val DEFAULT_DURATION = 2000
private const val ANIMATION_TARGET_VALUE = 1000f
private const val SHIMMER_START_OFFSET = 10f

// ShimmerProvider.kt
private val LocalShimmerProgress = staticCompositionLocalOf<Float> {
    error("No shimmer progress provided")
}

@Composable
fun ShimmerProvider(
    duration: Int = DEFAULT_DURATION,
    content: @Composable ShimmerScope.() -> Unit
) {
    val transition = rememberInfiniteTransition(label = "shimmer sync")
    val progress by transition.animateFloat(
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

    CompositionLocalProvider(LocalShimmerProgress provides progress) {
        content(ShimmerScopeImpl)
    }
}

interface ShimmerScope {
    @Composable
    fun Modifier.shimmerEffect(
        color: Color = MaterialTheme.colorScheme.surfaceVariant,
        shape: Shape = MaterialTheme.shapes.small
    ): Modifier
}

private object ShimmerScopeImpl : ShimmerScope {
    @Composable
    override fun Modifier.shimmerEffect(
        color: Color,
        shape: Shape
    ): Modifier {
        return shimmerEffectSync(color, shape)
    }
}

@Preview
@Composable
fun ShimmerScreen() {
    val shimmerColors = listOf(
        Color.LightGray.copy(alpha = 0.6f),
        Color.LightGray.copy(alpha = 0.2f),
        Color.LightGray.copy(alpha = 0.6f),
    )

    ShimmerProvider {
        Column {
            Box(
                modifier = Modifier
                    .size(120.dp, 20.dp)
                    .shimmerEffect()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .size(120.dp, 20.dp)
                    .background(createShimmerSyncBrush(shimmerColors))
            )
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .size(200.dp, 20.dp)
                    .background(createShimmerSyncBrush(shimmerColors))
            )
        }
    }
}
