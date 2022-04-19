package com.red.code015.domain

data class Champion(
    val version: String,
    val id: String,
    val key: String,
    val name: String,
    val title: String,
    val image: Image,
    val blurb: String,
    val info: Info,
    val tags: List<String>,
    val partype: String,
    val stats: Map<String, Double>,
    // Extra values
    val skins: List<Skin>,
    val lore: String?,
    val allytips: List<String>,
    val enemytips: List<String>,
    val spells: List<Spell>,
    val passive: Passive?,
    val recommended: List<Recommended>,
)

data class Image(
    val full: String,
    val sprite: String,
    val group: String,
    val x: Long,
    val y: Long,
    val w: Long,
    val h: Long,
)

data class Info(
    val attack: Long,
    val defense: Long,
    val magic: Long,
    val difficulty: Long,
)

data class Passive(
    val name: String,
    val description: String,
    val image: Image,
)

data class Recommended(
    val champion: String,
    val title: String,
    val map: String,
    val mode: String,
    val type: String,
    val customTag: String,
    val sortrank: Long? = null,
    val extensionPage: Boolean,
    val useObviousCheckmark: Boolean? = null,
    val customPanel: Any? = null,
    val blocks: List<Block>,
)

data class Block(
    val type: String,
    val recMath: Boolean,
    val recSteps: Boolean,
    val minSummonerLevel: Long,
    val maxSummonerLevel: Long,
    val showIfSummonerSpell: IfSummonerSpell,
    val hideIfSummonerSpell: IfSummonerSpell,
    val appendAfterSection: String? = null,
    val visibleWithAllOf: List<String>? = null,
    val hiddenWithAnyOf: List<String>? = null,
    val items: List<Item>,
)

enum class IfSummonerSpell {
    Empty,
    SummonerSmite
}

data class Item(
    val id: String,
    val count: Long,
    val hideCount: Boolean,
)

data class Skin(
    val id: String,
    val num: Long,
    val name: String,
    val chromas: Boolean,
)

data class Spell(
    val id: String,
    val name: String,
    val description: String,
    val tooltip: String,
    val leveltip: LevelTip,
    val maxrank: Long,
    val cooldown: List<Long>,
    val cooldownBurn: String,
    val cost: List<Long>,
    val costBurn: String,
    val datavalues: DataValues,
    val effect: List<List<Long>?>,
    val effectBurn: List<String?>,
    val vars: List<Any?>,
    val costType: String,
    val maxammo: String,
    val range: List<Long>,
    val rangeBurn: String,
    val image: Image,
    val resource: String,
)

class DataValues : Any()

data class LevelTip(
    val label: List<String>,
    val effect: List<String>,
)
