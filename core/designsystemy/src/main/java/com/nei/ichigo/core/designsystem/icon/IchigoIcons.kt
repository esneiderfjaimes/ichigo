package com.nei.ichigo.core.designsystem.icon

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.ui.graphics.vector.PathBuilder

object IchigoIcons {

    val ProfileIcons = Icons.Default.Image
    val Settings = Icons.Rounded.Settings

    inline fun path(name: String, pathBuilder: PathBuilder.() -> Unit) = materialIcon(name = name) {
        materialPath(pathBuilder = pathBuilder)
    }

}
