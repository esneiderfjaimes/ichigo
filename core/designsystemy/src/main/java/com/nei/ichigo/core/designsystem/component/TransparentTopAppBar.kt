package com.nei.ichigo.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBackIos
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun TransparentTopAppBar(
    text: AnnotatedString,
    modifier: Modifier = Modifier,
    actions: @Composable RowScope.() -> Unit = {},
) {
    TransparentTopAppBar(
        title = { Text(text = text) },
        modifier = modifier,
        actions = actions
    )
}

@Composable
fun TransparentTopAppBar(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit = {},
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    alpha: Float = 0.85f
) {
    TopAppBar(
        title = title,
        modifier = modifier,
        navigationIcon = navigationIcon,
        actions = actions,
        colors = TopAppBarDefaults.topAppBarColors()
            .let { colors ->
                colors.copy(
                    containerColor = colors.containerColor.copy(alpha = alpha),
                    scrolledContainerColor = colors.containerColor.copy(alpha = alpha),
                )
            },
        windowInsets = WindowInsets.safeDrawing.only(
            WindowInsetsSides.End + WindowInsetsSides.Top
        )
    )
}

@Preview
@Composable
private fun TransparentTopAppBarPreview() {
    Box {
        Box(
            Modifier
                .width(100.dp)
                .fillMaxHeight()
                .background(Color.Red)
        )
        Column {
            TransparentTopAppBar(
                title = { Text("Title") },
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBackIos,
                            contentDescription = "Close"
                        )
                    }
                }
            )
            TopAppBar(
                title = { Text("Title") },
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBackIos,
                            contentDescription = "Close"
                        )
                    }
                },
            )
            TransparentTopAppBar(
                title = { Text("Title") },
            )
            TopAppBar(
                title = { Text("Title") },
            )
        }
    }
}