package com.nei.ichigo.core.model

data class Spell(
    val id: String,
    val name: String,
    val description: String,
    val tooltip: String,
    val image: String,
    val summonerLevel: Int,
    val cooldown: Double,
    val modes: List<String>,
)