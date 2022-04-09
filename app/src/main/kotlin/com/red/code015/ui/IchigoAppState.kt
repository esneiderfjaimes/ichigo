/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.red.code015.ui

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.dataStore
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.red.code015.data.AppSettingsSerializer
import com.red.code015.domain.PlatformID
import com.red.code015.domain.Profile
import com.red.code015.data.model.Platform
import com.red.code015.data.model.Platforms

/**
 * List of screens for [IchigoApp]
 */
sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Register : Screen("register")
    object Summoner : Screen("summoner/{name}") {
        fun createRoute(name: String) = "summoner/$name"
    }
}

@Composable
fun rememberIchigoAppState(
    navController: NavHostController = rememberNavController(),
    context: Context = LocalContext.current,
) = remember(navController, context) {
    IchigoAppState(navController, context, Platforms[1])
}

class IchigoAppState(
    val navController: NavHostController,
    context: Context,
    var platform: Platform,
) {
    private val Context.dataStore by dataStore("app-settings.json", AppSettingsSerializer)
    private val settingsDataStore = context.dataStore
    val appSettings = settingsDataStore.data

    suspend fun setLanguage(platformID: PlatformID) {
        settingsDataStore.updateData { it.copy(platformID = platformID) }
    }

    suspend fun addOrUpdateProfile(profile: Profile) {
        settingsDataStore.updateData { it.copy(profiles = it.addOrUpdateProfile(profile)) }
    }

    suspend fun removeProfile(puuID: String) {
        settingsDataStore.updateData {
            it.removeProfile(puuID)
        }
    }

    fun navigateToSummoner(summonerName: String, from: NavBackStackEntry) {
        // In order to discard duplicated navigation events, we check the Lifecycle
        if (from.lifecycleIsResumed()) {
            val encodedUri = Uri.encode(summonerName)
            navController.navigate(Screen.Summoner.createRoute(encodedUri))
        }
    }

    fun navigateBack() {
        navController.popBackStack()
    }

    fun navigateToRegister(from: NavBackStackEntry? = null) {
        // In order to discard duplicated navigation events, we check the Lifecycle
        if (from == null || from.lifecycleIsResumed()) {
            navController.navigate(Screen.Register.route)
        }
    }


}

/**
 * If the lifecycle is not resumed it means this NavBackStackEntry already processed a nav event.
 *
 * This is used to de-duplicate navigation events.
 */
private fun NavBackStackEntry.lifecycleIsResumed() =
    this.lifecycle.currentState == Lifecycle.State.RESUMED
