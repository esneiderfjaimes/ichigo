package com.nei.ichigo.core.network.model

import com.google.gson.annotations.SerializedName
import com.nei.ichigo.core.model.Champion

data class ChampionResponseServer(
    @SerializedName("id") val id: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("image") val image: ImageResponseServer?,
    @SerializedName("tags") val tags: List<String>?,
    @SerializedName("version") val version: String?,
    /*
    @SerializedName("key") val key: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("blurb") val blurb: String?,
    @SerializedName("info") val info: InfoRS?,
    @SerializedName("partype") val parType: String?,
    @SerializedName("stats") val stats: Map<String, Double>?,
     */
    // Extra values
    /*
    @SerializedName("skins") val skins: List<SkinRS>?,
    @SerializedName("lore") val lore: String?,
    @SerializedName("allytips") val allyTips: List<String>?,
    @SerializedName("enemytips") val enemyTips: List<String>?,
    @SerializedName("spells") val spells: List<SpellRS>?,
    @SerializedName("passive") val passive: PassiveRS?,
    @SerializedName("recommended") val recommended: List<RecommendedRS>?,
     */
)

fun ChampionResponseServer.asExternalModel() = Champion(
    id = id!!,
    name = name!!,
    image = image!!.full!!,
    tags = tags!!,
)
