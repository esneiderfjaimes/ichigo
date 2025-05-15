package com.nei.ichigo.core.database.model

import androidx.room.Entity

@Entity(
    tableName = "champion",
    primaryKeys = ["version", "lang", "id"]
)
data class ChampionEntity(
    val version: String,
    val lang: String,
    val id: String,
    val name: String,
    val image: String,
    val tags: List<String>,
)
