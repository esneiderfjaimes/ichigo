package com.nei.ichigo.core.data.model

import com.nei.ichigo.core.model.ChampionDetail

data class ChampionPage(
    val version: String,
    val lang: String,
    val champion: ChampionDetail
)