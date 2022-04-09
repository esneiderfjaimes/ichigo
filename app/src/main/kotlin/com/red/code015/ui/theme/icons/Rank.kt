package com.red.code015.ui.theme.icons

import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.ui.graphics.vector.ImageVector

object IconsLoL

val IconsLoL.Rank: ImageVector
    get() {
        if (_rank != null) {
            return _rank!!
        }
        _rank = materialIcon(name = "LoL.Rank") {
            materialPath {
                addGroup(scaleX = 0.5f,
                    scaleY = 0.5f)

                moveTo(3.00896f, 7.49243f)
                curveTo(3.89044f, 12.6228f, 12.36f, 17.7662f, 16.4845f, 19.6967f)
                curveTo(13.6596f, 23.0585f, 13.5464f, 30.1777f, 17.6992f, 33.4265f)
                curveTo(21.0215f, 36.0256f, 21.174f, 38.6529f, 20.9198f, 41.1672f)
                curveTo(18.8858f, 36.1951f, 15.91f, 34.4341f, 13.9984f, 33.4265f)
                curveTo(13.6875f, 34.839f, 14.2244f, 36.5247f, 14.6764f, 37.2403f)
                curveTo(10.1337f, 34.9351f, 6.42722f, 27.4374f, 6.42722f, 24.8101f)
                curveTo(7.38262f, 25.999f, 8.54581f, 26.1758f, 9.12044f, 26.2631f)
                curveTo(9.1575f, 26.2687f, 9.19211f, 26.274f, 9.22406f, 26.2791f)
                curveTo(9.22406f, 25.1208f, 9.02628f, 23.6235f, 6.25769f, 20.9397f)
                curveTo(3.21069f, 17.986f, 1.87884f, 12.0125f, 3.00896f, 7.49243f)
                close()

                moveTo(30.7511f, 25.9401f)
                curveTo(30.7511f, 29.6067f, 27.7788f, 32.579f, 24.1122f, 32.579f)
                curveTo(20.4456f, 32.579f, 17.4733f, 29.6067f, 17.4733f, 25.9401f)
                curveTo(17.4733f, 22.2735f, 20.4456f, 19.3012f, 24.1122f, 19.3012f)
                curveTo(27.7788f, 19.3012f, 30.7511f, 22.2735f, 30.7511f, 25.9401f)
                close()

                moveTo(31.4229f, 19.6967f)

                curveTo(35.5475f, 17.7663f, 44.017f, 12.6228f, 44.8985f, 7.49248f)
                curveTo(46.0286f, 12.0126f, 44.6968f, 17.986f, 41.6498f, 20.9398f)
                curveTo(38.8812f, 23.6236f, 38.6834f, 25.1209f, 38.6834f, 26.2791f)
                curveTo(38.7154f, 26.274f, 38.75f, 26.2687f, 38.787f, 26.2631f)
                curveTo(39.3617f, 26.1758f, 40.5249f, 25.9991f, 41.4803f, 24.8101f)
                curveTo(41.4803f, 27.4374f, 37.7738f, 34.9351f, 33.2311f, 37.2404f)
                curveTo(33.6831f, 36.5247f, 34.22f, 34.8391f, 33.9091f, 33.4266f)
                curveTo(31.9975f, 34.4342f, 29.0217f, 36.1951f, 26.9877f, 41.1672f)
                curveTo(26.7335f, 38.6529f, 26.886f, 36.0256f, 30.2082f, 33.4266f)
                curveTo(34.3611f, 30.1777f, 34.2479f, 23.0586f, 31.4229f, 19.6967f)
                close()
            }
        }
        return _rank!!
    }

private var _rank: ImageVector? = null