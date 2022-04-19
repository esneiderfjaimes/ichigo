package com.red.code015.preload.assets

import com.google.gson.annotations.SerializedName
import com.red.code015.domain.*

data class BaseSchema<T>(
    @SerializedName("version") val version: String,
    @SerializedName("data") val data: Map<String, T>,
)

data class ChampJson(
    @SerializedName("version") val version: String,
    @SerializedName("id") val id: String,
    @SerializedName("key") val key: String,
    @SerializedName("name") val name: String,
    @SerializedName("title") val title: String,
    @SerializedName("image") val image: Image,
    @SerializedName("blurb") val blurb: String,
    @SerializedName("info") val info: Info,
    @SerializedName("tags") val tags: List<String>,
    @SerializedName("partype") val parType: String,
    @SerializedName("stats") val stats: Map<String, Double>,
    // Extra values
    @SerializedName("skins") val skins: List<Skin>?,
    @SerializedName("lore") val lore: String?,
    @SerializedName("allytips") val allyTips: List<String>?,
    @SerializedName("enemytips") val enemyTips: List<String>?,
    @SerializedName("spells") val spells: List<Spell>?,
    @SerializedName("passive") val passive: Passive?,
    @SerializedName("recommended") val recommended: List<Recommended>?,
)

data class ChampSummaryJson(
    @SerializedName("id") val id: String,
    @SerializedName("key") val key: String,
    @SerializedName("name") val name: String,
    @SerializedName("image") val image: Image,
    @SerializedName("tags") val tags: List<String>,
)