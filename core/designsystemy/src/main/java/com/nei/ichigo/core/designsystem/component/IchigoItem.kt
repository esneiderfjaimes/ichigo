package com.nei.ichigo.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.ColorImage
import coil3.compose.AsyncImagePreviewHandler
import coil3.compose.LocalAsyncImagePreviewHandler
import com.nei.ichigo.core.designsystem.theme.Gold

val DEFAULT_ITEM_PADDING = 4.dp
val DEFAULT_ITEM_SIZE = 75.dp
val DEFAULT_ITEM_SHAPE = RoundedCornerShape(25)
val DEFAULT_ITEM_BORDER_WIDTH = 0.75.dp

@Composable
fun IchigoItemImage(
    model: Any?,
    modifier: Modifier = Modifier,
    size: Dp = DEFAULT_ITEM_SIZE,
    shape: Shape = DEFAULT_ITEM_SHAPE,
    borderWidth: Dp = DEFAULT_ITEM_BORDER_WIDTH,
) {
    AsyncImage(
        model = model,
        modifier = Modifier
            // add background
            .clip(shape)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            // add other modifiers
            .then(modifier)
            // first limit image size
            .size(size)
            // add border
            .border(
                width = borderWidth,
                color = Gold,
                shape = shape
            )
            // add padding to adjust content to border
            .padding(borderWidth),
    )
}

@Composable
fun IchigoItemLabel(modifier: Modifier = Modifier, text: String) {
    Text(
        text = text,
        modifier = modifier
            // .background(MaterialTheme.colorScheme.background, CircleShape)
            .padding(top = 4.dp),
        style = MaterialTheme.typography.bodySmall,
        textAlign = TextAlign.Center
    )
}

@Preview
@Composable
private fun IchigoImagePreview() {
    CompositionLocalProvider(LocalAsyncImagePreviewHandler provides AsyncImagePreviewHandler {
        ColorImage(Color.Black.toArgb())
    }) {
        Column {
            IchigoItemImage(
                model = "",
                shape = MaterialTheme.shapes.small,
                borderWidth = 20.dp,
                size = 150.dp,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
            Box(
                Modifier
                    .padding(horizontal = 20.dp)
                    .background(Color.Red)
                    .size(150.dp)
            )
        }
    }
}