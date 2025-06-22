package com.nei.ichigo.core.network.retrofit

import android.content.Context
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import java.io.File

class JsonDiskCacheInterceptor(
    private val context: Context
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        // Only cache GET
        if (request.method != "GET") return chain.proceed(request)

        val urlPath = request.url.encodedPath
        val dir = File(context.filesDir, "network-cache")

        if (!dir.exists()) {
            dir.mkdirs()
        }

        val cacheFile = File(dir, "${urlPath.trimStart('/').replace("/", "_")}.json")

        // If the cache file exists, return it
        try {
            if (cacheFile.exists()) {
                val cachedBody = cacheFile.readText()
                return Response.Builder()
                    .code(200)
                    .message("\uD83E\uDDE0 (MEMORY_CACHE)")
                    .request(request)
                    .protocol(Protocol.HTTP_1_1)
                    .body(cachedBody.toResponseBody("application/json".toMediaType()))
                    .build()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // force to remove cache
            cacheFile.delete()
        }

        // If the cache file doesn't exist, proceed
        val response = chain.proceed(request)

        // If the response is successful, write the body to the cache file
        if (response.isSuccessful) {
            val rawBody = response.body?.string() ?: ""
            cacheFile.writeText(rawBody)

            // return the original response
            return response.newBuilder()
                .body(rawBody.toResponseBody("application/json".toMediaType()))
                .build()
        }

        return response
    }
}
