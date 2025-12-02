package com.nei.ichigo.feature.encyclopedia.champions.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.nei.ichigo.feature.encyclopedia.champions.ChampionsScreen
import com.nei.ichigo.navigation.ListDetailScene
import kotlinx.serialization.Serializable

@Serializable
data object ChampionsRoute : NavKey

fun EntryProviderScope<NavKey>.champions(onChampionClick: (String) -> Unit) {
    entry<ChampionsRoute>(
        metadata = ListDetailScene.listPane()
    ) {
        ChampionsScreen(onChampionClick)
    }
}