package com.red.code015.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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