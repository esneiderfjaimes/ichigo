package com.nei.ichigo.core.model

data class ChampionDetail(
    val id: String,
    val name: String,
    val image: String,
    val tags: List<String>,
    val title: String,
    val blurb: String,
    val parType: String,
    val stats: Map<String, Double>,
    val skins: List<Skin>,
    val lore: String,
)

data class Skin(
    val id: String,
    val num: Int,
    val name: String,
    val chromas: Boolean,
)