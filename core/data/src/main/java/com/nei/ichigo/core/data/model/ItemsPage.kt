package com.nei.ichigo.core.data.model

import com.nei.ichigo.core.model.Item

data class ItemsPage(
    val version: String,
    val lang: String,
    val icons: List<Item>,
)