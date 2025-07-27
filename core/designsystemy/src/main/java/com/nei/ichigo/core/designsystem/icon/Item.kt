package com.nei.ichigo.core.designsystem.icon

import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.ui.graphics.vector.ImageVector

private var _item: ImageVector? = null

val IchigoIcons.Item: ImageVector
    get() {
        if (_item != null) {
            return _item!!
        }
        _item = materialIcon("item") {
            materialPath {
                moveToRelative(3.858f, 19.975f)
                curveToRelative(-0.148f, 0.26f, -0.187f, 0.58f, -0.108f, 0.868f)
                curveToRelative(0.08f, 0.288f, 0.278f, 0.542f, 0.538f, 0.69f)
                curveToRelative(0.26f, 0.148f, 0.58f, 0.187f, 0.868f, 0.108f)
                curveToRelative(0.288f, -0.08f, 0.542f, -0.278f, 0.69f, -0.538f)
                curveToRelative(0.148f, -0.26f, 0.187f, -0.58f, 0.108f, -0.868f)
                curveTo(5.874f, 19.946f, 5.677f, 19.692f, 5.416f, 19.545f)
                curveTo(5.156f, 19.397f, 4.837f, 19.358f, 4.548f, 19.437f)
                curveToRelative(-0.288f, 0.08f, -0.542f, 0.278f, -0.69f, 0.538f)
                close()
            }
            materialPath {
                moveToRelative(8.856f, 16.594f)
                curveToRelative(0.802f, -0.024f, 2.406f, -0.072f, 3.291f, -0.198f)
                curveToRelative(0.884f, -0.125f, 1.048f, -0.328f, 1.143f, -0.539f)
                curveToRelative(0.095f, -0.211f, 0.121f, -0.429f, -0.5f, -0.838f)
                curveToRelative(-0.621f, -0.408f, -1.887f, -1.006f, -3.136f, -1.312f)
                curveToRelative(-1.249f, -0.305f, -2.48f, -0.318f, -3.16f, -0.272f)
                curveToRelative(-0.68f, 0.047f, -0.809f, 0.153f, -0.942f, 0.294f)
                curveToRelative(-0.133f, 0.141f, -0.272f, 0.318f, -0.264f, 0.526f)
                curveToRelative(0.008f, 0.207f, 0.162f, 0.445f, 0.49f, 0.765f)
                curveToRelative(0.328f, 0.32f, 0.829f, 0.722f, 1.238f, 1.001f)
                curveToRelative(0.408f, 0.28f, 0.723f, 0.437f, 0.881f, 0.516f)
                curveToRelative(0.158f, 0.079f, 0.158f, 0.079f, 0.96f, 0.055f)
                close()
            }
            materialPath {
                moveTo(5.826f, 20.737f)
                lineTo(11.005f, 14.149f)
                lineTo(9.635f, 12.982f)
                lineTo(4.369f, 19.907f)
                close()
            }
            materialPath {
                moveToRelative(13.195f, 15.664f)
                curveToRelative(0.0f, 0.0f, 0.034f, -1.177f, 0.834f, -2.378f)
                curveToRelative(0.801f, -1.201f, 1.683f, -1.259f, 1.683f, -1.259f)
                curveToRelative(0.0f, 0.0f, -0.298f, -1.252f, 0.377f, -1.9f)
                curveToRelative(0.675f, -0.648f, 0.102f, -0.539f, 1.93f, -1.603f)
                curveToRelative(1.828f, -1.064f, 1.719f, -0.614f, 1.719f, -0.614f)
                lineToRelative(0.191f, -4.665f)
                curveToRelative(0.0f, 0.0f, 0.068f, -0.777f, -0.055f, -0.887f)
                curveToRelative(-0.123f, -0.109f, -0.273f, -0.205f, -1.269f, 0.191f)
                curveToRelative(-0.996f, 0.396f, -3.724f, 2.101f, -4.87f, 2.892f)
                curveToRelative(-1.146f, 0.791f, -1.228f, 0.982f, -1.228f, 0.982f)
                curveToRelative(0.0f, 0.0f, 0.745f, 0.526f, 0.156f, 1.379f)
                curveTo(11.97f, 8.808f, 12.02f, 9.079f, 10.288f, 9.911f)
                curveTo(9.999f, 11.8f, 8.364f, 12.518f, 8.364f, 12.518f)
                curveToRelative(0.0f, 0.0f, -0.994f, 0.818f, -1.779f, 1.116f)
                curveToRelative(-0.784f, 0.298f, 6.61f, 2.03f, 6.61f, 2.03f)
                close()
            }
        }
        return _item!!
    }