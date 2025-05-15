package com.nei.ichigo.core.data.repository

import android.content.Context
import android.util.Log
import com.nei.ichigo.core.data.R
import com.nei.ichigo.core.data.model.ChampionsPage
import com.nei.ichigo.core.datastore.IchigoPreferencesDataSource
import com.nei.ichigo.core.datastore.OfflineDataDragonPreferencesDataSource
import com.nei.ichigo.core.network.IchigoNetworkDataSource
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

internal class OnlineChampionsRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val networkDataSource: IchigoNetworkDataSource,
    private val offlinePreferencesDataSource: OfflineDataDragonPreferencesDataSource,
    private val ichigoPreferencesDataSource: IchigoPreferencesDataSource,
) : ChampionsRepository {
    override fun getChampionsPage() =
        ichigoPreferencesDataSource.userSettings
            .map {
                val lang = getCurrentLang(it.langSelected)
                val version = getCurrentVersion(it.versionSelected)
                version to lang
            }.distinctUntilChanged().map { (version, lang) ->
                kotlin.runCatching {
                    ChampionsPage(
                        version = version,
                        lang = lang,
                        champions = networkDataSource.getChampions(version, lang)
                    )
                }
            }.catch {
                emit(
                    Result.failure(it)
                )
            }.flowOn(Dispatchers.IO)

    private suspend fun getCurrentVersion(userVersionSelected: String?): String {
        val versions = getVersions()
        return if (userVersionSelected != null && versions.contains(userVersionSelected)) {
            userVersionSelected
        } else {
            versions.first()
        }
    }

    private suspend fun getCurrentLang(userLangSelected: String?): String {
        val languages = getLanguages()

        fun automaticSelection(): String {
            val langFromRes = context.getString(R.string.core_data_lang_code)
            return if (languages.contains(langFromRes)) {
                langFromRes
            } else {
                languages.first()
            }
        }

        return if (userLangSelected != null && languages.contains(userLangSelected)) {
            userLangSelected
        } else {
            automaticSelection()
        }
    }

    override suspend fun getLanguages() = getValue(
        offlinePreferencesDataSource::languagesIsExpired,
        offlinePreferencesDataSource::getLanguages,
        networkDataSource::getLanguages,
        offlinePreferencesDataSource::saveLanguages
    )

    override suspend fun getVersions() = getValue(
        offlinePreferencesDataSource::versionsIsExpired,
        offlinePreferencesDataSource::getVersions,
        networkDataSource::getVersions,
        offlinePreferencesDataSource::saveVersions
    )

    private suspend fun <T> getValue(
        isExpired: suspend () -> Boolean,
        offlineDataSource: suspend () -> Result<T>,
        networkDataSource: suspend () -> Result<T>,
        offlineSaver: suspend (T) -> Unit
    ): T {
        suspend fun networkOrOffline() = networkDataSource().fold(
            onSuccess = {
                offlineSaver(it)
                it
            },
            onFailure = {
                offlineDataSource().getOrThrow()
            }
        )

        return if (isExpired()) {
            networkOrOffline()
        } else {
            offlineDataSource().fold(
                onSuccess = { it },
                onFailure = {
                    Log.e(TAG, "Failed to get offline value", it)
                    networkOrOffline()
                }
            )
        }
    }

    companion object {
        private const val TAG = "OnlineChampionsRepo"
    }
}