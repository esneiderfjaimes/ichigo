@file:OptIn(ExperimentalTypeInference::class)

package com.red.code015.data.util

import com.red.code015.data.ForbiddenException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlin.experimental.ExperimentalTypeInference


 fun <T> tryFlow(
    @BuilderInference
    block: suspend FlowCollector<T>.() -> Unit,
): Flow<T> = flow {
    try {
        block(this)
    } catch (e: Exception) {
        throw e
    }
}.flowOn(Dispatchers.IO)
