package com.nei.ichigo.core.data.repository

import android.util.Log
import com.nei.ichigo.core.datastore.OfflineDataDragonPreferencesDataSource
import com.nei.ichigo.core.network.IchigoNetworkDataSource
import jakarta.inject.Inject

internal class OnlineChampionsRepository @Inject constructor(
    private val networkDataSource: IchigoNetworkDataSource,
    private val offlinePreferencesDataSource: OfflineDataDragonPreferencesDataSource,
) : ChampionsRepository {

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