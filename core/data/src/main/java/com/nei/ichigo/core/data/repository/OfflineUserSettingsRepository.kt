package com.nei.ichigo.core.data.repository

import android.util.Log
import com.nei.ichigo.core.datastore.IchigoPreferencesDataSource
import com.nei.ichigo.core.model.UserSettings
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

internal class OfflineUserSettingsRepository @Inject constructor(
    private val ichigoPreferencesDataSource: IchigoPreferencesDataSource,
) : UserSettingsRepository {

    override val userSettings: Flow<UserSettings>
        get() = ichigoPreferencesDataSource.userSettings

    override suspend fun saveVersionSelected(version: String?) {
        Log.d(TAG, "saveVersionSelected: $version")
        ichigoPreferencesDataSource.saveUserVersionSelected(version)
    }

    override suspend fun saveLanguageSelected(language: String?) {
        Log.d(TAG, "saveLanguageSelected: $language")
        ichigoPreferencesDataSource.saveUserLangSelected(language)
    }

    companion object {
        private const val TAG = "OfflineUserSettingsRepo"
    }
}