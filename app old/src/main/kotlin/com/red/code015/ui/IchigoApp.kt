package com.red.code015.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.ImportContacts
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.red.code015.R
import com.red.code015.ui.pages.encyclopedia.EncyclopediaPage
import com.red.code015.ui.pages.home.HomePage
import com.red.code015.ui.pages.masteries_and_chests.MasteriesAndChestsPage
import com.red.code015.ui.theme.icons.IconsLoL
import com.red.code015.ui.theme.icons.Mastery
import com.red.code015.ui.theme.icons.Rank

@Composable
fun IchigoApp(
    viewModel: IchigoAppViewModel,
    appState: IchigoAppState = rememberIchigoAppState(),
) {
    IchigoScaffold { page ->
        when (page) {
            Page.Home -> HomePage(viewModel = viewModel, appState = appState)
            Page.MasteriesAndChests ->
                MasteriesAndChestsPage(
                    viewModel = viewModel,
                    appState = appState
                )
            Page.Encyclopedia -> EncyclopediaPage()
            Page.Settings -> Unit
        }
    }
}

enum class Page(val icon: ImageVector, val resId: Int) {
    Home(
        icon = Icons.Rounded.Home,
        resId = R.string.home
    ),
    MasteriesAndChests(
        icon = IconsLoL.Mastery,
        resId = R.string.masteries
    ),
    Encyclopedia(
        icon = Icons.Rounded.ImportContacts,
        resId = R.string.encyclopedia
    ),
    Settings(
        Icons.Rounded.Settings,
        R.string.settings
    )
}