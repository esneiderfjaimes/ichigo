package com.nei.ichigo.core.network.model

import com.google.gson.annotations.SerializedName

data class PageResponseServer<T>(
    /*
    @SerializedName("type") val type: String?,
    @SerializedName("format") val format: String?,
    @SerializedName("version") val version: String?,
    */
    @SerializedName("data") val data: Map<String, T>?,
)