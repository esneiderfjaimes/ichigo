@file:OptIn(
    ExperimentalMaterial3Api::class, ExperimentalCoilApi::class
)

package com.nei.ichigo.feature.encyclopedia.champions

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.ColorImage
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.AsyncImagePreviewHandler
import coil3.compose.LocalAsyncImagePreviewHandler
import coil3.compose.SubcomposeAsyncImage
import com.nei.ichigo.core.model.Champion
import com.nei.ichigo.feature.encyclopedia.champions.ChampionsViewModel.ChampionsUiState

@Composable
fun ChampionsScreen(viewModel: ChampionsViewModel) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    ChampionsScreen(state = state)
}

@Composable
private fun ChampionsScreen(state: ChampionsUiState) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            Surface(
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append("Encyclopedia")
                        }
                        if (state is ChampionsUiState.Success) {
                            val latestVersion = state.version
                            if (latestVersion.isNotBlank()) withStyle(
                                style = SpanStyle(
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                    fontSize = MaterialTheme.typography.titleSmall.fontSize
                                )
                            ) {
                                append(" v$latestVersion")
                            }
                        }
                    },
                    modifier = Modifier
                        .padding(horizontal = 32.dp, vertical = 16.dp)
                        .statusBarsPadding(),
                    style = MaterialTheme.typography.headlineSmall,
                )
            }
        },
        contentWindowInsets = WindowInsets.safeDrawing
    ) { innerPadding ->
        when (state) {
            ChampionsUiState.Error -> {
                Text(text = "Error")
            }

            ChampionsUiState.Loading -> {
                Text(text = "Loading")
            }

            is ChampionsUiState.Success -> {
                ChampionsSuccess(
                    champions = state.champions,
                    innerPadding = innerPadding
                )
            }
        }
    }
}

private val ITEM_SIZE = 75.dp
private val ITEM_SPADING = 4.dp

@Composable
fun ChampionsSuccess(
    champions: List<Champion>,
    innerPadding: PaddingValues
) {
    val layoutDirection = LocalLayoutDirection.current
    val contentPadding = PaddingValues(
        top = innerPadding.calculateTopPadding(),
        bottom = innerPadding.calculateBottomPadding() + 8.dp,
        start = innerPadding.calculateStartPadding(layoutDirection) + 32.dp,
        end = innerPadding.calculateEndPadding(layoutDirection) + 32.dp
    )
    LazyVerticalGrid(
        modifier = Modifier,
        columns = GridCells.Adaptive(minSize = ITEM_SIZE + (ITEM_SPADING)),
        horizontalArrangement = Arrangement.SpaceAround,
        contentPadding = contentPadding,
        content = {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Text(
                    text = "${champions.size} champions.",
                    modifier = Modifier
                        .padding(8.dp),
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                )
            }
            items(champions, key = { it.id }) { champion ->
                ChampionItem(champion)
            }
        }
    )
}

private val BORDER_SIZE = 0.75.dp

@Composable
fun ChampionItem(champion: Champion) {
    Column(
        modifier = Modifier
            .padding(ITEM_SPADING),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SubcomposeAsyncImage(
            model = champion.image,
            contentDescription = null,
            modifier = Modifier
                .size(ITEM_SIZE)
                .clip(RoundedCornerShape(25))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .border(
                    width = BORDER_SIZE,
                    color = Color(0xFFC28F2C),
                    shape = RoundedCornerShape(25)
                )
                .padding(BORDER_SIZE),
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
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(24.dp)
                    )
                }
            }
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = champion.name,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
fun ChampionsScreenPreview() {
    val previewHandler = AsyncImagePreviewHandler {
        ColorImage(Color.Transparent.toArgb())
    }

    CompositionLocalProvider(LocalAsyncImagePreviewHandler provides previewHandler) {
        ChampionsScreen(
            state = ChampionsUiState.Success(
                version = "1.0.0",
                lang = "en_US",
                listOf(
                    Champion(
                        id = "1",
                        name = "Aatrox",
                        image = "aatrox.png",
                        tags = emptyList()
                    ),
                    Champion(
                        id = "2",
                        name = "Aatrox",
                        image = "aatrox.png",
                        tags = emptyList()
                    ),
                )
            )
        )
    }
}