@file:OptIn(ExperimentalCoilApi::class)

package com.nei.ichigo.core.designsystem.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import coil3.ColorImage
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.AsyncImagePreviewHandler
import coil3.compose.LocalAsyncImagePreviewHandler
import coil3.compose.SubcomposeAsyncImage

@Composable
fun AsyncImage(model: Any?, modifier: Modifier = Modifier) {
    SubcomposeAsyncImage(
        model = model,
        contentDescription = null,
        modifier = modifier,
        onError = {
            it.result.throwable.printStackTrace()
        },
        error = {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Rounded.Warning,
                    contentDescription = null,
                )
            }
        },
        loading = {
            Box(
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(Modifier.size(24.dp))
            }
        }
    )
}

@Composable
fun AsyncImagePreviewProvider(content: @Composable () -> Unit) {
    val previewHandler = AsyncImagePreviewHandler {
        val randomColor = Color(
            alpha = 128,
            red = (0..255).random(),
            green = (0..255).random(),
            blue = (0..255).random()
        )
        ColorImage(randomColor.toArgb())
    }

    CompositionLocalProvider(LocalAsyncImagePreviewHandler provides previewHandler) {
        content()
    }
}