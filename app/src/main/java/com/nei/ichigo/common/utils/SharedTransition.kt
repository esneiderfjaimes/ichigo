package com.nei.ichigo.common.utils

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable

@SuppressLint("UnusedContentLambdaTargetStateParameter")
@Composable
fun SharedTransitionPreviewProvider(content: @Composable SharedTransitionScope.(AnimatedContentScope) -> Unit) {
    SharedTransitionLayout {
        AnimatedContent(true) {
            content(this@SharedTransitionLayout, this)
        }
    }
}