package com.red.code015.ui.theme.icons

import androidx.compose.material.icons.materialIcon
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path

private var _chest: ImageVector? = null

val IconsLoL.Chest: ImageVector
    get() {
        if (_chest != null) {
            return _chest!!
        }
        _chest = materialIcon(name = "LoL.Rank") {
            val vectorSize = 24f
            val border = 2.5f
            val margin = border / 2

            path(
                stroke = Brush.linearGradient(
                    listOf(Color(0xFFC89C3C), Color(0xFF805E18)),
                    Offset(12f, 12f),
                    Offset(14f, 24f),
                    tileMode = TileMode.Mirror
                ),
                strokeLineWidth = 0.5f,
            ) {
                moveTo(border + margin, border)
                lineToRelative(border, border * -1)

                moveTo(vectorSize - border - margin, border)
                lineToRelative(border * -1, border * -1)

                moveTo(vectorSize - border, vectorSize - border - margin)
                lineToRelative(border, border * -1)

                moveTo(vectorSize - border, border + margin)
                lineToRelative(border, border)

                moveTo(border, vectorSize - border - margin)
                lineToRelative(border * -1, border * -1)

                moveTo(border, border + margin)
                lineToRelative(border * -1, border)

                moveTo(border, border)
                horizontalLineToRelative(vectorSize - (border * 2))
                verticalLineToRelative(vectorSize - (border * 2))
                horizontalLineToRelative((vectorSize - (border * 2)) * -1)
                close()
            }

            path(
                stroke = SolidColor(Color(0xFFC89C3C)),
                strokeLineWidth = 0.25f,
            ) {
                moveTo(1f, 1f)
                horizontalLineToRelative(22f)
                verticalLineToRelative(22f)
                horizontalLineToRelative(-22f)
                close()
            }

            path(
                fill = Brush.radialGradient(
                    listOf(Color(0xFFC89C3C), Color(0xFF553D0B)),
                    center = Offset(12f, 0f), radius = 4f,
                )
            ) {
                // M 60 20 L 80 10 V 0 h -40 V 10 Z
                moveTo(12f, 4f)
                lineTo(14f, 2.75f)
                verticalLineTo(0f)
                horizontalLineToRelative(-4f)
                verticalLineTo(2.75f)
                close()
            }

        }
        return _chest!!
    }