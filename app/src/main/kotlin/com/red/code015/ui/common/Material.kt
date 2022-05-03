package com.red.code015.ui.common

import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import com.google.accompanist.placeholder.material.placeholder

@Composable
fun Color.ApplyOn(content: @Composable (Color) -> Unit) {
    CompositionLocalProvider(LocalContentColor provides contentColorFor(backgroundColor = this)) {
        content(this)
    }
}

fun Modifier.placeholderDefault(boolean: Boolean) = composed {
    placeholder(
        boolean,
        color = MaterialTheme.colorScheme.surface,
    )
}