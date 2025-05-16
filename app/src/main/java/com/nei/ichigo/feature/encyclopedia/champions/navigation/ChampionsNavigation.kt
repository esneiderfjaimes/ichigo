package com.nei.ichigo.feature.encyclopedia.champions.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.nei.ichigo.feature.encyclopedia.champions.ChampionsScreen
import kotlinx.serialization.Serializable

@Serializable
data object ChampionsRoute

fun NavController.navigateToChampions(navOptions: NavOptions) =
    navigate(route = ChampionsRoute, navOptions)

fun NavGraphBuilder.champions() {
    composable<ChampionsRoute> {
        ChampionsScreen()
    }
}