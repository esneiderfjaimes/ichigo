package com.nei.ichigo.core.model

data class UserSettings(
    val darkThemeConfig: DarkThemeConfig,
    val useDynamicColor: Boolean,
    val langSelected: String?,
    val versionSelected: String?,
    val lastNavigationRoute: String?
)