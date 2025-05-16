package com.nei.ichigo.core.data.model

import com.nei.ichigo.core.model.ProfileIcon

data class ProfileIconsPage(
    val version: String,
    val lang: String,
    val icons: List<ProfileIcon>
)