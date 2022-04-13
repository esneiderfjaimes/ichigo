package com.red.code015.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.red.code015.utils.Coil

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
                color = MaterialTheme.colorScheme.primary,
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
fun ImageBackground(
    painter: Painter,
    paddingHorizontal:Dp,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    content: @Composable ColumnScope.() -> Unit,
) {
    val color = MaterialTheme.colorScheme.background
    Box {
        Image(painter = painter,
            contentDescription = null,
            modifier = Modifier.drawWithCache {
                val gradient = Brush.verticalGradient(
                    colors = listOf(Color.Transparent,
                        color.copy(0.75f),
                        color),
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