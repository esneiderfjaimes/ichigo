package com.red.code015.ui.common

import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

@Composable
fun Color.ApplyOn(content: @Composable (Color) -> Unit) {
    CompositionLocalProvider(LocalContentColor provides contentColorFor(backgroundColor = this)) {
        content(this)
    }
}