package com.nei.ichigo.feature.encyclopedia.runes.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.nei.ichigo.feature.encyclopedia.runes.RunesScreen
import kotlinx.serialization.Serializable

@Serializable
data object RunesRoute : NavKey

fun EntryProviderScope<NavKey>.runes() {
    entry<RunesRoute> {
        RunesScreen()
    }
}