package com.red.code015.ui.components

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PriorityHigh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageScope
import coil.request.ImageRequest
import com.red.code015.data.model.ChampListItemUI
import com.red.code015.ui.common.ApplyOn
import com.red.code015.utils.Coil

@Composable
fun SummonerIcon(iconId: Int, sizeSummonerIcon: Dp = 50.0.dp) {
    AsyncImage(modifier = Modifier
        .size(sizeSummonerIcon)
        .clip(CircleShape),
        model = Coil.urlProfileIcon(iconId),
        loading = {
            Box(Modifier.size(sizeSummonerIcon)) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        },
        success = {
            Box(Modifier.border(width = 2.dp,
                color = colorScheme.primary,
                shape = CircleShape)) {
                Image(
                    modifier = Modifier
                        .padding(4.dp)
                        .clip(CircleShape),
                    painter = this@AsyncImage.painter,
                    contentDescription = null
                )
            }
        }
    )
}


typealias ImageBox = @Composable () -> Unit

@Composable
fun CommonChampionThumbnail(
    champListItem: ChampListItemUI,
    modifier: Modifier = Modifier,
    container: @Composable (ImageBox) -> Unit = { it() },
) {
    @Composable
    fun containerImage(painter: Painter) {
        container { Image(painter, null, modifier) }
    }

    if (champListItem.bitmap != null) {
        containerImage(painter = champListItem.bitmap.asPainter())
    } else AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(Coil.champion(champListItem.image.full))
            .build(),
        loading = {
            Box(modifier) {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            }
        },
        success = {
            containerImage(this@AsyncImage.painter)
        },
        error = {
            colorScheme.error.ApplyOn {
                Box(modifier.background(it)) {
                    Icon(Icons.Rounded.PriorityHigh, null, Modifier.align(Alignment.Center))
                }
            }
        }
    )
}

@Composable
fun AsyncImage(
    modifier: Modifier = Modifier,
    model: Any?,
    contentDescription: String? = null,
    loading: @Composable (SubcomposeAsyncImageScope.(AsyncImagePainter.State.Loading) -> Unit)? = null,
    success: @Composable (SubcomposeAsyncImageScope.(AsyncImagePainter.State.Success) -> Unit)? = null,
    error: @Composable (SubcomposeAsyncImageScope.(AsyncImagePainter.State.Error) -> Unit)? = null,
) {
    if (model is ImageRequest)
        Log.i("AsyncImage", "AsyncImage: ${model.data}")
    else Log.i("AsyncImage", "AsyncImage: ${model.toString()}")

    SubcomposeAsyncImage(model = model,
        contentDescription = contentDescription,
        modifier = modifier,
        loading = loading,
        success = success,
        error = error)
}

@SuppressLint("RememberReturnType")
@NonRestartableComposable
@Composable
fun Bitmap.asPainter(filterQuality: FilterQuality = DrawScope.DefaultFilterQuality)
        : BitmapPainter = asImageBitmap().let { bitmap ->
    remember(bitmap) { BitmapPainter(bitmap, filterQuality = filterQuality) }
}

fun Modifier.champThumbnail(isInRotation: Boolean, onClick: () -> Unit = {}, size: Dp) = size(size)
    .border(
        width = 0.75.dp,
        color = if (isInRotation) Color.Green else Color(0xFFC28F2C),
        shape = RoundedCornerShape(25)
    )
    .padding(0.75.dp)
    .clip(RoundedCornerShape(25))
    .clickable(onClick = onClick)

fun Modifier.champThumbnail(size: Dp) = size(size)
    .border(
        width = 0.75.dp,
        color = Color(0xFFC28F2C),
        shape = RoundedCornerShape(25)
    )
    .padding(0.75.dp)
    .clip(RoundedCornerShape(25))

@Composable
fun ImageBackground(
    modifier: Modifier,
    painter: Painter,
    paddingHorizontal: Dp,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    content: @Composable ColumnScope.() -> Unit,
) {
    val color = colorScheme.background
    val systemInDarkTheme = isSystemInDarkTheme()

    Box(modifier) {
        Image(painter = painter,
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .drawWithCache {
                    val gradient = Brush.verticalGradient(
                        colors = if (systemInDarkTheme) listOf(
                            Color.Transparent,
                            color.copy(0.75f),
                            color
                        )
                        else listOf(
                            Color.Transparent,
                            Color.Transparent,
                            Color.Transparent,
                            color.copy(0.25f),
                            color
                        ),
                        startY = 0F,
                        endY = size.height
                    )
                    onDrawWithContent {
                        drawContent()
                        drawRect(gradient, blendMode = BlendMode.SrcOver)
                    }
                }
        )
        Column(modifier = Modifier
            .align(Alignment.BottomCenter)
            .padding(horizontal = paddingHorizontal)
            .fillMaxWidth(),
            verticalArrangement = verticalArrangement,
            horizontalAlignment = horizontalAlignment,
            content = content
        )
    }
}