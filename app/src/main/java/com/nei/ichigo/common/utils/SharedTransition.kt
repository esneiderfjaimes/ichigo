package com.nei.ichigo.common.utils

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier

val LocalEnableSharedTransition = staticCompositionLocalOf { true }

val LocalSharedTransitionScope = staticCompositionLocalOf<SharedTransitionScope> {
    error("No SharedTransitionScope provided")
}

val LocalAnimatedVisibilityScope = staticCompositionLocalOf<AnimatedVisibilityScope> {
    error("No AnimatedVisibilityScope provided")
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
fun AnimatedVisibilityScope.AnimatedVisibilityScopeProvider(content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalAnimatedVisibilityScope provides this@AnimatedVisibilityScopeProvider
    ) {
        content()
    }
}

@Composable
fun Modifier.withSharedTransitionScope(
    scope: @Composable SharedTransitionScope.(modifier: Modifier) -> Modifier
): Modifier {
    val enableSharedTransition = LocalEnableSharedTransition.current
    if (!enableSharedTransition) {
        return this
    }

    val sharedTransitionScope = LocalSharedTransitionScope.current
    return with(sharedTransitionScope) {
        scope(this@withSharedTransitionScope)
    }
}

@SuppressLint("UnusedContentLambdaTargetStateParameter")
@Composable
fun SharedTransitionPreviewProvider(content: @Composable () -> Unit) {
    SharedTransitionLayout {
        AnimatedVisibility(true) {
            CompositionLocalProvider(
                LocalSharedTransitionScope provides this@SharedTransitionLayout,
                LocalAnimatedVisibilityScope provides this,
                content = content
            )
        }
    }
}
