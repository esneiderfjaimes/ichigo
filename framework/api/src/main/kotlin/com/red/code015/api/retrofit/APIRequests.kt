package com.red.code015.api.retrofit

import android.util.Log
import com.red.code015.api.host.HostInterceptor
import com.red.code015.api.host.Platform
import com.red.code015.api.host.Region
import com.red.code015.api.retrofit.BaseRequest.BaseAPI
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

// TODO Multiple instances
abstract class BaseRequest2<A : BaseAPI>(
    // a single domain
    private var baseUrl: String,
) {

    private val okHttpClient: OkHttpClient = HttpLoggingInterceptor().run {
        level = HttpLoggingInterceptor.Level.BODY
        OkHttpClient.Builder().addInterceptor(this).build()
    }

    inline fun <reified T : A> getService(): T =
        buildRetrofit().run {
            create(T::class.java)
        }

    fun buildRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}


abstract class BaseRequest<H : HostInterceptor.BaseHost, A : BaseAPI>(
    private var interceptor: HostInterceptor<H>,
) {
    interface BaseAPI

    fun updateHost(it: HostInterceptor<H>) {
        interceptor = it
        okHttpClient = HttpLoggingInterceptor().run {
            level = HttpLoggingInterceptor.Level.BODY
            OkHttpClient.Builder().addInterceptor(interceptor).build()
        }
        services.clear()
    }

    private var okHttpClient: OkHttpClient = HttpLoggingInterceptor().run {
        level = HttpLoggingInterceptor.Level.BODY
        OkHttpClient.Builder().addInterceptor(interceptor).build()
    }

    val services: MutableMap<String, A> = hashMapOf()

    inline fun <reified T : A> getService(className: String = "-"): T =
        buildRetrofit(className).run {
            create(T::class.java)
        }

    inline fun <reified T : A> service(): T = (T::class.java).simpleName.let {
        if (services[it] == null) services[it] = getService<T>(it)
        services[it] as T
    }

    fun buildRetrofit(className: String): Retrofit = Retrofit.Builder()
        .baseUrl("https://${interceptor.baseUrl}")
        .client(okHttpClient)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build().apply {
            Log.d("okhttp.OkHttpClient", "buildRetrofit: $className, int ${interceptor.baseUrl}")
        }
}

interface DataDragonAPI : BaseAPI
class DataDragonRequest : BaseRequest2<DataDragonAPI>("https://ddragon.leagueoflegends.com/")

interface RiotAPI : BaseAPI
class RiotRequest(host: HostInterceptor<Region>) : BaseRequest<Region, RiotAPI>(host)

interface LoLAPIs : BaseAPI
class LoLRequest(host: HostInterceptor<Platform>) : BaseRequest<Platform, LoLAPIs>(host)