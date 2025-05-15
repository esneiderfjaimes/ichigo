package com.nei.ichigo.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
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
            versionSelected = it[USER_VERSION_KEY],
            langSelected = it[USER_LANG_KEY]
        )
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

        val USER_LANG_KEY = stringPreferencesKey("lang_selected")
        val USER_VERSION_KEY = stringPreferencesKey("version_selected")
    }

    @Retention(AnnotationRetention.BINARY)
    @Qualifier
    annotation class DataStoreUserSettings
}