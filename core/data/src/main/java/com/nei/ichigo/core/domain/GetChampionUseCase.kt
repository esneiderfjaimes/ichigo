package com.nei.ichigo.core.domain

import com.nei.ichigo.core.data.repository.PagerHelper
import com.nei.ichigo.core.network.IchigoNetworkDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class GetChampionUseCase @Inject constructor(
    private val networkDataSource: IchigoNetworkDataSource,
    private val pagerHelper: PagerHelper
) {
    operator fun invoke(championKey: String) = pagerHelper.createPage { version, lang ->
        networkDataSource.getChampion(version, lang, championKey)
    }
}

class RefreshableFlow<T>(
    private val upstream: () -> Flow<T>
) {
    private val refreshTrigger = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    val flow: Flow<T> = refreshTrigger
        .onStart { emit(Unit) }
        .flatMapLatest {
            upstream()
                .onStart { _isRefreshing.value = true }
                .onCompletion { _isRefreshing.value = false }
        }

    fun refresh() {
        refreshTrigger.tryEmit(Unit)
    }
}