package nei.ichigo.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

val padHor = 28.dp
val padVer = padHor / 2
val margin = 4.dp
val marginX2 = margin * 2
val marginX4 = margin * 4

val modFill = Modifier.padding(horizontal = padHor)

@Composable
fun CommonChampsGrid(
    championsSize: Int,
    size: Dp,
    footer: String? = null,
    content: LazyGridScope.() -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = size),
        horizontalArrangement = Arrangement.SpaceAround,
        contentPadding = PaddingValues(horizontal = padHor),
        content = {
            if (championsSize > 0) {
                textMaxSpan(text = "$championsSize champions")
            }
            content(this)
            footer?.let { textMaxSpan(it) }
        }
    )
}

fun LazyGridScope.textMaxSpan(text: String) {
    item(span = { GridItemSpan(maxLineSpan) }) {
        Text(
            text = text,
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp),
            color = colorScheme.onBackground.copy(0.25f),
            textAlign = TextAlign.Center,
            style = typography.bodySmall
        )
    }
}