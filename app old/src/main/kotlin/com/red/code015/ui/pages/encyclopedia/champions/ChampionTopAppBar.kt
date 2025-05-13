@file:OptIn(ExperimentalMaterialApi::class)

package com.red.code015.ui.pages.encyclopedia.champions

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.red.code015.R
import com.red.code015.ui.common.Filter
import com.red.code015.ui.components.BottomSheet
import com.red.code015.ui.components.material_modifications.ModalBottomSheetState
import com.red.code015.ui.pages.home.screens.summoner.paddingHorizontal
import com.red.code015.ui.pages.home.screens.summoner.paddingVertical

@Composable
fun SheetFilters(
    sheetState: ModalBottomSheetState,
    filters: ChampionsViewModel.Filters,
    filterByTags: List<String>,
    onFilterChange: (ChampionsViewModel.Filters) -> Unit,
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

                filterByTags.Filter(filters.tag, stringResource(R.string.role)) {
                    onFilterChange(filters.copy(tag = it))
                }

                filtersByRotation.Filter(
                    filter = filters.rotation,
                    title = stringResource(R.string.free_champion_rotation),
                ) {
                    onFilterChange(filters.copy(rotation = it))
                }

                Spacer(modifier = Modifier.height(paddingVertical))
            }
        }
    )
}