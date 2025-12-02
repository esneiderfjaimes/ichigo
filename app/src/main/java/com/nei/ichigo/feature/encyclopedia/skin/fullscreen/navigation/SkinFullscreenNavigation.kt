package com.nei.ichigo.feature.encyclopedia.skin.fullscreen.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.nei.ichigo.feature.encyclopedia.skin.fullscreen.SkinFullscreenScreen
import com.nei.ichigo.navigation.Navigator
import kotlinx.serialization.Serializable

@Serializable
data class SkinFullscreenRoute(val championId: String, val selectedSkinId: String?) : NavKey

fun Navigator.navigateToSkinFullscreen(championId: String, selectedSkinId: String?) =
    navigate(route = SkinFullscreenRoute(championId, selectedSkinId))

fun EntryProviderScope<NavKey>.skinFullscreen(onBackPress: () -> Unit) {
    entry<SkinFullscreenRoute> { skinFullscreenRoute ->
        SkinFullscreenScreen(
            championId = skinFullscreenRoute.championId,
            selectedSkinId = skinFullscreenRoute.selectedSkinId,
            onBackPress = onBackPress
        )
    }
}