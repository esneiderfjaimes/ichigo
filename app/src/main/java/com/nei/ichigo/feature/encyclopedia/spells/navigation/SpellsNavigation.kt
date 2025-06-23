package com.nei.ichigo.feature.encyclopedia.spells.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.nei.ichigo.feature.encyclopedia.spells.SpellsScreen
import kotlinx.serialization.Serializable

@Serializable
data object SpellsRoute

fun NavController.navigateToSpells(navOptions: NavOptions) =
    navigate(route = SpellsRoute, navOptions)

fun NavGraphBuilder.spells() {
    composable<SpellsRoute> {
        SpellsScreen()
    }
}