/*
 * Copyright 2025 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nei.ichigo.core.network.retrofit

import com.google.gson.annotations.SerializedName
import androidx.tracing.trace
import com.google.samples.apps.nowinandroid.core.network.BuildConfig
import com.google.samples.apps.nowinandroid.core.network.NiaNetworkDataSource
import com.google.samples.apps.nowinandroid.core.network.model.NetworkChangeList
import com.google.samples.apps.nowinandroid.core.network.model.NetworkNewsResource
import com.google.samples.apps.nowinandroid.core.network.model.NetworkTopic
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Inject
import javax.inject.Singleton

// region Data Dragon

data class ChampionsResponseServer(
    @SerializedName("type") val type: String?,
    @SerializedName("format") val format: String?,
    @SerializedName("version") val version: String?,
    @SerializedName("data") val data: Map<String, ChampionResponseServer>?,
)

data class ChampionResponseServer(
    @SerializedName("version") val version: String?,
    @SerializedName("id") val id: String?,
    @SerializedName("key") val key: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("image") val image: ImageRS?,
    @SerializedName("blurb") val blurb: String?,
 //   @SerializedName("info") val info: InfoRS?,
    @SerializedName("tags") val tags: List<String>?,
    @SerializedName("partype") val parType: String?,
    @SerializedName("stats") val stats: Map<String, Double>?,
    // Extra values
  //  @SerializedName("skins") val skins: List<SkinRS>?,
    @SerializedName("lore") val lore: String?,
    @SerializedName("allytips") val allyTips: List<String>?,
    @SerializedName("enemytips") val enemyTips: List<String>?,
//    @SerializedName("spells") val spells: List<SpellRS>?,
  //  @SerializedName("passive") val passive: PassiveRS?,
    //@SerializedName("recommended") val recommended: List<RecommendedRS>?,
)

data class ImageRS(
    val full: String?,
    val sprite: String?,
    val group: String?,
    val x: Long?,
    val y: Long?,
    val w: Long?,
    val h: Long?,
)

// endregion

/**
 * Retrofit API declaration for NIA Network API
 */
private interface RetrofitNiaNetworkApi {
    @GET(value = "topics")
    suspend fun getTopics(
        @Query("id") ids: List<String>?,
    ): NetworkResponse<List<NetworkTopic>>

    @GET(value = "newsresources")
    suspend fun getNewsResources(
        @Query("id") ids: List<String>?,
    ): NetworkResponse<List<NetworkNewsResource>>

    @GET(value = "changelists/topics")
    suspend fun getTopicChangeList(
        @Query("after") after: Int?,
    ): List<NetworkChangeList>

    @GET(value = "changelists/newsresources")
    suspend fun getNewsResourcesChangeList(
        @Query("after") after: Int?,
    ): List<NetworkChangeList>


    @GET("/api/versions.json")
    suspend fun versions(): List<String>

    @GET("cdn/{version}/data/{lang}/champion.json")
    suspend fun champions(
        @Path("version") version: String,
        @Path("lang") lang: String,
    ): ChampionsResponseServer

    @GET("cdn/{version}/data/{lang}/champion/{champKey}.json")
    suspend fun champion(
        @Path("version") version: String,
        @Path("lang") lang: String,
        @Path("champKey") champKey: String,
    ): ChampionsResponseServer
}

private const val NIA_BASE_URL = BuildConfig.BACKEND_URL

/**
 * Wrapper for data provided from the [NIA_BASE_URL]
 */
@Serializable
private data class NetworkResponse<T>(
    val data: T,
)

/**
 * [Retrofit] backed [NiaNetworkDataSource]
 */
@Singleton
internal class RetrofitNiaNetwork @Inject constructor(
    networkJson: Json,
    okhttpCallFactory: dagger.Lazy<Call.Factory>,
) : NiaNetworkDataSource {

    private val networkApi = trace("RetrofitNiaNetwork") {
        Retrofit.Builder()
            .baseUrl(NIA_BASE_URL)
            // We use callFactory lambda here with dagger.Lazy<Call.Factory>
            // to prevent initializing OkHttp on the main thread.
            .callFactory { okhttpCallFactory.get().newCall(it) }
            .addConverterFactory(
                networkJson.asConverterFactory("application/json".toMediaType()),
            )
            .build()
            .create(RetrofitNiaNetworkApi::class.java)
    }

    override suspend fun getTopics(ids: List<String>?): List<NetworkTopic> =
        networkApi.getTopics(ids = ids).data

    override suspend fun getNewsResources(ids: List<String>?): List<NetworkNewsResource> =
        networkApi.getNewsResources(ids = ids).data

    override suspend fun getTopicChangeList(after: Int?): List<NetworkChangeList> =
        networkApi.getTopicChangeList(after = after)

    override suspend fun getNewsResourceChangeList(after: Int?): List<NetworkChangeList> =
        networkApi.getNewsResourcesChangeList(after = after)
}
