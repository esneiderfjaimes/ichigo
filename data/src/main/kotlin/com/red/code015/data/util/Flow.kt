@file:OptIn(ExperimentalTypeInference::class)

package com.red.code015.data.util

import android.util.Log
import com.red.code015.data.repositories.API
import com.red.code015.data.repositories.Block
import com.red.code015.data.repositories.DataDragonRepository
import com.red.code015.domain.DataSources
import kotlinx.coroutines.flow.FlowCollector
import kotlin.experimental.ExperimentalTypeInference

suspend fun <T> API.loopDataSources(
    name: String,
    withDataSource: DataSources? = null,
    @BuilderInference blockApi: Block<T>? = null,
    @BuilderInference blockDatabase: Block<T>? = null,
    @BuilderInference blockPreload: Block<T>? = null,
    flowCollector: FlowCollector<T>,
) {
    Log.i(TAG, "> loop($name) ds:$withDataSource")
    var dataSources = withDataSource
    var errors = DataSourcesErrorsRiot()
    loop@ while (dataSources != null) {
        Log.d(TAG, "> flow($name) ds:$dataSources, $errors")
        try {
            when (dataSources) {
                DataSources.DATABASE -> {
                    blockDatabase?.invoke(flowCollector) ?: break@loop
                }
                DataSources.API -> {
                    blockApi?.let {
                        tryRemote(flowCollector) { it.invoke(flowCollector) }
                    } ?: break@loop
                }
                DataSources.PRELOAD -> {
                    blockPreload?.invoke(flowCollector) ?: break@loop
                }
                else -> break@loop
            }
            break@loop
        } catch (e: Exception) {
            errors = errors.let {
                when (dataSources) {
                    DataSources.API -> it.copy(api = false)
                    DataSources.DATABASE -> it.copy(local = false)
                    else -> it
                }
            }
            dataSources = getDataSource(errors)
        }
    }
    Log.i(TAG, "< loop($name) ds:$dataSources $errors")
}

suspend fun <T : Any> API.loopDataSources(
    name: String,
    withDataSource: DataSources? = null,
    blockApi: suspend () -> T,
    blockDatabase: suspend () -> T,
    blockPreload: suspend () -> T,
    onDataSources: (DataSourcesErrors) -> DataSources?,
): T {
    Log.i(TAG, "> loop($name) ds:$withDataSource")

    var dataSources = withDataSource
    var errors = DataSourcesErrors()

    loop@ while (dataSources != null) {
        Log.i(DataDragonRepository.TAG,
            "encyclopediaChampion: DataSource:$dataSources errors:$errors")
        try {
            return when (dataSources) {
                DataSources.PRELOAD -> blockPreload()
                DataSources.DATABASE -> blockDatabase()
                DataSources.API -> tryRemote { return@tryRemote blockApi() }
            }
        } catch (e: Exception) {
            errors = errors.let {
                when (dataSources) {
                    DataSources.API -> it.copy(api = false)
                    DataSources.DATABASE -> it.copy(local = false)
                    DataSources.PRELOAD -> it.copy(preload = false)
                    else -> it
                }
            }
            dataSources = onDataSources(errors)
        }
    }
    throw Throwable("No data")
}