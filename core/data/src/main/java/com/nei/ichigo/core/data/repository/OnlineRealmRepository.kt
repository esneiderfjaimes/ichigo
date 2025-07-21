@file:OptIn(ExperimentalCoroutinesApi::class)

package com.nei.ichigo.core.data.repository

import android.util.Log
import com.nei.ichigo.core.data.repository.DDragonRepository.MetaData
import com.nei.ichigo.core.datastore.OfflineDataDragonPreferencesDataSource
import com.nei.ichigo.core.network.IchigoNetworkDataSource
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart

fun <T> safeFlow(block: suspend () -> T): Flow<Result<T>> = flow {
    emit(runCatching { block() })
}

internal class OnlineRealmRepository @Inject constructor(
    private val network: IchigoNetworkDataSource,
    private val offline: OfflineDataDragonPreferencesDataSource,
) : DDragonRepository {
    private val forceFetchTrigger = MutableSharedFlow<Unit>(replay = 1)

    override val metaData = forceFetchTrigger
        .onStart { emit(Unit) }
        .flatMapLatest {
            safeFlow { MetaData(getVersions(), getLanguages()) }
        }
        .distinctUntilChanged()

    override suspend fun forceUpdate() {
        forceFetchTrigger.emit(Unit)
    }

    private suspend fun getLanguages() = getValue(
        offline::getLanguages,
        network::getLanguages,
        offline::saveLanguages
    )

    private suspend fun getVersions() = getValue(
        offline::getVersions,
        network::getVersions,
        offline::saveVersions
    )

    private suspend fun <T> getValue(
        offlineProvider: suspend () -> T,
        networkProvider: suspend () -> T,
        offlineSaver: suspend (T) -> Unit
    ): T {
        val offline = runCatching { offlineProvider() }

        val offlineThrowable = offline.fold(
            onSuccess = { return it },
            onFailure = { it }
        )
        Log.e(TAG, "Failed to get offline value", offlineThrowable)

        runCatching { networkProvider() }.fold(
            onSuccess = {
                offlineSaver(it)
                return it
            },
            onFailure = { throw offlineThrowable }
        )
    }

    companion object {
        private const val TAG = "OnlineChampionsRepo"
    }
}