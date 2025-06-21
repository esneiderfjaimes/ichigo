package com.nei.ichigo.feature.encyclopedia.champion

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nei.ichigo.R
import com.nei.ichigo.core.designsystem.ZoomableBox3
import com.nei.ichigo.core.designsystem.component.AsyncImage
import com.nei.ichigo.core.designsystem.component.AsyncImagePreviewProvider
import com.nei.ichigo.core.designsystem.component.BottomPager
import com.nei.ichigo.core.designsystem.component.PageInfo
import com.nei.ichigo.core.designsystem.utils.getChampionSkinImage
import com.nei.ichigo.core.model.Skin

// context(SharedTransitionScope)
@Composable
fun SkinFullscreen(
    championId: String,
    skins: List<Skin>,
    selectedSkin: Int?,
    onSelectSkin: (Int?) -> Unit = {}
) {
    AnimatedContent(
        targetState = selectedSkin,
        transitionSpec = { fadeIn() togetherWith fadeOut() },
        label = "SkinFullscreen"
    ) { selectedSkin ->
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (selectedSkin == null) {
                Log.i("SkinFullscreen", "SkinFullscreen:  selectedSkin == null")
            } else {
                var pager by remember {
                    mutableStateOf(
                        PageInfo(
                            selectedSkin,
                            skins.size
                        )
                    )
                }

                val skin = skins.getOrNull(selectedSkin) ?: return@Box

                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Column {
                                    Text(skin.name, style = MaterialTheme.typography.titleLarge)
                                    Text(
                                        stringResource(R.string.skin),
                                        style = MaterialTheme.typography.labelMedium
                                    )
                                }
                            },
                            navigationIcon = {
                                IconButton(onClick = { onSelectSkin(null) }) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                                        contentDescription = "Close"
                                    )
                                }
                            }
                        )
                    },
                    bottomBar = {
                        BottomPager(
                            pageInfo = pager,
                            title = { stringResource(R.string.select_skin) },
                            itemLabel = { index ->
                                val skin = skins[index]
                                skin.name
                            },
                            onSelectPage = onSelectSkin
                        )
                    },
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable(
                                interactionSource = null,
                                indication = null,
                                onClick = {
                                    onSelectSkin(null)
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        ZoomableBox3(
                            modifier = Modifier,
                            maxZoom = 5f,
                            minZoom = 1f,
                            doubleTapZoom = 2f,
                            content = { modifier ->
                                AsyncImage(
                                    modifier = modifier
                                        .padding(16.dp)
                                        .padding(innerPadding)
                                        .clip(MaterialTheme.shapes.extraLarge),
                                    /*   .clickable(onClick = requestClose)*/
                                    //.height(400.dp),
                                    model = getChampionSkinImage(
                                        championId,
                                        skin.num
                                    ),
                                )
                            }
                        )
                    }

                    BackHandler {
                        onSelectSkin(null)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun SkinFullscreenPreview() {
    AsyncImagePreviewProvider {
        SkinFullscreen(
            championId = "1",
            skins = listOf(
                Skin(id = "1", num = 1, name = "Aatrox", chromas = false),
                Skin(id = "2", num = 2, name = "Aatrox", chromas = false),
                Skin(id = "3", num = 3, name = "Aatrox", chromas = false),
            ),
            selectedSkin = 0
        )
    }
}