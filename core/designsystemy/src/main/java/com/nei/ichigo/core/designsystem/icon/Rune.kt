package com.nei.ichigo.core.designsystem.icon

import androidx.compose.material.icons.materialPath
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

private var _rune: ImageVector? = null

inline fun materialIcon(
    name: String,
    autoMirror: Boolean = false,
    block: ImageVector.Builder.() -> ImageVector.Builder
): ImageVector = ImageVector.Builder(
    name = name,
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 32f,
    viewportHeight = 32f,
    autoMirror = autoMirror
).block().build()

val IchigoIcons.Rune: ImageVector
    get() {
        if (_rune != null) {
            return _rune!!
        }
        _rune = materialIcon("rune") {
            materialPath {
                moveTo(5.0f, 15.0f)
                curveTo(5.99f, 15.0f, 6.98f, 15.0f, 8.0f, 15.0f)
                curveTo(8.0f, 15.66f, 8.0f, 16.32f, 8.0f, 17.0f)
                curveTo(8.887f, 16.979f, 9.774f, 16.959f, 10.688f, 16.938f)
                curveTo(15.205f, 18.387f, 16.009f, 20.702f, 18.117f, 24.773f)
                curveTo(19.0f, 27.0f, 19.0f, 27.0f, 19.0f, 30.0f)
                curveTo(17.68f, 30.33f, 16.36f, 30.66f, 15.0f, 31.0f)
                curveTo(14.697f, 30.481f, 14.394f, 29.961f, 14.082f, 29.426f)
                curveTo(11.991f, 25.944f, 10.081f, 23.081f, 7.188f, 20.188f)
                curveTo(6.466f, 19.466f, 5.744f, 18.744f, 5.0f, 18.0f)
                curveTo(5.0f, 17.01f, 5.0f, 16.02f, 5.0f, 15.0f)
                close()
            }
            materialPath {
                moveTo(14.0f, 1.0f)
                curveTo(15.75f, 1.125f, 15.75f, 1.125f, 18.0f, 2.0f)
                curveTo(19.362f, 3.981f, 20.696f, 5.981f, 22.0f, 8.0f)
                curveTo(22.99f, 8.33f, 23.98f, 8.66f, 25.0f, 9.0f)
                curveTo(24.67f, 10.32f, 24.34f, 11.64f, 24.0f, 13.0f)
                curveTo(22.02f, 13.33f, 20.04f, 13.66f, 18.0f, 14.0f)
                curveTo(13.357f, 6.211f, 13.357f, 6.211f, 13.0f, 3.0f)
                curveTo(13.33f, 2.34f, 13.66f, 1.68f, 14.0f, 1.0f)
                close()
            }
            materialPath {
                moveTo(5.0f, 15.0f)
                curveTo(5.99f, 15.0f, 6.98f, 15.0f, 8.0f, 15.0f)
                curveTo(8.0f, 15.66f, 8.0f, 16.32f, 8.0f, 17.0f)
                curveTo(10.97f, 17.495f, 10.97f, 17.495f, 14.0f, 18.0f)
                curveTo(13.01f, 18.495f, 13.01f, 18.495f, 12.0f, 19.0f)
                curveTo(12.0f, 20.65f, 12.0f, 22.3f, 12.0f, 24.0f)
                curveTo(10.825f, 23.073f, 9.66f, 22.133f, 8.5f, 21.188f)
                curveTo(7.525f, 20.404f, 7.525f, 20.404f, 6.531f, 19.605f)
                curveTo(5.0f, 18.0f, 5.0f, 18.0f, 5.0f, 15.0f)
                close()
            }
            materialPath {
                moveTo(18.0f, 19.0f)
                curveTo(20.339f, 19.287f, 22.674f, 19.619f, 25.0f, 20.0f)
                curveTo(23.384f, 22.041f, 21.714f, 24.041f, 20.0f, 26.0f)
                curveTo(19.34f, 26.0f, 18.68f, 26.0f, 18.0f, 26.0f)
                curveTo(17.619f, 24.009f, 17.287f, 22.007f, 17.0f, 20.0f)
                curveTo(17.33f, 19.67f, 17.66f, 19.34f, 18.0f, 19.0f)
                close()
            }
            materialPath {
                moveTo(8.0f, 8.0f)
                curveTo(10.475f, 8.495f, 10.475f, 8.495f, 13.0f, 9.0f)
                curveTo(13.33f, 11.31f, 13.66f, 13.62f, 14.0f, 16.0f)
                curveTo(10.839f, 14.63f, 10.007f, 14.011f, 8.0f, 11.0f)
                curveTo(8.0f, 10.01f, 8.0f, 9.02f, 8.0f, 8.0f)
                close()
            }
            materialPath {
                moveTo(20.0f, 15.0f)
                curveTo(21.98f, 15.0f, 23.96f, 15.0f, 26.0f, 15.0f)
                curveTo(25.67f, 16.32f, 25.34f, 17.64f, 25.0f, 19.0f)
                curveTo(23.063f, 18.688f, 23.063f, 18.688f, 21.0f, 18.0f)
                curveTo(20.67f, 17.01f, 20.34f, 16.02f, 20.0f, 15.0f)
                close()
            }
        }
        return _rune!!
    }