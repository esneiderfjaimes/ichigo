package com.nei.ichigo.feature.encyclopedia.skin.fullscreen

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
import androidx.compose.material.icons.automirrored.rounded.ArrowBackIos
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nei.ichigo.R
import com.nei.ichigo.common.BaseScreen
import com.nei.ichigo.common.UiState
import com.nei.ichigo.common.toSuccessUiState
import com.nei.ichigo.core.designsystem.ZoomableBox3
import com.nei.ichigo.core.designsystem.component.AsyncImage
import com.nei.ichigo.core.designsystem.component.AsyncImageDefaults
import com.nei.ichigo.core.designsystem.component.BottomPager
import com.nei.ichigo.core.designsystem.component.PageInfo
import com.nei.ichigo.core.designsystem.component.TransparentTopAppBar
import com.nei.ichigo.core.designsystem.theme.IchigoPreview
import com.nei.ichigo.core.designsystem.theme.IchigoPreviewWrapper
import com.nei.ichigo.core.designsystem.utils.getChampionSkinImage
import com.nei.ichigo.core.model.Skin

@Composable
fun SkinFullscreenScreen(
    championId: String,
    selectedSkinId: String?,
    onBackPress: () -> Unit,
) {
    val viewModel = hiltViewModel<SkinFullscreenViewModel, SkinFullscreenViewModel.Factory>(
        key = championId + selectedSkinId,
    ) { factory ->
        factory.create(SkinFullscreenViewModel.Factory.Args(championId, selectedSkinId))
    }
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    SkinFullscreenScreen(
        state = state,
        onBackPress = onBackPress,
        onSelectSkin = viewModel::updateSelectedSkin
    )
}

@Composable
fun SkinFullscreenScreen(
    state: UiState<out SkinFullscreenViewModel.SkinFullscreenUiState>,
    onSelectSkin: (Int) -> Unit = {},
    onBackPress: () -> Unit = {}
) {
    BaseScreen(
        state = state,
        topBar = {
            if (state is UiState.Success<SkinFullscreenViewModel.SkinFullscreenUiState>) {
                SkinFullscreenTopAppBar(
                    skin = state.content.skin,
                    onCloseClick = onBackPress
                )
            }
        },
        bottomBar = {
            if (state is UiState.Success<SkinFullscreenViewModel.SkinFullscreenUiState>) {
                BottomPager(
                    pageInfo = PageInfo(
                        state.content.selectedSkinIndex,
                        state.content.skins.size
                    ),
                    columns = 1,
                    title = { stringResource(R.string.select_skin) },
                    itemLabel = { index ->
                        val skin = state.content.skins[index]
                        skin.name
                    },
                    onSelectPage = onSelectSkin
                )
            }
        },
    ) { state, innerPadding ->
        AnimatedContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            targetState = state.skin,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "skin-fullscreen"
        ) { skin ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(
                        interactionSource = null,
                        indication = null,
                        onClick = {
                            onBackPress()
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
                                .padding(innerPadding)
                                .clip(MaterialTheme.shapes.extraLarge),
                            /*   .clickable(onClick = requestClose)*/
                            //.height(400.dp),
                            model = getChampionSkinImage(
                                state.championId,
                                skin.num
                            ),
                            loading = AsyncImageDefaults.LoadingCircleProgress,
                        )
                    }
                )
            }

        }
    }
}

@Composable
fun SkinFullscreenTopAppBar(
    skin: Skin,
    onCloseClick: () -> Unit,
) {
    TransparentTopAppBar(
        title = {
            TooltipBox(
                positionProvider = TooltipDefaults.rememberTooltipPositionProvider(
                    TooltipAnchorPosition.Below
                ),
                tooltip = { PlainTooltip { Text(skin.name) } },
                state = rememberTooltipState()
            ) {
                Column {
                    Text(
                        skin.name,
                        style = MaterialTheme.typography.titleLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        stringResource(R.string.skin),
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        },
        navigationIcon = {
            IconButton(onClick = onCloseClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBackIos,
                    contentDescription = "Close"
                )
            }
        }
    )
}

@PreviewWrapper(IchigoPreviewWrapper::class)
@IchigoPreview
@Composable
fun SkinFullscreenPreview() {
    SkinFullscreenScreen(
        state = SkinFullscreenViewModel.SkinFullscreenUiState(
            championId = "1",
            skin = Skin(id = "1", num = 1, name = "Aatrox", chromas = false),
            skins = listOf(
                Skin(id = "1", num = 1, name = "Aatrox", chromas = false),
                Skin(id = "2", num = 2, name = "Aatrox", chromas = false),
                Skin(id = "3", num = 3, name = "Aatrox", chromas = false),
            ),
            selectedSkinIndex = 0,
            version = "1.0.0"
        ).toSuccessUiState()
    )
}