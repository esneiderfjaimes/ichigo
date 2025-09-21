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

val LocalAnimatedContentScope = staticCompositionLocalOf<AnimatedContentScope> {
    error("No AnimatedContentScope provided")
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

@Composable
fun AnimatedContentScope.AnimatedContentScopeProvider(content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalAnimatedContentScope provides this@AnimatedContentScopeProvider
    ) {
        content()
    }
}

@SuppressLint("UnusedContentLambdaTargetStateParameter")
@Composable
fun SharedTransitionPreviewProvider(content: @Composable () -> Unit) {
    SharedTransitionLayout {
        AnimatedContent(true) {
            CompositionLocalProvider(
                LocalSharedTransitionScope provides this@SharedTransitionLayout,
                LocalAnimatedContentScope provides this,
                content = content
            )
        }
    }
}
