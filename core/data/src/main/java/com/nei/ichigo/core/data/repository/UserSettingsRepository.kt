package com.nei.ichigo.core.data.repository

import com.nei.ichigo.core.model.DarkThemeConfig
import com.nei.ichigo.core.model.UserSettings
import kotlinx.coroutines.flow.Flow

interface UserSettingsRepository {

    val userSettings: Flow<UserSettings>

    suspend fun saveDarkThemeConfig(darkThemeConfig: DarkThemeConfig)

    suspend fun saveUseDynamicColor(useDynamicColor: Boolean)

    suspend fun saveVersionSelected(version: String?)

    suspend fun saveLanguageSelected(language: String?)

    suspend fun updateLastNavigationRoute(route: String)

}