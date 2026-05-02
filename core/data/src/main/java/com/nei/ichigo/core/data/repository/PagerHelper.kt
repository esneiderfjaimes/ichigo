package com.nei.ichigo.core.data.repository

import android.util.Log
import com.nei.ichigo.core.data.model.ListPage
import com.nei.ichigo.core.data.model.Page
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

typealias FetchPage<T> = suspend (version: String, lang: String) -> T
typealias FetchListPage<T> = suspend (version: String, lang: String) -> List<T>

@Singleton
class PagerHelper @Inject constructor(
    private val appConfigRepository: AppConfigRepository
) {
    fun <T> createFlow(fetchPage: FetchPage<T>) = appConfigRepository.config
        .map { result ->
            val (version, lang) = result.getOrThrow()
            val data = fetchPage(version.value, lang.value)
            Log.d("PagerHelper", "data: $data")
            Result.success(data)
        }
        .catch {
            Log.e("PagerHelper", "Error creating flow", it)
            emit(Result.failure(it))
        }
        .flowOn(Dispatchers.IO)

    fun <T> createPage(fetchPage: FetchPage<T>) = createFlow { version, lang ->
        Page(version, lang, fetchPage(version, lang))
    }

    fun <T> createListPage(fetchPage: FetchListPage<T>) = createFlow { version, lang ->
        ListPage(version, lang, fetchPage(version, lang))
    }
}