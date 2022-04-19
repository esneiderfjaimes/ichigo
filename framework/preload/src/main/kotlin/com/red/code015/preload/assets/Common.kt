package com.red.code015.preload.assets

import android.content.Context
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.google.gson.reflect.TypeToken
import com.red.code015.preload.DataDragonAssetsDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import java.io.IOException
import java.io.InputStream
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
        Log.d(TAG, "$name: time:$time")
        return value
    } catch (e: Exception) {
        throw e
    }
}


/**
 * Use with classes that require T
 */
inline fun <reified T> DataDragonAssetsDataSource.assetsToType(filePath: String): T {
    val json = assetsToJson(filePath)
    return gson.fromJson(json, object : TypeToken<T>() {}.type)
}

inline fun <reified T> DataDragonAssetsDataSource.assetsTo(filePath: String): T {
    val json = assetsToJson(filePath)
    return gson.fromJson(json, T::class.java)
}

fun DataDragonAssetsDataSource.assetsToJson(filePath: String): String {
    val json = application.assets.open(filePath).bufferedReader().use {
        it.readText()
    }
    return json
}

fun Context.assetsToBitmap(filePath: String): Bitmap? {
    val assetManager: AssetManager = assets
    val input: InputStream
    var bitmap: Bitmap? = null
    try {
        input = assetManager.open(filePath.lowercase())
        bitmap = BitmapFactory.decodeStream(input)
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return bitmap
}
