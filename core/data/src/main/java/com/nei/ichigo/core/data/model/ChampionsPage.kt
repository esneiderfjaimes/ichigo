package com.nei.ichigo.core.data.model

import com.nei.ichigo.core.model.Champion

data class ChampionsPage(
    val version: String,
    val lang: String,
    val champions: List<Champion>
)