@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class,
    ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)

package com.red.code015.ui.pages.encyclopedia.champions

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.red.code015.data.model.toUI
import com.red.code015.domain.ChampListItem
import com.red.code015.domain.RotationChamp
import com.red.code015.ui.common.CommonChampsGrid
import com.red.code015.ui.common.LoadingScreen
import com.red.code015.ui.components.CommonChampionThumbnail
import com.red.code015.ui.components.IchigoScaffold
import com.red.code015.ui.components.IconButtonBadge
import com.red.code015.ui.components.champThumbnail
import com.red.code015.ui.components.material_modifications.ModalBottomSheetValue
import com.red.code015.ui.components.material_modifications.rememberModalBottomSheetState
import com.red.code015.ui.pages.encyclopedia.TopBar
import com.red.code015.ui.pages.encyclopedia.champions.ChampionsViewModel.Filters
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

val filtersByRotation =
    listOf(RotationChamp.Free, RotationChamp.FreeForNewPlayers)

@Composable
fun ChampionScreen(
    viewModel: ChampionsViewModel = hiltViewModel(),
    scope: CoroutineScope,
    topBar: TopBar,
    scrollBehavior: TopAppBarScrollBehavior,
    latestVersion: String,
) {
    viewModel.setup(latestVersion)

    val state by viewModel.state.collectAsState()
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    var filters by rememberSaveable { mutableStateOf(Filters()) }

    IchigoScaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            topBar({
                IconButtonBadge(
                    imageVector = Icons.Rounded.FilterList,
                    badgeIsVisible = filters.isFiltering,
                    visible = state.filterByTags.isNotEmpty()
                ) { scope.launch { sheetState.show() } }
            }, {})
        },
        content = {
            if (state.list.isEmpty()) LoadingScreen()
            else ChampsGrid(state.list, filters, state.footer)
        },
        bottomSheet = {
            SheetFilters(sheetState, filters, state.filterByTags) {
                filters = it
            }
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
    CommonChampsGrid(items.size, size, footer) {
        items(items = items, key = { it.name }) { champ ->
            ChampItem(Modifier.animateItemPlacement(), champ, size)
        }
    }
}

@Composable
fun ChampItem(modifier: Modifier, champ: ChampListItem, size: Dp) {
    Column(
        modifier = modifier.padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ChampThumbEncyclopedia(champ, size)

        Text(text = champ.name,
            modifier = Modifier.padding(4.dp),
            style = typography.bodySmall,
            textAlign = TextAlign.Center)
    }
}

@Composable
fun ChampThumbEncyclopedia(
    champ: ChampListItem,
    size: Dp,
    onClick: () -> Unit = {},
) {
    CommonChampionThumbnail(champ.toUI(), Modifier.champThumbnail(
        isInRotation = champ.rotation == RotationChamp.Free,
        onClick = onClick,
        size = size
    ))
}
