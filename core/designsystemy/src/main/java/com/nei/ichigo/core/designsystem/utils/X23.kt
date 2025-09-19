package com.nei.ichigo.core.designsystem.utils

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp

@Composable
fun AnimatedLoader(
    hasContent: Boolean,
    modifier: Modifier = Modifier,
    height: Dp = 8.dp,
    padding: Dp = 0.dp,
    color: Color = Color.Black,
    backgroundColor: Color = Color.White,
    indicatorWidth: Dp = 50.dp,
    visibleContent: @Composable () -> Unit
) {
    var showContent by remember { mutableStateOf(false) }
    val progress = remember { Animatable(0f) }
    val scale = remember { Animatable(0f) }
    // Mantener una vista siempre actualizada del Boolean sin reiniciar LaunchedEffect
    val hasContentState = rememberUpdatedState(hasContent)

    // No usamos hasContent como key: la corrutina NO se reiniciará en cada cambio
    LaunchedEffect(Unit) {
        // loop: subir a 1, chequear, si no hay contenido bajar a 0 y repetir
        while (true) {
            progress.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 1000, easing = LinearEasing)
            )
            // aquí la animación llegó a 1f; verificamos el valor *actual* de hasContent
            if (hasContent) break

            progress.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis = 1000, easing = LinearEasing)
            )
            // loop continua
        }

        // Cuando salimos del loop significa que en el momento de llegar a 1f había contenido
        progress.snapTo(1f)
        scale.snapTo(0f)
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing)
        )
        showContent = true
    }
/*    LaunchedEffect(showContent) {
        if (!showContent) {
            progress.snapTo(0f)
            while (true) {
                progress.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(
                        durationMillis = 1000,
                        easing = LinearEasing
                    )
                )
                if (hasContent) {
                    // Anim final (0 → 1)
                    progress.snapTo(0f)
                    progress.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(
                            durationMillis = 800,
                            easing = FastOutSlowInEasing
                        )
                    )
                    showContent = true
                    break
                }
                progress.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(
                        durationMillis = 1000,
                        easing = LinearEasing
                    )
                )
            }
        }

    }*/
/*
    LaunchedEffect(hasContent) {
        if (!hasContent) {
            showContent = false
            progress.snapTo(0f)
            // Loop infinito hasta que hasContent cambie
            while (!hasContent) {
                progress.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(
                        durationMillis = 1000,
                        easing = LinearEasing
                    )
                )
                progress.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(
                        durationMillis = 1000,
                        easing = LinearEasing
                    )
                )
            }
        } else {
            // Anim final (0 → 1)
            progress.snapTo(0f)
            progress.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = 800,
                    easing = FastOutSlowInEasing
                )
            )
            showContent = true
        }
    }
*/

    val density = LocalDensity.current
    val circleWidthPx = density.run { indicatorWidth.roundToPx() }

    Box(modifier) {

        Canvas(
            modifier = Modifier
                .padding(padding)
                .fillMaxWidth()
                .height(height)
                .background(backgroundColor, CircleShape)
        ) {
            fun getXs(): Pair<Float, Float> {
                val maxTranslateX = size.width - circleWidthPx
                val x1 = lerp(0f, maxTranslateX, progress.value)
                val x2 = x1 + circleWidthPx
                return x1 to x2
            }

            val (startX, endX) = if (!hasContent) {
                getXs()
            } else {
                val x1End = 0f
                val x1Start =  size.width - circleWidthPx
                val x1 = lerp(x1Start, x1End, scale.value)
                x1 to size.width
            }

            drawRoundRect(
                color = color,
                topLeft = Offset(startX, 0f),
                size = Size(endX - startX, size.height),
                cornerRadius = CornerRadius(x = size.height / 2, y = size.height / 2)
            )
        }


        if (!showContent) {
            // Aquí tu loader con el valor de progress
    //        LoaderAnimation(progress.value)
        } else {
            // Cuando terminó la anim, se muestra el contenido
            visibleContent()
        }
    }
}

@Composable
fun LoaderAnimation(progress: Float) {
    Box(
        modifier = Modifier
            .size(50.dp * progress) // ejemplo de anim usando progress
            .background(Color.Red, CircleShape)
    ) {
        Text("Loading... $progress")
    }
}

@Preview
@Composable
fun PreviewAnimatedLoader() {
    var hasContent by remember { mutableStateOf(false) }
    Column {

        AnimatedLoader(hasContent = hasContent) {
            Text("Content")
        }
        Button(onClick = { hasContent = !hasContent }) {
            Text("Toggle")
        }
    }
}

