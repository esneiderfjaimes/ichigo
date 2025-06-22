package com.nei.ichigo.core.network.di

import android.content.Context
import androidx.tracing.trace
import com.nei.ichigo.core.network.BuildConfig
import com.nei.ichigo.core.network.IchigoNetworkDataSource
import com.nei.ichigo.core.network.retrofit.JsonDiskCacheInterceptor
import com.nei.ichigo.core.network.retrofit.RetrofitIchigoNetworkDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {

    @Provides
    @Singleton
    fun providesConverterFactory(): Converter.Factory = trace("GsonConverterFactory") {
        GsonConverterFactory.create()
    }

    @Provides
    @Singleton
    fun okHttpCallFactory(
        @ApplicationContext context: Context,
    ): Call.Factory = trace("OkHttpClient") {
        OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor()
                    .apply {
                        if (BuildConfig.DEBUG) {
                            setLevel(HttpLoggingInterceptor.Level.BODY)
                        }
                    },
            )
            .addInterceptor(
                JsonDiskCacheInterceptor(context)
            )
            .build()
    }

    @Provides
    fun provideDataSource(dataSource: RetrofitIchigoNetworkDataSource): IchigoNetworkDataSource {
        return dataSource
    }
}
