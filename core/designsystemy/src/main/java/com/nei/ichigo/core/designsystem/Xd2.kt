package com.nei.ichigo.core.designsystem

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.PathEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun AnimatedLoaderBar3(
    modifier: Modifier = Modifier,
    isFinished: Boolean,
    onAnimationFinished: () -> Unit
) {
    val barHeight = 8.dp
    val cornerRadius = 4.dp

    var containerWidthPx by remember { mutableIntStateOf(0) }
    var shouldAnimate by remember { mutableStateOf(false) }

    val customPathEasing = remember {
        PathEasing(
            path = Path().apply {
                moveTo(0f, 0f)
                cubicTo(0.81f, 0.0f, 0.63f, 1.0f, 1f, 1f)
            }
        )
    }

    val animatedTranslationX by animateFloatAsState(
        targetValue = if (shouldAnimate && !isFinished) 1f else 0f,
        animationSpec = if (isFinished) tween(0) else infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = customPathEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "translation_animation"
    )

    val animatedWidth by animateDpAsState(
        targetValue = if (isFinished) with(LocalDensity.current) { containerWidthPx.toDp() } else 20.dp,
        animationSpec = tween(durationMillis = 500, easing = LinearEasing),
        label = "width_animation",
        finishedListener = {
            if (isFinished) {
                onAnimationFinished()
            }
        }
    )

    LaunchedEffect(Unit) {
        shouldAnimate = true
    }

    val density = LocalDensity.current

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(barHeight)
            .background(Color(0xFFE3F2FD), RoundedCornerShape(cornerRadius))
            .onSizeChanged { size ->
                containerWidthPx = size.width
            }
    ) {
        val barWidthPx = with(density) { 20.dp.toPx() }
        val translationPx = (containerWidthPx - barWidthPx) * animatedTranslationX

        Box(
            modifier = Modifier
                .width(animatedWidth)
                .fillMaxHeight()
                .offset { IntOffset(x = translationPx.roundToInt(), y = 0) }
                .background(Color(0xFF2196F3), RoundedCornerShape(cornerRadius))
        )
    }
}

@Preview
@Composable
fun AnimatedLoaderBarPreview3() {
    var isFinished by remember { mutableStateOf(false) }
    Scaffold(
        content = { padding ->
            Column(
                modifier = Modifier.padding(padding),
            ) {
                AnimatedLoaderBar3(
                    modifier = Modifier.padding(top = 300.dp),
                    isFinished = isFinished,
                    onAnimationFinished = {
                        println("La animación de la barra de carga ha finalizado.")
                    }
                )

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { isFinished = !isFinished },
                ) {
                    Text(if (isFinished) "Iniciar animación" else "Detener animación")
                }
            }

        }
    )
}