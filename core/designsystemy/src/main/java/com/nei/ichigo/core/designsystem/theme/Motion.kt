package com.nei.ichigo.core.designsystem.theme

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.material3.MotionScheme

@Suppress("UNCHECKED_CAST")
fun noMotionScheme() = object : MotionScheme {
    private val noAnimationSpec = tween<Any>(0)

    override fun <T> defaultSpatialSpec() = noAnimationSpec as FiniteAnimationSpec<T>

    override fun <T> fastSpatialSpec() = noAnimationSpec as FiniteAnimationSpec<T>

    override fun <T> slowSpatialSpec() = noAnimationSpec as FiniteAnimationSpec<T>

    override fun <T> defaultEffectsSpec() = noAnimationSpec as FiniteAnimationSpec<T>

    override fun <T> fastEffectsSpec() = noAnimationSpec as FiniteAnimationSpec<T>

    override fun <T> slowEffectsSpec() = noAnimationSpec as FiniteAnimationSpec<T>
}
