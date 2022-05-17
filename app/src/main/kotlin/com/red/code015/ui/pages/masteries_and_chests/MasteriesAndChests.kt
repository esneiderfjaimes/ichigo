package com.red.code015.ui.pages.masteries_and_chests

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import com.red.code015.data.AppSettings
import com.red.code015.ui.IchigoAppState
import com.red.code015.ui.IchigoAppViewModel
import com.red.code015.ui.Screen
import com.red.code015.ui.Screen.Masteries
import com.red.code015.ui.comp
import com.red.code015.ui.pages.home.screens.summoner.masteries.MasteriesScreen

@Composable
fun MasteriesAndChestsPage(
    viewModel: IchigoAppViewModel,
    appState: IchigoAppState,
) {
    val nav = appState.navMasteries

    val settings by viewModel.flowAppSettings.collectAsState(AppSettings(loading = true))
    if (settings.loading) return

    NavHost(
        navController = nav.controller,
        startDestination = Screen.Home.route
    ) {

        comp(Screen.Home) { backStackEntry ->
            HomeScreen(
                profiles = settings.profiles
            ) {
                nav.to(
                    route = Masteries.createRoute(
                        platform = it.platformID,
                        summonerName = it.name
                    ),
                    from = backStackEntry
                )
            }
        }

        comp(Masteries) { backStackEntry ->
            MasteriesScreen(
                platform = settings.platformID,
                onBackPress = nav::navigateBack,
                summonerName = backStackEntry.arguments?.getString("summonerName") ?: "",
                showView = Masteries.getShowView(backStackEntry)
            )
        }
    }
}