package com.nei.ichigo.core.model

data class ChampionDetail(
    val id: String,
    val name: String,
    val image: String,
    val tags: List<String>,
    val title: String,
    val parType: String,
    val skins: List<Skin>,
    val lore: String,
    val allyTips: List<String>?,
    val enemyTips: List<String>?,
)

data class Skin(
    val id: String,
    val num: Int,
    val name: String,
    val chromas: Boolean,
)