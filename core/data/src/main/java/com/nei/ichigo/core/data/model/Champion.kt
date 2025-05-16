package com.nei.ichigo.core.data.model

import com.nei.ichigo.core.database.model.ChampionEntity
import com.nei.ichigo.core.model.Champion

fun Champion.asEntity(version: String, lang: String) = ChampionEntity(
    version = version,
    lang = lang,
    code = id,
    name = name,
    image = image,
    tags = tags
)

fun ChampionEntity.asExternalModel() = Champion(
    id = code,
    name = name,
    image = image,
    tags = tags
)