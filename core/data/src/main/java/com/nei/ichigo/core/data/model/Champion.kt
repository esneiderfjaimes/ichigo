package com.nei.ichigo.core.data.model

import com.nei.ichigo.core.database.model.ChampionEntity
import com.nei.ichigo.core.model.Champion

fun Champion.asEntity(version: String, lang: String) = ChampionEntity(
    version = version,
    lang = lang,
    id = id,
    name = name,
    image = image,
    tags = tags
)

fun ChampionEntity.asExternalModel() = Champion(
    id = id,
    name = name,
    image = image,
    tags = tags
)