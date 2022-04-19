package com.red.code015.ui.pages.home

import androidx.compose.runtime.*
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.red.code015.data.AppSettings
import com.red.code015.data.model.toUI
import com.red.code015.ui.IchigoAppState
import com.red.code015.ui.IchigoAppViewModel
import com.red.code015.ui.Screen
import com.red.code015.ui.pages.home.screens.home.HomeScreen
import com.red.code015.ui.pages.home.screens.register.RegisterScreen
import com.red.code015.ui.pages.home.screens.summoner.SummonerScreen

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

        composable(route = Screen.Home.route) { backStackEntry ->
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
                goToSummonerDetails = { summonerName ->
                    appState.navigateToSummoner(summonerName, backStackEntry)
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

        composable(route = Screen.Register.route) {
            RegisterScreen(
                onBackPress = appState::navigateBack,
                platform = settings.platformID.toUI(),
                onPlatformChange = { viewModel.setLanguage(it.id) },
                registerProfile = viewModel::addOrUpdateProfile
            )
        }

        composable(
            route = Screen.Summoner.route,
            arguments = listOf(navArgument("name") { type = NavType.StringType })
        ) { backStackEntry ->
            SummonerScreen(
                platform = settings.platformID,
                onBackPress = appState::navigateBack,
                summonerName = backStackEntry.arguments?.getString("name") ?: ""
            )
        }
    }
}