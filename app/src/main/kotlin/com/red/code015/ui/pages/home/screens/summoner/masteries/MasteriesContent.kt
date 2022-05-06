@file:OptIn(ExperimentalFoundationApi::class)

package com.red.code015.ui.pages.home.screens.summoner.masteries

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowUpward
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.red.code015.R
import com.red.code015.data.model.*
import com.red.code015.ui.common.*
import com.red.code015.ui.components.CommonChampionThumbnail
import com.red.code015.ui.components.champThumbnail
import com.red.code015.ui.theme.icons.Chest
import com.red.code015.ui.theme.icons.IconsLoL
import com.red.code015.utils.ANIMATIONS_ON
import com.red.code015.utils.addTagOn
import com.red.code015.utils.formatPoints
import com.red.code015.utils.parseHtml
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun MasteriesContent(
    selectShowView: ShowView,
    filters: MasteriesViewModel.Filters,
    masteries: List<MasteryUI>,
) {
    when (selectShowView) {
        ShowView.Grid -> MasteriesGrid(
            masteries = filters.sortBy.inList(masteries, filters.isReverse),
            filters = filters)
        ShowView.List -> MasteriesList(
            masteries = filters.sortBy.inList(masteries, filters.isReverse),
            filters = filters)
    }
}


@Composable
fun MasteriesGrid(
    masteries: List<MasteryUI>,
    filters: MasteriesViewModel.Filters,
    footer: String? = null,
) {
    val size = 80.dp
    val items = masteries.filter(filters::predicate)
    CommonChampsGrid(items.size, size + margin2, footer) {
        items(items = items, key = { it.championId }) { mastery ->
            MasteryItem(Modifier.animateItemPlacement(), mastery, filters.search, size)
        }
    }
}

@Composable
fun MasteryItem(modifier: Modifier, mastery: MasteryUI, filterSearch: String, size: Dp) {
    val champ = mastery.champListItem
    var expanded by remember { mutableStateOf(false) }

    CommonChampionThumbnail(champ,
        Modifier.champThumbnail(false, { expanded = true }, size)) { imageBox ->
        Box(modifier.padding(margin), contentAlignment = Alignment.TopCenter) {
            Image(painter = painterResource(mastery.getMasteryItem()!!),
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .width(size)
                    .padding(size * 0.1f)
                    .clip(RoundedCornerShape(25)),
                contentDescription = null)
            imageBox()
            if (mastery.chestGranted)
                Image(
                    imageVector = IconsLoL.Chest,
                    contentDescription = null,
                    modifier = Modifier.size(size),
                )
            Text(text = champ.name.highlightSearch(filterSearch),
                modifier = Modifier
                    .padding(horizontal = 4.dp, vertical = size * 0.1f)
                    .align(Alignment.BottomCenter),
                style = typography.bodySmall,
                textAlign = TextAlign.Center)
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                Column(modifier = Modifier
                    .padding(horizontal = margin4, vertical = margin2)
                    .width(150.dp),
                    verticalArrangement = Arrangement.spacedBy(margin2)) {

                    // TODO put in Item
                    val total = mastery.championPointsUntilNextLevel + mastery.championPoints
                    val totalLastLevel =
                        mastery.championPointsUntilNextLevel + mastery.championPointsSinceLastLevel
                    val percentageProgress: Float =
                        (mastery.championPointsSinceLastLevel * 100).toFloat() / totalLastLevel.toFloat()

                    Row(verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(margin)) {
                        Column {
                            val strokeWidth = when {
                                (5..7).contains(mastery.championLevel) -> 2.dp
                                else -> 4.dp
                            }
                            Box(Modifier
                                .size(size / 2)
                                .border(
                                    width = strokeWidth,
                                    color = mastery
                                        .getColorMastery()
                                        .copy(alpha = 0.2f),
                                    shape = CircleShape
                                ),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(progress = percentageProgress / 100f,
                                    modifier = Modifier
                                        .size(size / 2)
                                        .rotate(180f),
                                    color = mastery.getColorMastery(),
                                    strokeWidth = strokeWidth)
                                Image(painter = painterResource(mastery.getMastery()),
                                    modifier = Modifier
                                        .padding(top = size / 2 * 0.1f)
                                        .size(size / 2),
                                    contentDescription = null)
                            }
                        }
                        Text(
                            text = champ.name,
                            fontWeight = FontWeight.Bold,
                            style = typography.headlineSmall,
                        )
                    }

                    MenuDefaults.Divider()

                    Text(
                        text = buildAnnotatedString {
                            append(mastery.championPoints.formatPoints())
                            if (mastery.championPoints != total) withStyle(
                                SpanStyle(color = colorScheme.onBackground.copy(0.5f))) {
                                append(" / ${total.formatPoints()}")
                            }
                            append(" pts")
                        },
                        style = typography.bodySmall
                    )
                    if (mastery.chestGranted) {
                        Row(verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(margin)) {
                            Icon(
                                painter = painterResource(id = R.mipmap.ic_chest),
                                tint = Color(0xFFC28F2C),
                                contentDescription = null,
                            )
                            Text(text = stringResource(id = R.string.chest_granted),
                                style = typography.bodySmall
                            )
                        }
                    }

                    if (mastery.tokensEarned > 0) {
                        Row(verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(margin)) {
                            Image(
                                painterResource(mastery.getMasteryToken(true)),
                                modifier = Modifier.size(12.dp),
                                contentDescription = null,
                            )
                            Text(
                                text = buildAnnotatedString {
                                    append(mastery.tokensEarned.toString())
                                    withStyle(
                                        SpanStyle(color = colorScheme.onBackground.copy(0.5f))) {
                                        append("/${
                                            when (mastery.championLevel) {
                                                6 -> "3"
                                                5 -> "2"
                                                else -> "0"
                                            }
                                        }")
                                    }
                                    append(" for the next level")
                                }, style = typography.bodySmall
                            )
                        }
                    }

                    Text(
                        text = "Last game: " + dateFormat.format(Date(mastery.lastPlayTime)),
                        style = typography.bodySmall.copy(
                            fontStyle = FontStyle.Italic,
                            color = colorScheme.onBackground.copy(0.5f),
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun MasteriesList(
    masteries: List<MasteryUI>,
    filters: MasteriesViewModel.Filters,
) {
    val listState = rememberLazyListState() // TODO apply in common
    val coroutineScope = rememberCoroutineScope()

    val size = 40.dp
    val items = masteries.filter(filters::predicate)
    LazyColumn(Modifier.fillMaxWidth(),
        state = listState,
        contentPadding = PaddingValues(bottom = 80.dp)) {
        itemsIndexed(items = items, key = { _, it -> it.championId }) { index, mastery ->
            MasteryItemList(modifier = Modifier
                .background(
                    if (index % 2 == 0) colorScheme.background
                    else colorScheme.surface.copy(0.5f)
                )
                .animateItemPlacement(),
                mastery = mastery,
                filterSearch = filters.search,
                size = size
            )
        }
    }

    val showButton by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 0
        }
    }

    ScrollToTopButton(
        visible = showButton,
        onClick = {
            coroutineScope.launch {
                if (ANIMATIONS_ON) listState.animateScrollToItem(index = 0)
                else listState.scrollToItem(index = 0)
            }
        }
    )
}

@Composable
fun ScrollToTopButton(visible: Boolean, onClick: () -> Unit) {
    if (visible) Box(modifier = Modifier
        .alpha(0.5f)
        .fillMaxWidth()
        .padding(margin / 2),
        contentAlignment = Alignment.TopCenter) {
        SmallFloatingActionButton(onClick = onClick,
            containerColor = colorScheme.surface) {
            MyIcon(icon = Icons.Rounded.ArrowUpward)
        }
    }
}

@SuppressLint("ConstantLocale")
val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

@Composable
fun MasteryItemList(modifier: Modifier, mastery: MasteryUI, filterSearch: String, size: Dp) {
    val champ = mastery.champListItem

    // TODO put in Item
    val total = mastery.championPointsUntilNextLevel + mastery.championPoints
    val totalLastLevel =
        mastery.championPointsUntilNextLevel + mastery.championPointsSinceLastLevel
    val percentageProgress: Float =
        (mastery.championPointsSinceLastLevel * 100).toFloat() / totalLastLevel.toFloat()

    Row(modifier.padding(vertical = margin2, horizontal = padHor),
        horizontalArrangement = Arrangement.spacedBy(margin2),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CommonChampionThumbnail(champ, Modifier.champThumbnail(size))

        Column(Modifier.weight(1f)) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(margin),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = champ.name.highlightSearch(filterSearch),
                    fontWeight = FontWeight.Bold,
                    style = typography.titleMedium,
                )
                if (mastery.chestGranted) Icon(
                    painter = painterResource(id = R.mipmap.ic_chest),
                    tint = Color(0xFFC28F2C),
                    contentDescription = null,
                )

                repeat(mastery.tokensEarned) {
                    Image(
                        painterResource(mastery.getMasteryToken(true)),
                        modifier = Modifier.size(12.dp),
                        contentDescription = null,
                    )
                }
            }

            Text(
                text = "Last game: " + dateFormat.format(Date(mastery.lastPlayTime)),
                style = typography.bodySmall.copy(
                    fontStyle = FontStyle.Italic,
                    color = colorScheme.onBackground.copy(0.5f),
                )
            )
        }

        Text(
            text = buildAnnotatedString {
                append(mastery.championPoints.formatPoints())
                if (mastery.championPoints != total) withStyle(
                    SpanStyle(color = colorScheme.onBackground.copy(0.5f))) {
                    append(" / ${total.formatPoints()}")
                }
                append(" pts")
            },
            style = typography.bodySmall
        )

        Column {
            val strokeWidth = when {
                (5..7).contains(mastery.championLevel) -> 2.dp
                else -> 4.dp
            }
            Box(Modifier
                .size(size)
                .border(
                    width = strokeWidth,
                    color = mastery
                        .getColorMastery()
                        .copy(alpha = 0.2f),
                    shape = CircleShape
                ),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(progress = percentageProgress / 100f,
                    modifier = Modifier
                        .size(size)
                        .rotate(180f),
                    color = mastery.getColorMastery(),
                    strokeWidth = strokeWidth)
                Image(painter = painterResource(mastery.getMastery()),
                    modifier = Modifier
                        .padding(top = size * 0.1f)
                        .size(size / 1),
                    contentDescription = null)
            }
        }
    }
}

fun String.highlightSearch(search: String) = addTagOn(
    value = search,
    tag = "mark",
    ignoreCase = true
).parseHtml()