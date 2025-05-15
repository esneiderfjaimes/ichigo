package com.nei.ichigo.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.time.Duration
import java.time.Instant
import javax.inject.Inject
import javax.inject.Qualifier

class OfflineDataDragonPreferencesDataSource @Inject constructor(
    @DataStoreDataDragon
    private val preferences: DataStore<Preferences>,
) {
    suspend fun versionsIsExpired() = isExpired(LAST_VERSIONS_UPDATE_KEY)

    suspend fun getVersions() = getValue(VERSIONS_KEY)

    suspend fun saveVersions(versions: List<String>) {
        saveValue(VERSIONS_KEY, LAST_VERSIONS_UPDATE_KEY, versions)
    }

    suspend fun languagesIsExpired() = isExpired(LAST_LANGUAGES_UPDATE_KEY)

    suspend fun getLanguages() = getValue(LANGUAGES_KEY)

    suspend fun saveLanguages(languages: List<String>) {
        saveValue(LANGUAGES_KEY, LAST_LANGUAGES_UPDATE_KEY, languages)
    }

    private suspend fun saveValue(
        valueKey: Preferences.Key<String>,
        lastUpdateKey: Preferences.Key<Long>,
        value: List<String>,
    ) {
        preferences.edit { preferences ->
            preferences[valueKey] = value.joinToString(",")
            preferences[lastUpdateKey] = Instant.now().toEpochMilli()
        }
    }

    private suspend fun getValue(key: Preferences.Key<String>) = kotlin.runCatching {
        preferences.data.map { preferences ->
            preferences[key]?.split(",")
                ?: throw IllegalStateException("No ${key.name} found")
        }.first()
    }

    private suspend fun isExpired(key: Preferences.Key<Long>): Boolean {
        val lastUpdate = preferences.data
            .map { preferences -> preferences[key]?.let { Instant.ofEpochMilli(it) } }.first()
        return lastUpdate == null || Instant.now().isAfter(lastUpdate.plus(EXPIRATION_DURATION))
    }

    companion object {
        private val Context.dataStore by preferencesDataStore("data_dragon")

        fun dataStoreBy(context: Context) = context.dataStore

        val VERSIONS_KEY = stringPreferencesKey("versions")
        val LANGUAGES_KEY = stringPreferencesKey("languages")
        private val LAST_VERSIONS_UPDATE_KEY = longPreferencesKey("last_versions_update")
        private val LAST_LANGUAGES_UPDATE_KEY = longPreferencesKey("last_languages_update")
        private val EXPIRATION_DURATION = Duration.ofDays(1)
    }

    @Retention(AnnotationRetention.BINARY)
    @Qualifier
    annotation class DataStoreDataDragon
}