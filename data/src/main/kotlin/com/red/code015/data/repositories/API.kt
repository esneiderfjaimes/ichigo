@file:OptIn(ExperimentalTypeInference::class)

package com.red.code015.data.repositories

import com.red.code015.data.APIDataSource
import com.red.code015.data.ForbiddenException
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import kotlin.experimental.ExperimentalTypeInference

typealias Block<T> = suspend FlowCollector<T>.() -> Unit

open class API @Inject constructor(
    private val remote: APIDataSource,
) {
    suspend fun <T> tryRemote(
        flowCollector: FlowCollector<T>,
        @BuilderInference block: Block<T>,
    ) {
        try {
            block.invoke(flowCollector)
        } catch (e: Exception) {
            if (e is ForbiddenException) {
                remote.fetchApiKey(flowCollector, block)
            } else throw e
        }
    }

    fun <T> flowTryRemote(@BuilderInference block: Block<T>) = flow {
        tryRemote(this) { block.invoke(this) }
    }

    suspend fun <T : Any> tryRemote(
        block: suspend () -> T,
    ): T = try {
        block.invoke()
    } catch (e: Exception) {
        if (e is ForbiddenException) {
            remote.fetchApiKey(block)
        } else throw e
    }
}