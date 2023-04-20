@file:OptIn(ExperimentalFoundationApi::class)

package nei.ichigo.ui.pages.encyclopedia.champs

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import nei.ichigo.data.modeles.ChampListItem
import nei.ichigo.ui.common.*

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChampsGrid(
    champs: List<ChampListItem>,
    footer: String?,
) {
    val size = 70.dp
    CommonChampsGrid(champs.size, size + marginX2, footer) {
        items(items = champs, key = { it.name }) { champ ->
            ChampItem(Modifier.animateItemPlacement(), champ, size)
        }
    }
}

@Composable
fun ChampItem(modifier: Modifier, champ: ChampListItem, size: Dp) {
    Column(
        modifier = modifier.padding(margin),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ChampThumbEncyclopedia(champ, size)
        Text(
            text = champ.name,
            modifier = Modifier.padding(margin),
            style = typography.bodySmall,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ChampThumbEncyclopedia(
    champ: ChampListItem,
    size: Dp,
    onClick: () -> Unit = {},
) {
    CommonChampionThumbnail(
        champ.image, Modifier.champThumbnail(
            isInRotation = false,
            onClick = onClick,
            size = size
        )
    )
}