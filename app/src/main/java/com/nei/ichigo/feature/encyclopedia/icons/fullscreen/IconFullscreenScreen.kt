package com.nei.ichigo.feature.encyclopedia.icons.fullscreen

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBackIos
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.nei.ichigo.R
import com.nei.ichigo.common.utils.LocalSharedTransitionScope
import com.nei.ichigo.common.utils.SharedTransitionPreviewProvider
import com.nei.ichigo.core.designsystem.ZoomableBox3
import com.nei.ichigo.core.designsystem.component.DEFAULT_ITEM_SIZE
import com.nei.ichigo.core.designsystem.component.TransparentTopAppBar
import com.nei.ichigo.core.designsystem.theme.IchigoThemePreview
import com.nei.ichigo.feature.encyclopedia.icons.IconUi
import com.nei.ichigo.feature.encyclopedia.icons.ProfileIconItem

@Composable
context(animatedContentScope: AnimatedContentScope)
fun IconFullscreenScreen(
    icon: IconUi,
    version: String,
    onBackPress: () -> Unit
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f)),
        topBar = {
            TransparentTopAppBar(
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
                navigationIcon = {
                    IconButton(onClick = onBackPress) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBackIos,
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
                modifier = Modifier.fillMaxSize(),
                maxZoom = 5f,
                minZoom = 1f,
                doubleTapZoom = 2f,
                content = { modifier ->
                    val sharedTransitionScope = LocalSharedTransitionScope.current
                    with(sharedTransitionScope) {
                        ProfileIconItem(
                            icon = icon,
                            version = version,
                            size = DEFAULT_ITEM_SIZE * 2,
                        )
                    }
                }
            )
        }
    }
}

@PreviewLightDark
@Composable
fun IconFullscreenScreenPreview() {
    IchigoThemePreview {
        SharedTransitionPreviewProvider {
            IconFullscreenScreen(
                icon = IconUi(
                    id = "1",
                    image = "https://ddragon.leagueoflegends.com/cdn/13.19.1/img/profileicon/1.png"
                ),
                version = "13.19.1",
                onBackPress = {}
            )
        }
    }
}