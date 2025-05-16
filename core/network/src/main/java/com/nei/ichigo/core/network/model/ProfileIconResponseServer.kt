package com.nei.ichigo.core.network.model

import com.nei.ichigo.core.model.ProfileIcon

data class ProfileIconResponseServer(
    val id: String?,
    val image: ImageResponseServer?
)

fun ProfileIconResponseServer.asExternalModel() = ProfileIcon(
    id = id!!,
    image = image!!.full!!
)