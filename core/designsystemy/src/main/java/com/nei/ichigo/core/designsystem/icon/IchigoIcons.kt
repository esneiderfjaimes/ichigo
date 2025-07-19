package com.nei.ichigo.core.designsystem.icon

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.ui.graphics.vector.PathBuilder

object IchigoIcons {

    val ProfileIcons = Icons.Default.Image
    val Items = Icons.Default.Circle
    val Spells = Icons.Default.Circle
    val Settings = Icons.Rounded.Settings

    inline fun path(pathBuilder: PathBuilder.() -> Unit) = materialIcon(name = "Champion") {
        materialPath(pathBuilder = pathBuilder)
    }

}
