package com.nei.ichigo.feature.encyclopedia.items.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.nei.ichigo.feature.encyclopedia.items.ItemsScreen
import kotlinx.serialization.Serializable

@Serializable
data object ItemsRoute

fun NavController.navigateToItems(navOptions: NavOptions) = navigate(route = ItemsRoute, navOptions)

fun NavGraphBuilder.items() {
    composable<ItemsRoute> {
        ItemsScreen()
    }
}