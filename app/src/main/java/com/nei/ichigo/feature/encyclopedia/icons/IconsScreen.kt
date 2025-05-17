@file:OptIn(
    ExperimentalSharedTransitionApi::class,
    ExperimentalCoilApi::class
)

package com.nei.ichigo.feature.encyclopedia.icons

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.ColorImage
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.AsyncImagePreviewHandler
import coil3.compose.LocalAsyncImagePreviewHandler
import coil3.compose.SubcomposeAsyncImage
import com.nei.ichigo.R
import com.nei.ichigo.core.designsystem.component.ErrorScreen
import com.nei.ichigo.core.designsystem.component.LoadingScreen
import com.nei.ichigo.core.designsystem.component.TransparentTopAppBar
import com.nei.ichigo.core.designsystem.utils.getProfileIconImage
import com.nei.ichigo.core.model.ProfileIcon
import com.nei.ichigo.feature.encyclopedia.icons.IconsViewModel.IconsUiState

@Composable
fun IconsScreen() {
    val viewModel: IconsViewModel = hiltViewModel()
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    IconsScreen(state = state)
}

@Composable
private fun IconsScreen(state: IconsUiState) {
    Scaffold(
        topBar = {
            IconsTopAppBar(state)
        }
    ) { innerPadding ->
        when (state) {
            IconsUiState.Error -> {
                ErrorScreen()
            }

            IconsUiState.Loading -> {
                LoadingScreen()
            }

            is IconsUiState.Success -> {
                SuccessContent(state, innerPadding)
            }
        }
    }
}

@Composable
private fun IconsTopAppBar(
    state: IconsUiState
) {
    TransparentTopAppBar(text = buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        ) {
            append(stringResource(R.string.icons))
        }
        if (state is IconsUiState.Success) {
            if (state.version.isNotBlank()) withStyle(
                style = SpanStyle(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    fontSize = MaterialTheme.typography.titleSmall.fontSize
                )
            ) {
                append(" v${state.version}")
            }
        }
    })
}

private val BORDER_SIZE = 0.75.dp

private val ITEM_SIZE = 70.dp
private val ITEM_SPADING = 4.dp
private val ITEM_SHAPE = RoundedCornerShape(25)

private val GRID_MIN_SIZE = ITEM_SIZE + (ITEM_SPADING * 2) + (BORDER_SIZE * 2)

@Composable
fun SuccessContent(state: IconsUiState.Success, innerPadding: PaddingValues) {
    val layoutDirection = LocalLayoutDirection.current
    val contentPadding = PaddingValues(
        top = innerPadding.calculateTopPadding(),
        bottom = innerPadding.calculateBottomPadding() + 8.dp,
        start = innerPadding.calculateStartPadding(layoutDirection) + 32.dp,
        end = innerPadding.calculateEndPadding(layoutDirection) + 32.dp
    )

    var selectedProfileIcon by remember { mutableStateOf<ProfileIcon?>(null) }

    SharedTransitionLayout(modifier = Modifier.fillMaxSize()) {
        LazyVerticalGrid(
            modifier = Modifier,
            columns = GridCells.Adaptive(minSize = GRID_MIN_SIZE),
            horizontalArrangement = Arrangement.SpaceAround,
            contentPadding = contentPadding,
            content = {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Text(
                        text = pluralStringResource(
                            id = R.plurals.number_of_icons,
                            count = state.icons.size,
                            state.icons.size
                        ),
                        modifier = Modifier
                            .padding(8.dp),
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center,
                    )
                }

                items(state.icons) { icon ->
                    AnimatedVisibility(
                        visible = icon.id != selectedProfileIcon?.id,
                        modifier = Modifier.animateItem()
                    ) {
                        ProfileIconItem(
                            icon = icon,
                            size = ITEM_SIZE,
                            version = state.version
                        ) {
                            selectedProfileIcon = icon
                        }
                    }
                }
            }
        )

        AnimatedContent(
            targetState = selectedProfileIcon,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "SnackEditDetails"
        ) { icon ->
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if (icon != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable(
                                interactionSource = null,
                                indication = null,
                                onClick = { selectedProfileIcon = null }
                            )
                            .background(Color.Black.copy(alpha = 0.5f))
                    )

                    ProfileIconItem(
                        icon = icon,
                        version = state.version,
                        size = ITEM_SIZE * 2,
                        onClick = { selectedProfileIcon = null }
                    )

                    BackHandler {
                        selectedProfileIcon = null
                    }
                }
            }
        }
    }
}

context(SharedTransitionScope, AnimatedVisibilityScope)
@Composable
fun ProfileIconItem(
    icon: ProfileIcon,
    size: Dp,
    version: String,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .padding(ITEM_SPADING)
            .sharedBounds(
                sharedContentState = rememberSharedContentState(key = "${icon.id}-bounds"),
                animatedVisibilityScope = this@AnimatedVisibilityScope,
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SubcomposeAsyncImage(
            model = getProfileIconImage(icon.image, version),
            contentDescription = null,
            modifier = Modifier
                .clip(ITEM_SHAPE)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .border(
                    width = BORDER_SIZE,
                    color = Color(0xFFC28F2C),
                    shape = ITEM_SHAPE
                )
                .padding(BORDER_SIZE)
                .size(size)
                .clickable(onClick = onClick)
                .sharedBounds(
                    sharedContentState = rememberSharedContentState(key = icon.id),
                    animatedVisibilityScope = this@AnimatedVisibilityScope,
                    clipInOverlayDuringTransition = OverlayClip(ITEM_SHAPE)
                ),
            onError = {
                it.result.throwable.printStackTrace()
            },
            error = {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Rounded.Warning,
                        contentDescription = null,
                    )
                }
            },
            loading = {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(Modifier.size(24.dp))
                }
            }
        )
        Text(
            text = "#" + icon.id,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background, CircleShape)
                .padding(4.dp)
                .sharedBounds(
                    sharedContentState = rememberSharedContentState(key = "${icon.id}-label"),
                    animatedVisibilityScope = this@AnimatedVisibilityScope,
                )
        )
    }
}

@Preview
@Composable
fun IconsScreenPreview() {
    val previewHandler = AsyncImagePreviewHandler {
        val randomColor = Color(
            red = (0..255).random(),
            green = (0..255).random(),
            blue = (0..255).random(),
            alpha = 255
        )
        ColorImage(randomColor.toArgb())
    }

    CompositionLocalProvider(LocalAsyncImagePreviewHandler provides previewHandler) {
        IconsScreen(
            state = IconsUiState.Success(
                version = "1.0.0",
                lang = "en",
                icons = listOf(
                    ProfileIcon(
                        id = "1",
                        image = "aatrox.png"
                    ),
                    ProfileIcon(
                        id = "2",
                        image = "aatrox.png"
                    ),
                )
            )
        )
    }
}