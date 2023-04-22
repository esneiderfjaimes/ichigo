@file:OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterialApi::class)

package com.red.code015.ui.pages.home.screens.register

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.red.code015.data.model.Platform
import com.red.code015.data.model.Platforms
import com.red.code015.domain.Profile
import com.red.code015.ui.common.SheetPlatforms
import com.red.code015.ui.components.IchigoScaffold
import com.red.code015.ui.components.material_modifications.ModalBottomSheetValue
import com.red.code015.ui.components.material_modifications.rememberModalBottomSheetState
import com.red.code015.ui.pages.home.screens.register.RegisterViewModel.Event
import com.red.code015.ui.pages.home.screens.register.RegisterViewModel.State
import com.red.code015.ui.pages.home.screens.register.RegisterViewModel.State.SearchBy
import com.red.code015.ui.theme.IchigoTheme
import com.red.code015.utils.toast
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(
    onBackPress: () -> Unit,
    platform: Platform,
    onPlatformChange: (Platform) -> Unit,
    registerProfile: (Profile) -> Unit,
    viewModel: RegisterViewModel = viewModel(),
) {
    viewModel.setup(platform)

    val context: Context = LocalContext.current
    viewModel.event.observe(LocalLifecycleOwner.current) { event ->
        viewModel.setLoading(false)
        when (event) {
            is Event.ProfileFound -> {
                registerProfile(event.profile)
                onBackPress()
            }
            is Event.Error -> context.toast(event.exception.message)
            Event.ProfileNotFound -> context.toast("Profile not found")
        }
    }

    val collectAsState by viewModel.state.collectAsState()
    RegisterScreen(
        viewState = collectAsState,
        onBackPress = onBackPress,
        platform = platform,
        onPlatformChange = onPlatformChange,
        onSearchByChange = viewModel::setSearchBy,
        onDoneBySummonerNameClick = viewModel::searchProfileBySummonerName,
        onDoneByRiotIDClick = viewModel::searchProfileByRiotID,
    )
}

@SuppressLint("UnrememberedMutableState")
@Composable
private fun RegisterScreen(
    viewState: State,
    onBackPress: () -> Unit = {},
    platform: Platform,
    onPlatformChange: (Platform) -> Unit = {},
    onSearchByChange: (SearchBy) -> Unit = {},
    onDoneBySummonerNameClick: (String) -> Unit = {},
    onDoneByRiotIDClick: (String, String) -> Unit = { _, _ -> },
) {
    val state = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()

    IchigoScaffold(
        topBar = {
            RegisterTopAppBar(onBackPress = onBackPress,
                platform = platform,
                onPlatformClick = { scope.launch { state.show() } })
        },
        content = {
            RegisterForm(viewState = viewState,
                sheetState = state,
                onSearchByChange = onSearchByChange,
                onDoneBySummonerNameClick = onDoneBySummonerNameClick,
                onDoneByRiotIDClick = onDoneByRiotIDClick)
        },
        bottomSheet = {
            SheetPlatforms(state = state,
                scope = scope,
                selectedPlatform = platform,
                onPlatformChange = { onPlatformChange(it) })
        }
    )
}

@Composable
fun SearchByDropdown(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onItemClick: (SearchBy) -> Unit,
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
    ) {
        SearchBy.values().forEach {
            DropdownMenuItem(text = {
                Text(text = stringResource(id = it.idRes))
            }, onClick = {
                onItemClick(it)
                onDismissRequest()
            })
        }
    }
}

@Preview
@Composable
private fun RegisterScreenPreview() {
    IchigoTheme {
        RegisterScreen(
            viewState = State(
                isLoading = false,
                searchBy = SearchBy.SummonerName
            ),
            platform = Platforms[1],
        )
    }
}