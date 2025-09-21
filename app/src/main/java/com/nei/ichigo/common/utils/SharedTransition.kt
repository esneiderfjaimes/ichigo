package com.nei.ichigo.common.utils

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier

val LocalSharedTransitionScope = staticCompositionLocalOf<SharedTransitionScope> {
    error("No SharedTransitionScope provided")
}

@Composable
fun SharedTransitionProvider(content: @Composable SharedTransitionScope.(Modifier) -> Unit) {
    SharedTransitionScope { sharedTransitionModifier ->
        CompositionLocalProvider(
            LocalSharedTransitionScope provides this@SharedTransitionScope
        ) {
            content(sharedTransitionModifier)
        }
    }
}

@SuppressLint("UnusedContentLambdaTargetStateParameter")
@Composable
fun SharedTransitionPreviewProvider(content: @Composable AnimatedContentScope.() -> Unit) {
    SharedTransitionLayout {
        AnimatedContent(true) {
            CompositionLocalProvider(
                LocalSharedTransitionScope provides this@SharedTransitionLayout
            ) {
                content(this)
            }
        }
    }
}
