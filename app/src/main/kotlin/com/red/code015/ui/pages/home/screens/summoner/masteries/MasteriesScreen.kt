@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
    ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)

package com.red.code015.ui.pages.home.screens.summoner.masteries

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.hilt.navigation.compose.hiltViewModel
import com.red.code015.data.model.MasteryUI
import com.red.code015.data.model.sortByLevel
import com.red.code015.domain.PlatformID
import com.red.code015.ui.common.LoadingScreen
import com.red.code015.ui.components.IchigoScaffold
import com.red.code015.ui.components.material_modifications.ModalBottomSheetState
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
    viewModel: MasteriesViewModel = hiltViewModel(),
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

    // TODO create stateTopBar
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    var filters by rememberSaveable { mutableStateOf(Filters()) }
    var action by rememberSaveable { mutableStateOf(Action.Booty) }
    var selectShowView by rememberSaveable { mutableStateOf(showView) }

    // TODO move top when filter change

    IchigoScaffold(
        topBar = {
            MasteriesTopBar(
                onBackPress = onBackPress,
                selectShowView = selectShowView,
                onShowViewClick = { selectShowView = it },
                changeAction = {
                    action = it
                    scope.launch { sheetState.show() }
                },
                filters = filters,
                onFiltersUpdate = { filters = it }
            )
        },
        content = {
            MasteriesContent(selectShowView, filters, masteries)
        },
        bottomSheet = {
            MasteriesBottomSheet(action, sheetState, filters, masteries) { filters = it }
        }
    )
}

@Composable
fun MasteriesBottomSheet(
    action: Action,
    sheetState: ModalBottomSheetState,
    filters: Filters,
    masteries: List<MasteryUI>,
    onFilterChange: (Filters) -> Unit,
) {
    when (action) {
        Action.Filter -> SheetFilters(sheetState, filters, onFilterChange)
        Action.Order -> SheetSorters(sheetState, filters, onFilterChange)
        Action.Booty -> SheetHextechCraftingMastery(sheetState, masteries.sortByLevel())
        else -> Unit
    }
}
