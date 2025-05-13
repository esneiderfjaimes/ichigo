package com.red.code015.ui.theme.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.EvenOdd
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val IconsLoL.Hot: ImageVector
    get() {
        if (_hot != null) {
            return _hot!!
        }
        _hot = Builder(name = "IcRacha", defaultWidth = 10.0.dp, defaultHeight = 12.0.dp,
                viewportWidth = 10.0f, viewportHeight = 12.0f).apply {
            path(fill = SolidColor(Color(0xFFFF9417)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = EvenOdd) {
                moveTo(3.25f, 4.6875f)
                curveTo(5.2335f, 3.8795f, 5.0f, 0.5f, 5.0f, 0.5f)
                curveTo(7.9521f, 1.7349f, 9.9265f, 5.6949f, 9.0f, 8.625f)
                curveTo(7.8819f, 12.132f, 2.8509f, 12.5362f, 1.1875f, 9.25f)
                curveTo(-0.6339f, 5.6618f, 2.875f, 2.6875f, 2.875f, 2.6875f)
                curveTo(2.8783f, 3.3629f, 3.0233f, 4.0504f, 3.25f, 4.6875f)
                close()
                moveTo(3.9375f, 10.1875f)
                curveTo(4.5465f, 10.6707f, 5.4103f, 10.6707f, 6.0192f, 10.1875f)
                curveTo(6.6282f, 9.7043f, 7.0551f, 8.9422f, 7.0625f, 8.125f)
                curveTo(7.0625f, 6.1841f, 5.0f, 4.5625f, 5.0f, 4.5625f)
                curveTo(5.0f, 4.5625f, 2.875f, 6.182f, 2.875f, 8.125f)
                curveTo(2.8834f, 8.9419f, 3.269f, 9.7075f, 3.9375f, 10.1875f)
                close()
            }
        }
        .build()
        return _hot!!
    }

private var _hot: ImageVector? = null
