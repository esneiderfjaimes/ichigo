package com.red.code015.ui.theme.icons

import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val IconsLoL.Mastery: ImageVector
    get() {
        if (_mastery != null) {
            return _mastery!!
        }
        _mastery = ImageVector.Builder(name = "IcMastery",
            defaultWidth = 24.0.dp,
            defaultHeight = 24.0.dp,
            viewportWidth = 128.0f,
            viewportHeight = 128.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = StrokeCap.Butt, strokeLineJoin = StrokeJoin.Miter, strokeLineMiter = 4.0f,
                pathFillType = PathFillType.NonZero) {
                moveTo(8.865f, 23.524f)
                reflectiveCurveToRelative(2.654f, 3.7f, 6.718f, 8.593f)
                arcToRelative(128.428f, 128.428f, 0.0f, false, false, 22.417f, 21.043f)
                curveToRelative(11.442f, 8.486f, 9.146f, 30.971f, 9.146f, 51.316f)
                curveToRelative(0.0f, 0.0f, -2.171f, -9.8f, -19.219f, -15.741f)
                curveToRelative(0.0f, 0.0f, -14.836f, -6.779f, -12.935f, -16.4f)
                curveToRelative(0.0f, 0.0f, 8.414f, 7.174f, 14.918f, 4.415f)
                curveToRelative(0.0f, 0.0f, -26.331f, -13.823f, -21.37f, -27.7f)
                curveToRelative(0.0f, 0.0f, 7.786f, 8.5f, 14.248f, 10.071f)
                curveToRelative(-0.001f, 0.001f, -21.139f, -22.852f, -13.923f, -35.597f)
                close()
                moveTo(119.135f, 23.524f)
                reflectiveCurveToRelative(-2.654f, 3.7f, -6.718f, 8.593f)
                arcToRelative(128.428f, 128.428f, 0.0f, false, true, -22.417f, 21.043f)
                curveToRelative(-11.442f, 8.486f, -9.146f, 31.24f, -9.146f, 51.316f)
                curveToRelative(0.0f, 0.0f, 2.171f, -9.8f, 19.219f, -15.741f)
                curveToRelative(0.0f, 0.0f, 14.836f, -6.779f, 12.935f, -16.4f)
                curveToRelative(0.0f, 0.0f, -8.414f, 7.174f, -14.918f, 4.415f)
                curveToRelative(0.0f, 0.0f, 26.331f, -13.823f, 21.369f, -27.7f)
                curveToRelative(0.0f, 0.0f, -7.785f, 8.5f, -14.247f, 10.071f)
                curveToRelative(0.001f, 0.001f, 21.139f, -22.852f, 13.923f, -35.597f)
                close()
                moveTo(53.0f, 50.0f)
                verticalLineToRelative(60.0f)
                lineToRelative(10.0f, 10.0f)
                quadToRelative(1.0f, 1.0f, 2.0f, 0.0f)
                lineToRelative(10.0f, -10.0f)
                verticalLineToRelative(-60.0f)
                quadToRelative(-11.0f, -3.0f, -22.0f, 0.0f)
                close()
            }
        }
            .build()
        return _mastery!!
    }

private var _mastery: ImageVector? = null