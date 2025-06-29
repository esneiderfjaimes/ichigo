package com.nei.ichigo.core.model

data class Item(
    val id: String,
    val name: String,
    val plaintext: String,
    val image: String,
    val description: String,
    val from: List<String>,
    val into: List<String>,
    val maps: Set<String>,
    val gold: Gold,
)

data class Gold(
    val base: Int,
    val purchasable: Boolean,
    val total: Int,
    val sell: Int
)