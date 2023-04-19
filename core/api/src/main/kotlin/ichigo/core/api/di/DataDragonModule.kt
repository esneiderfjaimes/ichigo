package ichigo.core.api.di

import ichigo.core.api.retrofit.DataDragonService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object DataDragonModule {
    private var provider: DataDragonService? = null

    val service: DataDragonService
        get() = if (provider == null) {
            provider = buildRetrofit()
            provider!!
        } else {
            provider!!
        }


    private fun buildRetrofit(): DataDragonService = Retrofit.Builder()
        .baseUrl("https://ddragon.leagueoflegends.com/")
        .client(HttpLoggingInterceptor().let { interceptor ->
            interceptor.level = HttpLoggingInterceptor.Level.BASIC
            OkHttpClient.Builder().addInterceptor(interceptor).build()
        })
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(DataDragonService::class.java)
}