package com.nei.ichigo.feature.encyclopedia.champion.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.nei.ichigo.feature.encyclopedia.champion.ChampionScreen
import kotlinx.serialization.Serializable

@Serializable
data class ChampionRoute(val championId: String)

fun NavController.navigateToChampion(championId: String) =
    navigate(route = ChampionRoute(championId))

fun NavGraphBuilder.champion(
    onBackPress: () -> Unit,
    navToSkinFullscreen: (String, String?) -> Unit
) {
    composable<ChampionRoute> { entry ->
        val championRoute = entry.toRoute<ChampionRoute>()
        ChampionScreen(
            championId = championRoute.championId,
            navToSkinFullscreen = { navToSkinFullscreen(championRoute.championId, it) },
            onBackPress = onBackPress
        )
    }
}