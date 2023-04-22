@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
    ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)

package com.red.code015.ui.pages.home.screens.summoner.masteries

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.red.code015.data.model.MasteryUI
import com.red.code015.data.model.sortByLevel
import com.red.code015.domain.PlatformID
import com.red.code015.ui.common.LoadingScreen
import com.red.code015.ui.components.IchigoScaffold
import com.red.code015.ui.components.material_modifications.ModalBottomSheetValue
import com.red.code015.ui.components.material_modifications.rememberModalBottomSheetState
import com.red.code015.ui.pages.home.screens.summoner.masteries.MasteriesViewModel.Filters
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun MasteriesScreen(
    platform: PlatformID,
    onBackPress: () -> Unit,
    summonerName: String,
    showView: ShowView = ShowView.Grid,
    viewModel: MasteriesViewModel = viewModel(),
) {
    if (summonerName.isBlank()) {
        onBackPress()
        return
    }

    LaunchedEffect(Unit) {
        viewModel.setup(platform, summonerName)
    }

    val state by viewModel.state.collectAsState()

    SummonerScreen(
        onBackPress = onBackPress,
        isLoading = state.isLoading,
        masteries = state.masteries,
        showView = showView
    )
}

@Composable
private fun SummonerScreen(
    scope: CoroutineScope = rememberCoroutineScope(),
    onBackPress: () -> Unit,
    isLoading: Boolean,
    masteries: List<MasteryUI>,
    showView: ShowView,
) {
    if (isLoading) {
        LoadingScreen(); return
    }

    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    var filters by rememberSaveable { mutableStateOf(Filters(sortBy = MasteryUI.SortBy.LastPlayTime)) }
    var selectShowView by rememberSaveable { mutableStateOf(ShowView.Grid) }

    IchigoScaffold(
        topBar = {
            MasteriesTopBar(
                onBackPress = onBackPress,
                selectShowView = selectShowView,
                onShowViewClick = { selectShowView = it },
                showSheet = { scope.launch { sheetState.show() } },
                filters = filters,
                onFiltersUpdate = { filters = it }
            )
        },
        content = {
            MasteriesContent(selectShowView, filters, masteries)
        },
        bottomSheet = {
            SheetHextechCraftingMastery(sheetState, masteries.sortByLevel())
        }
    )
}
