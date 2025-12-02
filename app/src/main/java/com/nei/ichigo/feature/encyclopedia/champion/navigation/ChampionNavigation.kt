package com.nei.ichigo.feature.encyclopedia.champion.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.nei.ichigo.feature.encyclopedia.champion.ChampionScreen
import com.nei.ichigo.navigation.ListDetailScene
import com.nei.ichigo.navigation.Navigator
import kotlinx.serialization.Serializable

@Serializable
data class ChampionRoute(val championId: String) : NavKey

fun Navigator.navigateToChampion(championId: String) =
    navigate(route = ChampionRoute(championId))

fun EntryProviderScope<NavKey>.champion(
    onBackPress: () -> Unit,
    navToSkinFullscreen: (String, String?) -> Unit
) {
    entry<ChampionRoute>(
        metadata = ListDetailScene.detailPane()
    ) { championRoute ->
        ChampionScreen(
            championId = championRoute.championId,
            navToSkinFullscreen = { navToSkinFullscreen(championRoute.championId, it) },
            onBackPress = onBackPress
        )
    }
}
