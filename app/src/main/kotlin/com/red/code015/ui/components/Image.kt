package com.red.code015.ui.components

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PriorityHigh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.red.code015.ui.common.ApplyOn
import com.red.code015.utils.Coil
import com.red.code015.utils.Coil.champion

@Composable
fun SummonerIcon(iconId: Int, sizeSummonerIcon: Dp = 50.0.dp) {
    SubcomposeAsyncImage(model = Coil.urlProfileIcon(iconId),
        contentDescription = null,
        modifier = Modifier
            .size(sizeSummonerIcon)
            .clip(CircleShape),
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
                    painter = this@SubcomposeAsyncImage.painter,
                    contentDescription = null
                )
            }
        }
    )
}

@Composable
fun ChampionThumbnail(
    bitmap: Bitmap?,
    isInRotation: Boolean,
    championImage: String,
    size: Dp,
    onClick: () -> Unit = {},
) {
    if (bitmap != null) Image(
        modifier = Modifier.champThumbnail(isInRotation, onClick, size),
        bitmap = bitmap.asImageBitmap(),
        contentDescription = null
    )
    else SubcomposeAsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(champion(championImage))
            .build(),
        contentDescription = null,
        loading = {
            Box(Modifier.champThumbnail(isInRotation, onClick, size)) {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            }
        },
        success = {
            Image(
                modifier = Modifier.champThumbnail(isInRotation, onClick, size),
                painter = this@SubcomposeAsyncImage.painter,
                contentDescription = null
            )
        },
        error = {
            colorScheme.error.ApplyOn {
                Box(Modifier
                    .champThumbnail(isInRotation, onClick, size)
                    .background(it)) {
                    Icon(Icons.Rounded.PriorityHigh, null, Modifier.align(Alignment.Center))
                }
            }
        }
    )
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

@Composable
fun ImageBackground(
    painter: Painter,
    paddingHorizontal: Dp,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    content: @Composable ColumnScope.() -> Unit,
) {
    val color = colorScheme.background
    Box {
        Image(painter = painter,
            contentDescription = null,
            modifier = Modifier.drawWithCache {
                val gradient = Brush.verticalGradient(
                    colors = listOf(Color.Transparent, color.copy(0.75f), color),
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