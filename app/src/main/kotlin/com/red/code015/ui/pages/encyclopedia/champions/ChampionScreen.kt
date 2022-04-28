@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class,
    ExperimentalFoundationApi::class)

package com.red.code015.ui.pages.encyclopedia.champions

import android.annotation.SuppressLint
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.red.code015.R
import com.red.code015.domain.ChampListItem
import com.red.code015.domain.RotationChamp
import com.red.code015.ui.common.Filter
import com.red.code015.ui.common.LoadingScreen
import com.red.code015.ui.components.ChampionThumbnail
import com.red.code015.ui.components.IconButtonBadge
import com.red.code015.ui.pages.encyclopedia.TopBar
import com.red.code015.ui.pages.encyclopedia.champions.ChampionsViewModel.Filters
import com.red.code015.ui.pages.home.screens.summoner.margin
import com.red.code015.ui.pages.home.screens.summoner.paddingHorizontal

val filtersByRotation =
    listOf(RotationChamp.Free, RotationChamp.FreeForNewPlayers)

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun ChampionScreen(
    viewModel: ChampionsViewModel = hiltViewModel(),
    topBar: TopBar,
    scrollBehavior: TopAppBarScrollBehavior,
    latestVersion: String,
) {
    viewModel.setup(latestVersion)
    val state by viewModel.state.collectAsState()
    var expanded by remember { mutableStateOf(false) }
    var filters by rememberSaveable { mutableStateOf(Filters(rotation = RotationChamp.Free)) }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            topBar({
                IconButtonBadge(
                    imageVector = Icons.Rounded.FilterList,
                    badgeIsVisible = filters.isFiltering,
                    visible = state.filterByTags.isNotEmpty()
                ) { expanded = !expanded }
            }, {
                Column(Modifier
                    .fillMaxWidth()
                    .background(colorScheme.surface)
                    .animateContentSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (expanded) {
                        TabRowDefaults.Divider(Modifier.padding(bottom = margin))

                        Text(text = stringResource(R.string.filter_by),
                            Modifier.padding(horizontal = paddingHorizontal + 4.dp),
                            style = typography.displaySmall,
                            fontWeight = FontWeight.Bold)

                        state.filterByTags.Filter(filters.tag, stringResource(R.string.role)) {
                            filters = filters.copy(tag = it)
                        }

                        filtersByRotation.Filter(
                            filter = filters.rotation,
                            title = stringResource(R.string.free_champion_rotation),
                        ) {
                            filters = filters.copy(rotation = it)
                        }

                        TabRowDefaults.Divider(Modifier.padding(top = margin))
                    }
                }
            })
        },
        content = {
            if (state.list.isEmpty()) LoadingScreen()
            else ChampsGrid(state.list, filters, state.footer)
        }
    )
}

@Composable
fun ChampsGrid(
    champs: List<ChampListItem>,
    filters: Filters,
    footer: String?,
) {
    val size = 70.dp
    val items = champs.filter(filters::predicate)
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = size),
        horizontalArrangement = Arrangement.SpaceAround,
        contentPadding = PaddingValues(
            start = 8.dp, end = 8.dp,
            top = 4.dp, bottom = 80.dp
        ),
        content = {
            if (items.isNotEmpty()) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Box(Modifier
                        .fillMaxWidth()
                        .padding(4.dp)) {
                        Text(text = "${items.size} champions",
                            modifier = Modifier.align(Alignment.Center),
                            color = colorScheme.onBackground.copy(0.325f),
                            style = typography.bodySmall)
                    }
                }
            }
            items(items = items, key = { it.name }) { champ ->
                ChampItem(Modifier.animateItemPlacement(), champ, size)
            }
            footer?.let {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Box(Modifier
                        .fillMaxWidth()
                        .padding(4.dp)) {
                        Text(text = it,
                            modifier = Modifier.align(Alignment.Center),
                            color = colorScheme.onBackground.copy(0.25f),
                            style = typography.bodySmall)
                    }
                }
            }
        }
    )
}

@Composable
private fun ChampItem(modifier: Modifier, champ: ChampListItem, size: Dp) {
    Column(
        modifier = modifier.padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ChampionThumbnail(
            bitmap = champ.bitmap,
            isInRotation = champ.rotation == RotationChamp.Free,
            championImage = champ.image.full,
            size
        )

        Text(text = champ.name,
            modifier = Modifier.padding(4.dp),
            style = typography.bodySmall,
            textAlign = TextAlign.Center)
    }
}

