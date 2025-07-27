package com.nei.ichigo.core.designsystem.icon

import androidx.compose.ui.graphics.vector.ImageVector

private var _champion: ImageVector? = null

val IchigoIcons.Champion: ImageVector
    get() {
        if (_champion != null) {
            return _champion!!
        }
        _champion = path(name = "champion") {
            moveToRelative(11.999f, 2.0f)
            curveToRelative(0.0f, 0.0f, -7.789f, 2.174f, -7.156f, 4.843f)
            curveToRelative(0.632f, 2.669f, 0.022f, 4.626f, 0.101f, 5.081f)
            curveToRelative(0.079f, 0.455f, -1.523f, 3.44f, -2.097f, 3.994f)
            curveToRelative(-0.573f, 0.554f, 7.253f, 7.631f, 5.849f, 5.772f)
            curveToRelative(-1.404f, -1.858f, -0.926f, -4.745f, -0.926f, -4.745f)
            curveToRelative(0.0f, 0.0f, 0.019f, -0.06f, 0.968f, -3.124f)
            curveToRelative(-3.182f, -1.66f, -2.173f, -4.466f, -2.173f, -4.466f)
            curveToRelative(0.0f, 0.0f, 3.872f, -0.179f, 4.05f, 2.746f)
            curveToRelative(0.178f, 2.925f, -0.077f, 2.271f, 1.384f, 4.86f)
            curveToRelative(1.461f, -2.589f, 1.206f, -1.935f, 1.384f, -4.86f)
            curveToRelative(0.178f, -2.925f, 4.05f, -2.746f, 4.05f, -2.746f)
            curveToRelative(0.0f, 0.0f, 1.009f, 2.805f, -2.173f, 4.466f)
            curveToRelative(0.949f, 3.064f, 0.968f, 3.124f, 0.968f, 3.124f)
            curveToRelative(0.0f, 0.0f, 0.474f, 2.887f, -0.929f, 4.745f)
            curveToRelative(-1.403f, 1.858f, 6.426f, -5.219f, 5.853f, -5.772f)
            curveToRelative(-0.573f, -0.554f, -2.176f, -3.539f, -2.097f, -3.994f)
            curveToRelative(0.079f, -0.455f, -0.535f, -2.412f, 0.098f, -5.081f)
            curveTo(19.785f, 4.174f, 11.999f, 2.0f, 11.999f, 2.0f)
            close()
        }
        return _champion!!
    }