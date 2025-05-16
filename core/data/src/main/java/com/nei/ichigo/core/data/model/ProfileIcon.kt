package com.nei.ichigo.core.data.model

import com.nei.ichigo.core.database.model.ProfileIconEntity
import com.nei.ichigo.core.model.ProfileIcon

fun ProfileIcon.asEntity(version: String, lang: String) = ProfileIconEntity(
    version = version,
    lang = lang,
    code = id,
    image = image
)

fun ProfileIconEntity.asExternalModel() = ProfileIcon(code, image)
