package com.nei.ichigo.core.designsystem.component

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.ColorImage
import coil3.ImageLoader
import coil3.compose.AsyncImagePreviewHandler
import coil3.compose.LocalAsyncImagePreviewHandler
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.setSingletonImageLoaderFactory
import coil3.request.crossfade
import coil3.util.DebugLogger
import com.nei.ichigo.core.designsystem.BuildConfig

object AsyncImageDefaults {

    val LoadingCircleProgress = @Composable {
        Box(
            contentAlignment = Alignment.Center
        ) {
            LoadingIndicator(Modifier.size(24.dp))
        }
    }

    val LoadingShimmer = @Composable {
        Spacer(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RectangleShape
                )
                .shimmerEffect(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RectangleShape
                )
        )
    }
}

@Composable
fun AsyncImage(
    model: Any?,
    modifier: Modifier = Modifier,
    loading: @Composable () -> Unit = AsyncImageDefaults.LoadingShimmer,
    contentScale: ContentScale = ContentScale.Fit
) {
    SubcomposeAsyncImage(
        model = model,
        contentDescription = null,
        modifier = modifier,
        contentScale = contentScale,
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
            loading()
        }
    )
}

@Composable
fun AsyncImagePreviewProvider(
    width: Int = 0,
    height: Int = 0,
    color: Color? = null,
    content: @Composable () -> Unit
) {
    val randomColor = color ?: Color(
        alpha = 128,
        red = (0..255).random(),
        green = (0..255).random(),
        blue = (0..255).random()
    )
    val previewHandler = AsyncImagePreviewHandler {
        val data = it.data
        if (data is String) {
            if (data.contains("h_103,w_103")) {
                return@AsyncImagePreviewHandler ColorImage(
                    randomColor.toArgb(),
                    width = 103,
                    height = 103,
                )
            }
        }
        ColorImage(randomColor.toArgb(), width = width, height = height)
    }

    CompositionLocalProvider(LocalAsyncImagePreviewHandler provides previewHandler) {
        content()
    }
}

@SuppressLint("ComposableNaming")
@Composable
fun loadImageLoaderFactory() {
    setSingletonImageLoaderFactory { context ->
        ImageLoader.Builder(context).apply {
            crossfade(true)
            if (BuildConfig.DEBUG) {
                logger(DebugLogger())
            }
        }.build()
    }
}

@Preview
@Composable
private fun AsyncImagePreview() {
    AsyncImagePreviewProvider {
        AsyncImage(
            model = "",
            modifier = Modifier.size(75.dp)
        )
    }
}