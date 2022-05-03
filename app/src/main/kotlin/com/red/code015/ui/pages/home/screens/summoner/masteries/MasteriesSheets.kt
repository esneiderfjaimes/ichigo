@file:OptIn(ExperimentalMaterialApi::class)

package com.red.code015.ui.pages.home.screens.summoner.masteries

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.red.code015.R
import com.red.code015.data.model.MasteryUI
import com.red.code015.data.model.getMasteryToken
import com.red.code015.ui.common.*
import com.red.code015.ui.components.BottomSheet
import com.red.code015.ui.components.CommonChampionThumbnail
import com.red.code015.ui.components.material_modifications.ModalBottomSheetState
import com.red.code015.ui.components.red.menu.Menu
import com.red.code015.ui.pages.home.screens.summoner.paddingHorizontal
import com.red.code015.ui.pages.home.screens.summoner.paddingVertical
import com.red.code015.utils.gradientShadow

@Composable
fun SheetFilters(
    sheetState: ModalBottomSheetState,
    filters: MasteriesViewModel.Filters,
    onFilterChange: (MasteriesViewModel.Filters) -> Unit,
) {
    BottomSheet(
        state = sheetState,
        sheetContent = {
            Column(Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .animateContentSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)) {

                Spacer(modifier = Modifier.height(paddingVertical))

                Text(text = stringResource(R.string.filter_by),
                    Modifier.padding(horizontal = paddingHorizontal + 4.dp),
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold)

                val stringResource = stringResource(R.string.level)
                (7 downTo 1).toList().Filter(filters.championLevel,
                    chipText = (7 downTo 1).map { "Lvl. $it" },
                    stringResource) {
                    onFilterChange(filters.copy(championLevel = it))
                }

                filterByChestGranted.Filter(
                    filter = filters.chestGranted,
                    chipText = stringArrayResource(R.array.filtersByChest).toList(),
                    title = stringResource(R.string.chest),
                ) {
                    onFilterChange(filters.copy(chestGranted = it))
                }

                Spacer(modifier = Modifier.height(paddingVertical))
            }
        }
    )
}

@Composable
fun SheetSorters(
    sheetState: ModalBottomSheetState,
    filters: MasteriesViewModel.Filters,
    onFilterChange: (MasteriesViewModel.Filters) -> Unit,
) {
    BottomSheet(
        state = sheetState,
        sheetContent = {
            Column(Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .animateContentSize(),
                verticalArrangement = Arrangement.spacedBy(margin2)) {

                SpaceV(padVer)

                Text(text = stringResource(R.string.sort_by),
                    Modifier.padding(horizontal = paddingHorizontal + 4.dp),
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold)

                Menu(items = mapOf(
                    MasteryUI.SortBy.Points to stringResource(R.string.mastery_points),
                    MasteryUI.SortBy.Level to stringResource(R.string.mastery_level),
                    MasteryUI.SortBy.Alpha to stringResource(R.string.champion_name)
                ), selected = filters.sortBy) {
                    onFilterChange(filters.copy(
                        sortBy = it,
                        isReverse = it.defaultIsReverse
                    ))
                }

                Menu(items = mapOf(
                    true to stringResource(R.string.ascendant_order),
                    false to stringResource(R.string.descending_order)
                ), selected = filters.isReverse) {
                    onFilterChange(filters.copy(isReverse = it))
                }

                SpaceV(padVer)
            }
        }
    )
}

@Composable
fun SheetHextechCraftingMastery(sheetState: ModalBottomSheetState, masteries: List<MasteryUI>) {
    BottomSheet(
        state = sheetState,
        sheetContent = {
            val size = 70.dp
            val items = masteries.filter { it.tokensEarned > 0 }
            CommonGrid(items.size, size, contentPadding = PaddingValues(
                horizontal = padHor,
                vertical = padVer
            )) {
                items(items = items, key = { it.championId }) { mastery ->
                    MasteryTokenItem(mastery, size)
                }
            }
        }
    )
}

@Composable
private fun MasteryTokenItem(mastery: MasteryUI, size: Dp) {
    val champ = mastery.champListItem.copy(bitmap = null)
    CommonChampionThumbnail(champ,
        Modifier
            .size(size)
            .padding(size * 0.05f)) { imageBox ->
        Column(Modifier
            .padding(margin),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val fontSize = (size * 0.25f).value.sp
            Box(contentAlignment = Alignment.Center) {
                imageBox()
                Image(painter = painterResource(mastery.getMasteryToken()),
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .width(size),
                    contentDescription = null)
                if (mastery.tokensEarned > 1)
                    Box(modifier = Modifier
                        .gradientShadow()
                        .align(Alignment.BottomEnd)) {
                        Text(text = mastery.tokensEarned.toString(),
                            modifier = Modifier
                                .padding((fontSize / 3).value.dp, size * 0.125f),
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = fontSize),
                            textAlign = TextAlign.Center)
                    }
            }
            Text(text = champ.name,
                modifier = Modifier
                    .padding(4.dp),
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center)
        }
    }
}