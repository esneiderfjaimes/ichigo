package com.nei.ichigo.core.model

data class RuneBranch(
    val id: String,
    val key: String,
    val icon: String,
    val name: String,
    val slots: List<RuneSlot>
)

data class RuneSlot(
    val runes: List<Rune>
)

data class Rune(
    val id: String,
    val key: String,
    val icon: String,
    val name: String,
    val shortDesc: String,
    val longDesc: String
)
