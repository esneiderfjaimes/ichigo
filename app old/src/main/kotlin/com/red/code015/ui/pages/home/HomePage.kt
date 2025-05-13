package com.red.code015.ui.pages.home

import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import com.red.code015.data.AppSettings
import com.red.code015.data.model.toUI
import com.red.code015.ui.IchigoAppState
import com.red.code015.ui.IchigoAppViewModel
import com.red.code015.ui.Screen
import com.red.code015.ui.comp
import com.red.code015.ui.pages.home.screens.home.HomeScreen
import com.red.code015.ui.pages.home.screens.register.RegisterScreen
import com.red.code015.ui.pages.home.screens.summoner.SummonerScreen
import com.red.code015.ui.pages.home.screens.summoner.masteries.MasteriesScreen

@Composable
fun HomePage(
    viewModel: IchigoAppViewModel,
    appState: IchigoAppState,
) {
    val settings by viewModel.flowAppSettings.collectAsState(AppSettings(loading = true))
    if (settings.loading) return

    var profilesByPlatform =
        settings.profiles.filter { it.platformID == settings.platformID }
    var profileSelected by remember { mutableStateOf(profilesByPlatform.firstOrNull()) }

    NavHost(
        navController = appState.navController,
        startDestination = Screen.Home.route
    ) {

        comp(Screen.Home) { backStackEntry ->
            HomeScreen(
                platform = settings.platformID.toUI(),
                onPlatformChange = {
                    viewModel.setLanguage(it.id)
                    profilesByPlatform = settings.profiles.filter { summoner ->
                        summoner.platformID == it.id
                    }
                    profileSelected = profilesByPlatform.firstOrNull()
                },
                goToRegister = { appState.navigateToRegister(backStackEntry) },
                goToSummonerDetails = { platform, summonerName ->
                    appState.navigateToSummoner(platform, summonerName, backStackEntry)
                },
                profiles = profilesByPlatform,
                profile = profileSelected,
                onProfileChange = { profileSelected = it },
                onProfileRemove = {
                    viewModel.removeProfile(it.puuID)
                    profilesByPlatform = settings.profiles.filter { summoner ->
                        summoner.platformID == settings.platformID
                    }
                    profileSelected = profilesByPlatform.firstOrNull()
                },
                updateProfile = {
                    viewModel.addOrUpdateProfile(it)
                    profileSelected = it
                }
            )
        }

        comp(Screen.Register) {
            RegisterScreen(
                onBackPress = appState::navigateBack,
                platform = settings.platformID.toUI(),
                onPlatformChange = { viewModel.setLanguage(it.id) },
                registerProfile = viewModel::addOrUpdateProfile
            )
        }


        comp(Screen.Summoner) { backStackEntry ->
            val summonerName = backStackEntry.arguments?.getString("name") ?: ""
            SummonerScreen(
                platform = settings.platformID,
                onBackPress = appState::navigateBack,
                onMasteryMorePress = { platform, name ->
                    appState.navMasteries.to(Screen.Masteries.createRoute(platform, name),
                        backStackEntry)
                },
                summonerName = summonerName
            )
        }

        comp(Screen.Masteries) { backStackEntry ->
            MasteriesScreen(
                platform = settings.platformID,
                onBackPress = appState::navigateBack,
                summonerName = backStackEntry.arguments?.getString("summonerName") ?: "",
                showView = Screen.Masteries.getShowView(backStackEntry)
            )
        }
    }
}