package com.red.code015.domain

import android.graphics.Bitmap

data class EncyclopediaChampion(
    val version: String,
    val data: List<ChampListItem>,
    val dataSource: DataSource,
)

data class ChampListItem(
    val id: String,
    val key: String,
    val name: String,
    val image: Image,
    val tags: List<String>,
    val rotation: RotationChamp = RotationChamp.None,
    val bitmap: Bitmap? = null,
)

enum class RotationChamp {
    Free,
    FreeForNewPlayers,
    Both,
    None
}
