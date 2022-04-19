package com.red.code015.api

import android.util.Log
import com.red.code015.data.APIException
import com.red.code015.data.ForbiddenException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import retrofit2.HttpException
import kotlin.system.measureTimeMillis

suspend inline fun <R> tryCoroutineScope(
    name: String = "fun",
    noinline block: suspend CoroutineScope.() -> R,
): R {
    try {
        val value: R
        val time = measureTimeMillis {
            value = coroutineScope(block)
        }
        Log.d(TAG, "$name: time:$time value:$value")
        return value
    } catch (e: Exception) {
        when (e) {
            is HttpException -> {
                if (e.code() == 403) {
                    throw ForbiddenException(e.message())
                } else throw APIException(e.message())
            }
        }
        throw e
    }
}
