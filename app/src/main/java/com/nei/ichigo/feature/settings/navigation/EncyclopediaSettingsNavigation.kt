package com.nei.ichigo.feature.settings.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.nei.ichigo.feature.settings.SettingsScreen
import kotlinx.serialization.Serializable

@Serializable
data object EncyclopediaSettingsRoute

fun NavController.navigateToEncyclopediaSettings(navOptions: NavOptions) =
    navigate(route = EncyclopediaSettingsRoute, navOptions)

fun NavGraphBuilder.encyclopediaSettings(onLicenseClick: () -> Unit) {
    composable<EncyclopediaSettingsRoute> {
        SettingsScreen(onLicenseClick)
    }
}