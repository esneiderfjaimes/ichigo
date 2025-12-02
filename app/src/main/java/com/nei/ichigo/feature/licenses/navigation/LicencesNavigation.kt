package com.nei.ichigo.feature.licenses.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.nei.ichigo.feature.licenses.LicencesScreen
import com.nei.ichigo.navigation.Navigator
import kotlinx.serialization.Serializable

@Serializable
data object LicencesNavigation : NavKey

fun Navigator.navigateToLicences() {
    navigate(LicencesNavigation)
}

fun EntryProviderScope<NavKey>.licences(onBackPress: () -> Unit) {
    entry<LicencesNavigation> {
        LicencesScreen(onBackPress)
    }
}