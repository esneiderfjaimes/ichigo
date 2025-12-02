package com.nei.ichigo.feature.encyclopedia.icons.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.nei.ichigo.feature.encyclopedia.icons.IconsScreen
import kotlinx.serialization.Serializable

@Serializable
data object IconsRoute : NavKey

fun EntryProviderScope<NavKey>.icons() {
    entry<IconsRoute> {
        IconsScreen()
    }
}