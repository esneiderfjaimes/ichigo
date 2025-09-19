package com.nei.ichigo.core.designsystem.utils

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.PathEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.nei.ichigo.core.designsystem.AnimatedLoaderBar2
import com.nei.ichigo.core.designsystem.AnimatedLoaderBar3
import com.nei.ichigo.core.designsystem.AnimatedLoaderBarL

object AnimatedLoaderBarConstants {
    val easing = PathEasing(
        path = Path().apply {
            moveTo(0f, 0f)
            cubicTo(0.81f, 0.0f, 0.63f, 1.0f, 1f, 1f)
        }
    )
    const val INFINITY_DURATION_MILLIS = 1000
    const val SCALE_DURATION_MILLIS = 500
    const val SHOW_CONTENT_DURATION_MILLIS = 500
}

private fun calculateXCoordinates(
    isIntermittent: Boolean,
    width: Float,
    indicatorWidthPx: Float,
    progress: Float,
    scale: Float
): Pair<Float, Float> {
    fun infiniteXCoordinates(): Pair<Float, Float> {
        val maxTranslateX = width - indicatorWidthPx
        val x1 = lerp(0f, maxTranslateX, progress)
        val x2 = x1 + indicatorWidthPx
        return x1 to x2
    }

    return if (isIntermittent) {
        infiniteXCoordinates()
    } else {
        val (x1Init, x2Init) = infiniteXCoordinates()
        val x1 = lerp(x1Init, 0f, scale)
        val x2 = lerp(x2Init, width, scale)
        x1 to x2
    }
}

@Composable
fun <T> AnimatedLoaderBar(
    modifier: Modifier = Modifier,
    value: T?,
    padding: PaddingValues = PaddingValues(),
    indicatorSize: DpSize = DpSize(20.dp, 8.dp),
    color: Color = MaterialTheme.colorScheme.primary,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    showContentAnimation: Boolean = true,
    content: @Composable (value: T) -> Unit
) {
    val isIntermittent by remember(value) { derivedStateOf { value == null } }
    var showContent by remember { mutableStateOf(false) }

    LaunchedEffect(isIntermittent) {
        if (isIntermittent) {
            showContent = false
        }
    }

    val progress = remember { Animatable(0f) }
    val scale by animateFloatAsState(
        targetValue = if (!isIntermittent) 1f else 0f,
        animationSpec = tween(
            durationMillis = AnimatedLoaderBarConstants.SCALE_DURATION_MILLIS,
            easing = AnimatedLoaderBarConstants.easing
        ),
        label = "scaleAnim",
        finishedListener = {
            if (!isIntermittent) {
                showContent = true
            }
        }
    )

    LaunchedEffect(Unit) {
        progress.animateTo(
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = AnimatedLoaderBarConstants.INFINITY_DURATION_MILLIS,
                    easing = AnimatedLoaderBarConstants.easing
                ),
                repeatMode = RepeatMode.Reverse
            ),
        )
    }

    val localDensity = LocalDensity.current
    val indicatorWidthPx = with(localDensity) { indicatorSize.width.toPx() }

    Box(modifier = modifier) {
        Canvas(
            modifier = Modifier
                .padding(padding)
                .fillMaxWidth()
                .height(indicatorSize.height)
                .background(backgroundColor, CircleShape)
        ) {
            val (startX, endX) = calculateXCoordinates(
                isIntermittent = isIntermittent,
                width = size.width,
                indicatorWidthPx = indicatorWidthPx,
                progress = progress.value,
                scale = scale
            )

            drawRoundRect(
                color = color,
                topLeft = Offset(startX, 0f),
                size = Size(endX - startX, size.height),
                cornerRadius = CornerRadius(x = size.height / 2, y = size.height / 2)
            )
        }
        if (showContentAnimation) {
            AnimatedVisibility(
                visible = showContent,
                modifier = Modifier
                    .padding(top = padding.calculateTopPadding())
                    .fillMaxWidth()
                    .padding(indicatorSize.height / 2),
                enter = slideInVertically(
                    initialOffsetY = { -it },
                    animationSpec = tween(
                        durationMillis = AnimatedLoaderBarConstants.SHOW_CONTENT_DURATION_MILLIS,
                        //   delayMillis = AnimatedLoaderBarConstants.SCALE_DURATION_MILLIS
                    )
                )
            ) {
                if (value != null) {
                    content(value)
                }
            }
        } else {
            if (showContent && value != null) {
                Box(
                    modifier = Modifier
                        .padding(top = padding.calculateTopPadding())
                        .fillMaxWidth(),
                ) {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .padding(indicatorSize.height / 2)
                    ) {
                        content(value)
                    }
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color)
                    )
                }
            }
        }
    }
}

// Example

data class Info(
    val title: String,
    val description: String
)

@Composable
fun Content(info: Info, paddingDp: Dp) {
    ElevatedCard(
        modifier = Modifier
            // Warning: top padding consumed by the indicator
            .padding(horizontal = paddingDp)
            .padding(bottom = paddingDp)
            .fillMaxWidth(),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = paddingDp,
            pressedElevation = paddingDp,
            hoveredElevation = paddingDp,
            focusedElevation = paddingDp,
            draggedElevation = paddingDp,
        ),
        shape = RoundedCornerShape(
            topStart = 0.dp,
            topEnd = 0.dp,
            bottomStart = 8.dp,
            bottomEnd = 8.dp
        ),
    ) {
        var expanded by remember { mutableStateOf(false) }
        Column(
            Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(text = info.title)
            Text(text = info.description, maxLines = if (expanded) Int.MAX_VALUE else 2)
            Button(onClick = { expanded = !expanded }) {
                Text(if (expanded) "Collapse" else "Expand")
            }
        }
    }
}

@Preview
@Composable
fun AnimatedLoaderBarPreview() {
    Scaffold {
        var value: Info? by remember { mutableStateOf(null) }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedLoaderBarL(modifier = Modifier.padding(16.dp), true, onAnimationFinished = {})
            AnimatedLoaderBar2(modifier = Modifier.padding(16.dp)) {}
            AnimatedLoaderBar3(
                modifier = Modifier.padding(16.dp),
                isFinished = false,
                onAnimationFinished = {
                    println("La animación de la barra de carga ha finalizado.")
                }
            )

            val paddingDp = 16.dp
            AnimatedLoaderBar(
                modifier = Modifier
                    .padding(it)
                    .weight(1f),
                value = value,
                padding = PaddingValues(paddingDp),
                content = { info ->
                    Content(info, paddingDp = paddingDp)
                }
            )

            Button(onClick = {
                value = if (value != null) null else Info(
                    "Title",
                    "Description Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."
                )
            }) {
                Text(if (value == null) "Expand" else "Intermittent")
            }
        }
    }
}
