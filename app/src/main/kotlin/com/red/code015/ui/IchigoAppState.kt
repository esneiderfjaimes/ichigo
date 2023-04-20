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
import androidx.lifecycle.Lifecycle
import androidx.navigation.*
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.red.code015.domain.PlatformID
import com.red.code015.ui.pages.home.screens.summoner.masteries.ShowView

const val uri = "https://ichi.go"

/**
 * List of screens for [IchigoApp]
 */
sealed class Screen(
    val route: String,
    val args: List<NamedNavArgument> = listOf(),
    val deepLinks: List<NavDeepLink> = listOf(),
) {
    object Home : Screen("home")
    object Register : Screen("register")
    object Summoner :
        Screen("{platform}/summoner/{name}",
            listOf(navArgument("name") { type = NavType.StringType })) {
        fun createRoute(platform: PlatformID, name: String) = "${platform.name}/summoner/$name"
    }

    object Masteries : Screen(
        route = "{platform}/summoner/{summonerName}/masteries?view={viewMode}",
        args = listOf(
            navArgument("platform") { type = NavType.StringType },
            navArgument("summonerName") { type = NavType.StringType },
            navArgument("viewMode") { type = NavType.StringType },
        ),
        listOf(navDeepLink {
            uriPattern = "$uri/{platform}/summoner/{summonerName}/masteries?view={viewMode}"
        })
    ) {
        private val defaultShowView = ShowView.Grid

        fun createRoute(
            platform: PlatformID,
            summonerName: String,
            showView: ShowView = defaultShowView,
        ) = "${platform.name}/summoner/$summonerName/masteries?view=${showView.name}"

        fun getShowView(backStackEntry: NavBackStackEntry)
                : ShowView = backStackEntry.arguments?.getString("viewMode")?.let {
            try {
                when (it.lowercase()) {
                    ShowView.Grid.name.lowercase() -> ShowView.Grid
                    ShowView.List.name.lowercase() -> ShowView.List
                    else -> defaultShowView
                }
            } catch (e: Exception) {
                defaultShowView
            }

        } ?: defaultShowView
    }
}

fun NavGraphBuilder.comp(
    screen: Screen, content: @Composable (NavBackStackEntry) -> Unit,
) {
    composable(screen.route, screen.args, screen.deepLinks, content = content)
}

@Composable
fun rememberIchigoAppState(
    navController: NavHostController = rememberNavController(),
    navControllerMasteries: NavHostController = rememberNavController(),
    context: Context = LocalContext.current,
) = remember(navController, navControllerMasteries, context) {
    IchigoAppState(navController, navControllerMasteries, context)
}

class IchigoAppState(
    val navController: NavHostController,
  private  val navControllerMasteries: NavHostController,
    context: Context,
) {

    val navMasteries = Directions(navControllerMasteries)

    fun navigateToSummoner(platform: PlatformID, summonerName: String, from: NavBackStackEntry) {
        // In order to discard duplicated navigation events, we check the Lifecycle
     //   if (from.lifecycleIsResumed()) {
            val encodedUri = Uri.encode(summonerName)
            navController.navigate(Screen.Summoner.createRoute(platform, encodedUri))
    //    }
    }

    fun navigateTo(route: String, from: NavBackStackEntry) {
        // In order to discard duplicated navigation events, we check the Lifecycle
    //    if (from.lifecycleIsResumed()) {
            navController.navigate(route)
   //     }
    }

    fun navigateBack() {
        navController.popBackStack()
    }

    fun navigateToRegister(from: NavBackStackEntry? = null) {
        // In order to discard duplicated navigation events, we check the Lifecycle
     //   if (from == null || from.lifecycleIsResumed()) {
            navController.navigate(Screen.Register.route)
    //    }
    }

}

class Directions(val controller: NavHostController){
    fun navigateBack() {
        controller.popBackStack()
    }
    fun to(route: String, from: NavBackStackEntry) {
        // In order to discard duplicated navigation events, we check the Lifecycle
      //  if (from.lifecycleIsResumed()) {
            controller.navigate(route)
    //    }
    }
}