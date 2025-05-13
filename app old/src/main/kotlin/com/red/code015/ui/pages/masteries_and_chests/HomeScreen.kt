@file:OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)

package com.red.code015.ui.pages.masteries_and_chests

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.red.code015.R
import com.red.code015.domain.Profile
import com.red.code015.ui.common.padHor
import com.red.code015.ui.components.IchigoScaffold

@Composable
fun HomeScreen(
    profiles: List<Profile>,
    viewModel: MasteriesAndChestsViewModel = hiltViewModel(),
    onProfileClick: (Profile) -> Unit,
) {
    val state by viewModel.state.collectAsState()

    IchigoScaffold(
        topBar = {
            SmallTopAppBar(
                title = {
                    Text(
                        stringResource(id = R.string.masteries_and_chests),
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.headlineSmall,
                    )
                }
            )
        },
        content = {
            SwipeRefresh(
                state = rememberSwipeRefreshState(state.isRefreshing),
                onRefresh = { viewModel.refresh() },
                swipeEnabled = false
            ) {
                LazyColumn(contentPadding = PaddingValues(bottom = 80.dp,
                    start = padHor,
                    end = padHor)) {
                    items(profiles) {
                        ProfileSection(it) { onProfileClick(it) }
                    }
                }
            }
        },
        bottomSheet = {}
    )
}