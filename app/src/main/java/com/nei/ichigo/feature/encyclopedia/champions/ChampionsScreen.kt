package com.nei.ichigo.feature.encyclopedia.champions

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nei.ichigo.R
import com.nei.ichigo.core.designsystem.component.AsyncImagePreviewProvider
import com.nei.ichigo.core.designsystem.component.DEFAULT_ITEM_PADDING
import com.nei.ichigo.core.designsystem.component.DEFAULT_ITEM_SIZE
import com.nei.ichigo.core.designsystem.component.ErrorScreen
import com.nei.ichigo.core.designsystem.component.IchigoItemImage
import com.nei.ichigo.core.designsystem.component.IchigoItemLabel
import com.nei.ichigo.core.designsystem.component.LoadingScreen
import com.nei.ichigo.core.designsystem.component.TransparentTopAppBar
import com.nei.ichigo.core.designsystem.component.appendTitle
import com.nei.ichigo.core.designsystem.component.appendVersion
import com.nei.ichigo.core.designsystem.utils.getChampionImage
import com.nei.ichigo.core.model.Champion
import com.nei.ichigo.feature.encyclopedia.champions.ChampionsViewModel.ChampionsUiState

@Composable
fun ChampionsScreen(
    onChampionClick: (String) -> Unit
) {
    val viewModel: ChampionsViewModel = hiltViewModel()
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    ChampionsScreen(
        state = state,
        onTagSelected = viewModel::onTagSelected,
        onChampionClick = onChampionClick
    )
}

@Composable
private fun ChampionsScreen(
    state: ChampionsUiState,
    onTagSelected: (String?) -> Unit = {},
    onChampionClick: (String) -> Unit = {}
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            ChampionsTopAppBar(
                state = state,
                onTagSelected = onTagSelected
            )
        },
        contentWindowInsets = WindowInsets.safeDrawing
            .only(WindowInsetsSides.Top)
    ) { innerPadding ->
        when (state) {
            ChampionsUiState.Error -> {
                ErrorScreen(Modifier.padding(innerPadding))
            }

            ChampionsUiState.Loading -> {
                LoadingScreen(Modifier.padding(innerPadding))
            }

            is ChampionsUiState.Success -> {
                ChampionsSuccess(
                    champions = state.champions,
                    version = state.version,
                    innerPadding = innerPadding,
                    onChampionClick = onChampionClick
                )
            }
        }
    }
}

@Composable
private fun ChampionsTopAppBar(
    state: ChampionsUiState,
    onTagSelected: (String?) -> Unit
) {
    TransparentTopAppBar(text = buildAnnotatedString {
        appendTitle(stringResource(R.string.champions))
        if (state is ChampionsUiState.Success) {
            appendVersion(state.version)
        }
    }) {
        if (state is ChampionsUiState.Success) {
            var openFilterDialog by rememberSaveable { mutableStateOf(false) }
            IconButton(onClick = { openFilterDialog = true }) {
                BadgedBox(
                    badge = {
                        if (state.tagSelected != null) {
                            Badge()
                        }
                    }
                ) {
                    Icon(Icons.Rounded.FilterList, contentDescription = null)
                }
            }

            if (openFilterDialog) {
                ChampionsFilterDialog(
                    currentTagSelected = state.tagSelected,
                    champions = state.tags,
                    onDismiss = { openFilterDialog = false },
                    onTagSelected = onTagSelected
                )
            }
        }
    }
}

private val GRID_MIN_SIZE = DEFAULT_ITEM_SIZE + (DEFAULT_ITEM_PADDING * 2)

@Composable
private fun ChampionsSuccess(
    champions: List<Champion>,
    version: String,
    innerPadding: PaddingValues,
    onChampionClick: (String) -> Unit
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
        columns = GridCells.Adaptive(minSize = GRID_MIN_SIZE),
        horizontalArrangement = Arrangement.SpaceAround,
        contentPadding = contentPadding,
        content = {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Text(
                    text = pluralStringResource(
                        id = R.plurals.number_of_champions,
                        count = champions.size,
                        champions.size
                    ),
                    modifier = Modifier
                        .padding(8.dp),
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                )
            }
            items(champions, key = { it.id }, contentType = { it }) { champion ->
                ChampionItem(champion, version, Modifier.animateItem(), onChampionClick)
            }
        }
    )
}

@Composable
fun ChampionItem(
    champion: Champion,
    version: String,
    modifier: Modifier = Modifier,
    onChampionClick: (String) -> Unit
) {
    Column(
        modifier = modifier
            .padding(DEFAULT_ITEM_PADDING),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IchigoItemImage(
            model = getChampionImage(champion.image, version),
            modifier = Modifier
                .clickable {
                    onChampionClick(champion.id)
                },
        )
        IchigoItemLabel(
            text = champion.name,
        )
    }
}

@Preview
@Composable
fun ChampionsScreenPreview() {
    AsyncImagePreviewProvider {
        ChampionsScreen(
            state = ChampionsUiState.Success(
                version = "1.0.0",
                lang = "en_US",
                champions = (1..100).map {
                    Champion(
                        id = it.toString(),
                        name = "Champ $it",
                        image = "",
                        tags = emptyList()
                    )
                },
                tags = emptyList()
            )
        )
    }
}

@Preview
@Composable
fun ChampionsScreenErrorPreview() {
    ChampionsScreen(state = ChampionsUiState.Error)
}

@Preview
@Composable
fun ChampionsScreenLoadingPreview() {
    ChampionsScreen(state = ChampionsUiState.Loading)
}