@file:OptIn(ExperimentalTypeInference::class)

package com.red.code015.data.util

import android.util.Log
import com.red.code015.data.repositories.DataDragonRepository.Companion.TAG
import com.red.code015.domain.DataSources
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlin.experimental.ExperimentalTypeInference
import kotlin.system.measureTimeMillis

fun <T> tryFlow(
    name: String = "fun",
    @BuilderInference
    block: suspend FlowCollector<T>.() -> Unit,
): Flow<T> = flow {
    try {
        Log.d(TAG, ">> $name")
        val time = measureTimeMillis {
            block(this)
        }
        Log.d(TAG, "<< $name: time:$time")
    } catch (e: Exception) {
        e.printStackTrace()
        Log.e(TAG, "<< $name")
        throw e
    }
}.flowOn(Dispatchers.IO)

fun getDataSource(
    vPreload: String?,
    vRemote: String?,
    dataSourcesError: DataSourcesErrors,
): DataSources? {

    if (vPreload == null && vRemote == null) return null // NO versions

    if (vPreload == null) { // Just API version
        return when {
            dataSourcesError.local -> DataSources.DATABASE
            dataSourcesError.api -> DataSources.API
            else -> null
        }
    }

    if (vRemote == null) { // Just PRELOAD version
        return when {
            dataSourcesError.preload -> DataSources.PRELOAD
            dataSourcesError.local -> DataSources.DATABASE
            else -> null
        }
    }

    return if (vPreload == vRemote) {
        when {
            dataSourcesError.preload -> DataSources.PRELOAD
            dataSourcesError.local -> DataSources.DATABASE
            dataSourcesError.api -> DataSources.API
            else -> null
        }
    } else {
        when {
            dataSourcesError.local -> DataSources.DATABASE
            dataSourcesError.api -> DataSources.API
            dataSourcesError.preload -> DataSources.PRELOAD // outdated
            else -> null
        }
    }

}

data class DataSourcesErrors(
    val preload: Boolean = true,
    val local: Boolean = true,
    val api: Boolean = true,
)

data class Versions(val preload: String?, val api: String?)