@file:OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)

package com.red.code015.ui.pages.home.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.red.code015.data.model.Platform
import com.red.code015.domain.Profile
import com.red.code015.ui.common.SheetPlatforms
import com.red.code015.ui.components.IchigoScaffold
import com.red.code015.ui.components.material_modifications.ModalBottomSheetValue
import com.red.code015.ui.components.material_modifications.rememberModalBottomSheetState
import com.red.code015.ui.pages.home.screens.home.cards.mysummoner.MySummonerSection
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    platform: Platform,
    onPlatformChange: (Platform) -> Unit = {},
    goToRegister: () -> Unit = {},
    goToSummonerDetails: (String) -> Unit = {},
    profiles: List<Profile>,
    profile: Profile?,
    onProfileChange: (Profile) -> Unit = {},
    onProfileRemove: (Profile) -> Unit = {},
    updateProfile: (Profile) -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel(),
) {
    viewModel.setup(platform, profile?.puuID)

    val state by viewModel.state.collectAsState()

    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()

    IchigoScaffold(
        topBar = {
            HomeTopAppBar(
                exception = state.exception,
                profiles = profiles,
                selectedProfile = profile,
                onProfileClick = { onProfileChange(it) },
                onAddProfileClick = goToRegister,
                platform = platform,
                onPlatformClick = { scope.launch { sheetState.show() } }
            )
        },
        content = { innerPadding ->
            SwipeRefresh(
                state = rememberSwipeRefreshState(state.isRefreshing),
                onRefresh = { viewModel.refresh(profile) },
            ) {
                HomeHeader(innerPadding) {
                    item { Spacer(Modifier.height(8.dp)) }
                    item {
                        MySummonerSection(
                            state = state.cardMySummoner,
                            profile = profile,
                            onRegisterClick = goToRegister,
                            onDetailClick = goToSummonerDetails,
                            onRemoveClick = { profile?.let { onProfileRemove(it) } },
                            updateProfile = updateProfile,
                        )
                    }
                    item {
                        Box(modifier = Modifier
                            .height(1000.dp)
                            .background(Color.Red)
                            .fillMaxWidth())
                    }
                }
            }
        },
        bottomSheet = {
            SheetPlatforms(
                state = sheetState,
                scope = scope,
                selectedPlatform = platform,
                onPlatformChange = { onPlatformChange(it) }
            )
        }
    )
}