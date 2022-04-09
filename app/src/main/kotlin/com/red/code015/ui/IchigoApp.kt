@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)

package com.red.code015.ui

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.red.code015.data.AppSettings
import com.red.code015.data.model.toUI
import com.red.code015.ui.screens.home.HomeScreen
import com.red.code015.ui.screens.register.RegisterScreen
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun IchigoApp(
    appState: IchigoAppState = rememberIchigoAppState(),
) {
    val scope = rememberCoroutineScope()

    // TODO ("java.lang.IllegalStateException: There are multiple DataStores active for the same file:
    //  .../app-settings.json. You should either maintain your DataStore as a singleton or confirm
    //  that there is no two DataStore's active on the same file (by confirming that the scope is cancelled).")
    val settings by appState.appSettings.collectAsState(AppSettings(loading = true))

    if (settings.loading) return

    NavHost(
        navController = appState.navController,
        startDestination = Screen.Home.route
    ) {

        composable(Screen.Home.route) { backStackEntry ->

            var profilesByPlatform =
                settings.profiles.filter { it.platformID == settings.platformID }
            var profileSelected by remember { mutableStateOf(profilesByPlatform.firstOrNull()) }

            HomeScreen(
                platform = settings.platformID.toUI(),
                onPlatformChange = {
                    scope.launch {
                        appState.setLanguage(it.id)
                        profilesByPlatform = settings.profiles.filter { summoner ->
                            summoner.platformID == it.id
                        }
                        profileSelected = profilesByPlatform.firstOrNull()
                    }
                },
                goToRegister = { appState.navigateToRegister(backStackEntry) },
                goToSummonerDetails = { summonerName ->
                    appState.navigateToSummoner(summonerName, backStackEntry)
                },
                profiles = profilesByPlatform,
                profile = profileSelected,
                onProfileChange = { profileSelected = it },
                onProfileRemove = {
                    scope.launch {
                        appState.removeProfile(it.puuID)
                        profilesByPlatform = settings.profiles.filter { summoner ->
                            summoner.platformID == settings.platformID
                        }
                        profileSelected = profilesByPlatform.firstOrNull()
                    }
                },
                updateProfile = {
                    scope.launch {
                        appState.addOrUpdateProfile(it)
                        profileSelected = it
                    }
                }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                onBackPress = appState::navigateBack,
                platform = settings.platformID.toUI(),
                onPlatformChange = { scope.launch { appState.setLanguage(it.id) } },
                registerProfile = {
                    Log.e("TAGEs", "IchigoApp: $it")
                    scope.launch { appState.addOrUpdateProfile(it) }
                }
            )
        }

        composable(Screen.Summoner.route) {
            // TODO)
        }
    }
}