package com.nei.ichigo.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.nei.ichigo.core.model.DarkThemeConfig
import com.nei.ichigo.core.model.UserSettings
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Qualifier

class IchigoPreferencesDataSource @Inject constructor(
    @DataStoreUserSettings
    private val preferences: DataStore<Preferences>,
) {
    val userSettings = preferences.data.map {
        UserSettings(
            darkThemeConfig = it[DARK_THEME_CONFIG_KEY]
                ?.let { ordinal -> DarkThemeConfig.entries.find { it.ordinal == ordinal } }
                ?: DarkThemeConfig.FOLLOW_SYSTEM,
            useDynamicColor = it[USE_DYNAMIC_COLOR_KEY] == true,
            versionSelected = it[USER_VERSION_KEY],
            langSelected = it[USER_LANG_KEY]
        )
    }

    suspend fun saveUserDarkThemeConfig(darkThemeConfig: DarkThemeConfig) {
        preferences.edit { preferences ->
            preferences[DARK_THEME_CONFIG_KEY] = darkThemeConfig.ordinal
        }
    }

    suspend fun saveUserUseDynamicColor(useDynamicColor: Boolean) {
        preferences.edit { preferences ->
            preferences[USE_DYNAMIC_COLOR_KEY] = useDynamicColor
        }
    }

    suspend fun saveUserVersionSelected(version: String?) {
        preferences.edit { preferences ->
            if (version == null) {
                preferences.remove(USER_VERSION_KEY)
            } else {
                preferences[USER_VERSION_KEY] = version
            }
        }
    }

    suspend fun saveUserLangSelected(lang: String?) {
        preferences.edit { preferences ->
            if (lang == null) {
                preferences.remove(USER_LANG_KEY)
            } else {
                preferences[USER_LANG_KEY] = lang
            }
        }
    }

    companion object {
        private val Context.dataStore by preferencesDataStore("user_settings")

        fun dataStoreBy(context: Context) = context.dataStore

        val DARK_THEME_CONFIG_KEY = intPreferencesKey("dark_theme_config")
        val USE_DYNAMIC_COLOR_KEY = booleanPreferencesKey("use_dynamic_color")
        val USER_LANG_KEY = stringPreferencesKey("lang_selected")
        val USER_VERSION_KEY = stringPreferencesKey("version_selected")
    }

    @Retention(AnnotationRetention.BINARY)
    @Qualifier
    annotation class DataStoreUserSettings
}