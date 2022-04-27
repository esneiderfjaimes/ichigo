package com.red.code015.api

import android.util.Log
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.red.code015.data.APIException
import com.red.code015.data.ForbiddenException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.util.concurrent.Semaphore
import kotlin.system.measureTimeMillis

suspend inline fun <R> tryCoroutineScope(
    name: String = "fun",
    noinline block: suspend CoroutineScope.() -> R,
): R {
    try {
        Log.d(TAG, ">> $name")
        val value: R
        val time = measureTimeMillis {
            value = coroutineScope(block)
        }
        Log.d(TAG, "<< $name: time:$time value:$value")
        return value
    } catch (e: Exception) {
        Log.e(TAG, "<< $name")
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

suspend fun <R> RiotGamesRetrofitDataSource.tryCoroutineScope2(
    name: String = "fun",
    block: suspend CoroutineScope.() -> R,
): R {
    val result: R
    val allTime = measureTimeMillis {
        result = withContext(Dispatchers.Main) {
            tryCoroutineScopeBlock(name, block)
        }
    }
    Log.d(TAG, "<<< $name: all time:$allTime")
    return result
}

private suspend fun <R> RiotGamesRetrofitDataSource.tryCoroutineScopeBlock(
    name: String = "fun",
    block: suspend CoroutineScope.() -> R,
    retry: Boolean = true,
): R {
    try {
        Log.d(TAG, ">> $name")
        val value: R
        val time = measureTimeMillis {
            value = coroutineScope(block)
        }
        Log.d(TAG, "<< $name: time:$time value:$value")
        return value
    } catch (e: Exception) {
        Log.e(TAG, "<< $name")
        when (e) {
            is HttpException -> {
                if (e.code() == 403) {
                    if (retry) {
                        return fetchApiKey {
                            return@fetchApiKey tryCoroutineScopeBlock(name = "$name retry",
                                block = block,
                                retry = false)
                        }
                    } else throw ForbiddenException(e.message())
                } else throw APIException(e.message())
            }
        }
        throw e
    }
}

suspend fun <R> RiotGamesRetrofitDataSource.fetchApiKey(
    block: suspend () -> R,
): R {
    val semaphore = Semaphore(0)
    firebaseRemoteConfig.setConfigSettingsAsync(
        FirebaseRemoteConfigSettings.Builder().setMinimumFetchIntervalInSeconds(0).build()
    )
    firebaseRemoteConfig.fetchAndActivate().addOnCompleteListener { task ->
        if (task.isSuccessful)
            remoteConfig.keyApi = FirebaseRemoteConfig.getInstance().getString("RIOT_API_KEY") + "1"
        Log.w(TAG, "fetch result:${task.result} keyApi:${remoteConfig.keyApi}")
        semaphore.release()
    }.addOnCanceledListener {
        semaphore.release()
    }.addOnFailureListener {
        semaphore.release()
    }

    Log.w(TAG, "waiting for response from remote configuration")
    semaphore.tryAcquire()
    Log.w(TAG, "continue...")

    return block.invoke()
}