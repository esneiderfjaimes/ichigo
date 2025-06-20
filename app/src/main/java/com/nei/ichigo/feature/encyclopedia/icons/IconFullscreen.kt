package com.nei.ichigo.feature.encyclopedia.icons

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.nei.ichigo.R
import com.nei.ichigo.core.designsystem.ZoomableBox3
import com.nei.ichigo.core.designsystem.component.AsyncImagePreviewProvider

context(SharedTransitionScope, AnimatedVisibilityScope)
@Composable
fun IconFullscreen(
    icon: IconUi,
    version: String,
    requestClose: () -> Unit
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f)),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "#" + icon.id,
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = stringResource(R.string.icon),
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                },
                actions = {
                    FilledTonalIconButton(onClick = requestClose) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            ZoomableBox3(
                modifier = Modifier,
                maxZoom = 5f,
                minZoom = 1f,
                doubleTapZoom = 2f,
                content = { modifier ->
                    ProfileIconItem(
                        icon = icon,
                        version = version,
                        size = ITEM_SIZE * 2,
                    )
                }
            )
        }
    }

    BackHandler {
        requestClose()
    }
}

@Preview
@Composable
private fun IconFullscreenPreview() {
    AsyncImagePreviewProvider {
        SharedTransitionLayout {
            AnimatedContent(true) {
                if (it) {
                    IconFullscreen(
                        icon = IconUi(
                            id = "1",
                            image = "https://ddragon.leagueoflegends.com/cdn/13.19.1/img/profileicon/1.png"
                        ),
                        version = "13.19.1",
                        requestClose = {}
                    )
                }
            }
        }
    }
}