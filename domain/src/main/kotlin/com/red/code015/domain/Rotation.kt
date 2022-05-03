package com.red.code015.domain

data class ChampionsRotation(
    val freeChampionIds: List<String>,
    val freeChampionIdsForNewPlayers: List<String>,
    val maxNewPlayerLevel: Int,
    val dataSource: DataSource,
)