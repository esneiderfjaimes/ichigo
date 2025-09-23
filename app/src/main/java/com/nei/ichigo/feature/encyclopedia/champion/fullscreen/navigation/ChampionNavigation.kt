package com.nei.ichigo.feature.encyclopedia.champion.fullscreen.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.nei.ichigo.feature.encyclopedia.champion.fullscreen.SkinFullscreenScreen
import kotlinx.serialization.Serializable

@Serializable
data class SkinFullscreenRoute(val championId: String, val selectedSkinId: String?)

fun NavController.navigateToSkinFullscreen(championId: String, selectedSkinId: String?) =
    navigate(route = SkinFullscreenRoute(championId, selectedSkinId))

fun NavGraphBuilder.skinFullscreen(onBackPress: () -> Unit) {
    composable<SkinFullscreenRoute> { entry ->
        val championRoute = entry.toRoute<SkinFullscreenRoute>()
        SkinFullscreenScreen(
            championId = championRoute.championId,
            selectedSkinId = championRoute.selectedSkinId,
            onBackPress = onBackPress
        )
    }
}