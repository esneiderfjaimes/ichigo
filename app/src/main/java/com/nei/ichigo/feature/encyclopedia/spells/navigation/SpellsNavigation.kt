package com.nei.ichigo.feature.encyclopedia.spells.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.nei.ichigo.feature.encyclopedia.spells.SpellsScreen
import kotlinx.serialization.Serializable

@Serializable
data object SpellsRoute : NavKey

fun EntryProviderScope<NavKey>.spells() {
    entry<SpellsRoute> {
        SpellsScreen()
    }
}