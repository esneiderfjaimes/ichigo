package com.nei.ichigo.feature.encyclopedia.items.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.nei.ichigo.feature.encyclopedia.items.ItemsScreen
import kotlinx.serialization.Serializable

@Serializable
data object ItemsRoute : NavKey

fun EntryProviderScope<NavKey>.items() {
    entry<ItemsRoute> {
        ItemsScreen()
    }
}