package com.nei.ichigo.core.network.model

import com.nei.ichigo.core.model.Spell

data class SummonerResponseServer(
    val id: String?,
    val name: String?,
    val description: String?,
    val tooltip: String?,
    val cooldown: List<Double>?,
    val cooldownBurn: String?,
    val effect: List<List<Float>?>?,
    val effectBurn: List<String?>?,
    val key: String?,
    val summonerLevel: Int?,
    val modes: List<String>?,
    val costType: String?,
    val maxammo: String?,
    val rangeBurn: String?,
    val image: ImageResponseServer?,
    val resource: String?
)

fun SummonerResponseServer.asExternalModel() = Spell(
    id = id!!,
    name = name!!,
    description = description!!,
    tooltip = tooltip!!,
    image = image!!.full!!,
    summonerLevel = summonerLevel!!,
    cooldown = cooldownBurn!!.toDouble(),
    modes = modes ?: emptyList()
)