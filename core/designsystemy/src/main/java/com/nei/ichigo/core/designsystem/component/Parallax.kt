@file:Suppress("unused")

package com.nei.ichigo.core.designsystem.component

import androidx.compose.foundation.ScrollState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout

fun Modifier.parallaxLayoutModifier(
    scrollState: ScrollState,
    rate: Double
) = parallaxLayoutModifier { (scrollState.value / rate).toInt() }

fun Modifier.parallaxLayoutModifier(
    scrollState: ScrollState,
    rate: Int
) = parallaxLayoutModifier { (scrollState.value / rate) }

fun Modifier.parallaxLayoutModifier(
    heightProvider: () -> Int
) = layout { measurable, constraints ->
    val placeable = measurable.measure(constraints)
    val height = heightProvider()
    layout(placeable.width, placeable.height) {
        placeable.place(0, height)
    }
}