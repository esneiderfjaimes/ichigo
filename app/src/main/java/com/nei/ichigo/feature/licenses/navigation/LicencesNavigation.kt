package com.nei.ichigo.feature.licenses.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.dialog
import com.nei.ichigo.feature.licenses.LicencesScreen
import kotlinx.serialization.Serializable

@Serializable
data object LicencesNavigation

fun NavController.navigateToLicences() = navigate(route = LicencesNavigation)

fun NavGraphBuilder.licencesScreen(onBackPress: () -> Unit) {
    dialog<LicencesNavigation> {
        LicencesScreen(onBackPress)
    }
}