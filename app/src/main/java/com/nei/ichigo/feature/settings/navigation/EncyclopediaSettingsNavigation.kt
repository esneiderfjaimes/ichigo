package com.nei.ichigo.feature.settings.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.nei.ichigo.feature.settings.SettingsScreen
import kotlinx.serialization.Serializable

@Serializable
data object EncyclopediaSettingsRoute : NavKey

fun EntryProviderScope<NavKey>.encyclopediaSettings(onLicenseClick: () -> Unit) {
    entry<EncyclopediaSettingsRoute> {
        SettingsScreen(onLicenseClick)
    }
}