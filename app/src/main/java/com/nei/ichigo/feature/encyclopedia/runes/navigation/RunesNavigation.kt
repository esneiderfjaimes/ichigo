package com.nei.ichigo.feature.encyclopedia.runes.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.nei.ichigo.feature.encyclopedia.runes.RunesScreen
import kotlinx.serialization.Serializable

@Serializable
data object RunesRoute

fun NavController.navigateToRunes(navOptions: NavOptions) =
    navigate(route = RunesRoute, navOptions)

fun NavGraphBuilder.runes() {
    composable<RunesRoute> {
        RunesScreen()
    }
}