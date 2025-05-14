package com.nei.ichigo.core.network.model

import com.google.gson.annotations.SerializedName
import com.nei.ichigo.core.model.Champion

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
    @SerializedName("image") val image: ImageResponseServer?,
    @SerializedName("blurb") val blurb: String?,
    //@SerializedName("info") val info: InfoRS?,
    @SerializedName("tags") val tags: List<String>?,
    @SerializedName("partype") val parType: String?,
    @SerializedName("stats") val stats: Map<String, Double>?,
    //Extra values
    //@SerializedName("skins") val skins: List<SkinRS>?,
    @SerializedName("lore") val lore: String?,
    @SerializedName("allytips") val allyTips: List<String>?,
    @SerializedName("enemytips") val enemyTips: List<String>?,
    //@SerializedName("spells") val spells: List<SpellRS>?,
    //@SerializedName("passive") val passive: PassiveRS?,
    //@SerializedName("recommended") val recommended: List<RecommendedRS>?,
)

data class ImageResponseServer(
    val full: String?,
    val sprite: String?,
    val group: String?,
    val x: Long?,
    val y: Long?,
    val w: Long?,
    val h: Long?,
)

fun ChampionResponseServer.asExternalModel() = Champion(
    id = id!!,
    name = name!!,
    // image = "https://ddragon.leagueoflegends.com/cdn/$version/img/champion/${champion.image.full}",
    image = "https://opgg-static.akamaized.net/meta/images/lol/$version/champion/${image!!.full}?image=c_crop,h_103,w_103,x_9,y_9/q_auto:good,f_webp,w_160,h_160&v=1510",
    tags = tags!!,
)
