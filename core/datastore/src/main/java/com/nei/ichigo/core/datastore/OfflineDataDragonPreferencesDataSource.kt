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

    suspend fun getVersions() = getStrings(VERSIONS_KEY, LAST_VERSIONS_UPDATE_KEY)

    suspend fun saveVersions(versions: List<String>) {
        saveStrings(VERSIONS_KEY, LAST_VERSIONS_UPDATE_KEY, versions)
    }

    suspend fun getLanguages() = getStrings(LANGUAGES_KEY, LAST_LANGUAGES_UPDATE_KEY)

    suspend fun saveLanguages(languages: List<String>) {
        saveStrings(LANGUAGES_KEY, LAST_LANGUAGES_UPDATE_KEY, languages)
    }

    private suspend fun saveStrings(
        valueKey: Preferences.Key<String>,
        lastUpdateKey: Preferences.Key<Long>,
        value: List<String>,
    ) {
        preferences.edit { preferences ->
            preferences[valueKey] = value.joinToString(",")
            preferences[lastUpdateKey] = Instant.now().toEpochMilli()
        }
    }

    private suspend fun getStrings(
        key: Preferences.Key<String>,
        lastUpdateKey: Preferences.Key<Long>
    ): List<String> {
        val isExpired = isExpired(lastUpdateKey)
        if (isExpired) throw Exception("${key.name} is expired")
        return preferences.data.map { preferences ->
            preferences[key]?.split(",")
                ?.ifEmpty { throw IllegalStateException("No ${key.name} found") }
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

        private val VERSIONS_KEY = stringPreferencesKey("versions")
        private val LANGUAGES_KEY = stringPreferencesKey("languages")
        private val LAST_VERSIONS_UPDATE_KEY = longPreferencesKey("last_versions_update")
        private val LAST_LANGUAGES_UPDATE_KEY = longPreferencesKey("last_languages_update")
        private val EXPIRATION_DURATION = Duration.ofDays(1)
    }

    @Retention(AnnotationRetention.BINARY)
    @Qualifier
    annotation class DataStoreDataDragon
}