package com.nei.ichigo.feature.encyclopedia.settings.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.dialog
import com.nei.ichigo.feature.encyclopedia.settings.SettingsDialog
import kotlinx.serialization.Serializable

@Serializable
data object EncyclopediaSettingsRoute

fun NavController.navigateToEncyclopediaSettings(navOptions: NavOptions) =
    navigate(route = EncyclopediaSettingsRoute, navOptions)

fun NavGraphBuilder.encyclopediaSettings(onDismiss: () -> Unit) {
    dialog<EncyclopediaSettingsRoute> {
        SettingsDialog(onDismiss)
    }
}