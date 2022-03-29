package com.red.code015.data.api

import com.google.gson.annotations.SerializedName

data class SummonerResponseServer(
    @SerializedName("name") val name: String,
    @SerializedName("profileIconId") val profileIconId: Int,
    @SerializedName("summonerLevel") val level: Int,
)