package com.red.code015.api

import com.google.gson.annotations.SerializedName

data class SummonerResponseServer(
    @SerializedName("id") val id: String,
    @SerializedName("accountId") val accountId: String,
    @SerializedName("puuid") val puuId: String,
    @SerializedName("name") val name: String,
    @SerializedName("profileIconId") val profileIconId: Int,
    @SerializedName("revisionDate") val revisionDate: Long,
    @SerializedName("summonerLevel") val level: Int,
)