package com.nei.ichigo.feature.encyclopedia.champions

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nei.ichigo.R
import com.nei.ichigo.common.BaseScreen
import com.nei.ichigo.common.BaseTopAppBar
import com.nei.ichigo.common.UiState
import com.nei.ichigo.common.layout.BaseShimmer
import com.nei.ichigo.common.layout.Grid
import com.nei.ichigo.core.designsystem.component.DEFAULT_ITEM_PADDING
import com.nei.ichigo.core.designsystem.component.DEFAULT_ITEM_SIZE
import com.nei.ichigo.core.designsystem.component.IchigoItemImage
import com.nei.ichigo.core.designsystem.component.IchigoItemLabel
import com.nei.ichigo.core.designsystem.component.IchigoItemShimmerImage
import com.nei.ichigo.core.designsystem.component.IchigoItemShimmerLabel
import com.nei.ichigo.core.designsystem.component.ShimmerScope
import com.nei.ichigo.core.designsystem.theme.IchigoThemePreview
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
    state: UiState<out ChampionsUiState>,
    onTagSelected: (String?) -> Unit = {},
    onChampionClick: (String) -> Unit = {}
) {
    BaseScreen(
        state = state,
        topBar = {
            ChampionsTopAppBar(
                state = state,
                onTagSelected = onTagSelected
            )
        },
        shimmerContent = { innerPadding -> ChampionsShimmer(innerPadding = innerPadding) }
    ) { state, innerPadding ->
        ChampionsSuccess(
            champions = state.champions,
            version = state.version,
            innerPadding = innerPadding,
            onChampionClick = onChampionClick
        )
    }
}

@Composable
private fun ChampionsTopAppBar(
    state: UiState<out ChampionsUiState>,
    onTagSelected: (String?) -> Unit
) {
    BaseTopAppBar(state, R.string.champions) {
        if (state is UiState.Success) {
            var openFilterDialog by rememberSaveable { mutableStateOf(false) }
            IconButton(onClick = { openFilterDialog = true }) {
                BadgedBox(
                    badge = {
                        if (state.content.tagSelected != null) {
                            Badge()
                        }
                    }
                ) {
                    Icon(Icons.Rounded.FilterList, contentDescription = null)
                }
            }

            if (openFilterDialog) {
                ChampionsFilterDialog(
                    currentTagSelected = state.content.tagSelected,
                    champions = state.content.tags,
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
    Grid(
        minSize = GRID_MIN_SIZE,
        innerPadding = innerPadding,
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

@Composable
private fun ShimmerScope.ChampionsShimmer(
    innerPadding: PaddingValues,
) {
    BaseShimmer(
        minSize = GRID_MIN_SIZE,
        idPlural = R.plurals.number_of_champions,
        innerPadding = innerPadding,
        itemContent = { ChampionSkeletonItem() }
    )
}

@Composable
fun ShimmerScope.ChampionSkeletonItem() {
    Column(
        modifier = Modifier
            .padding(DEFAULT_ITEM_PADDING),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IchigoItemShimmerImage()
        IchigoItemShimmerLabel(text = "      ")
    }
}

@PreviewLightDark
@Composable
fun ChampionsScreenPreview() {
    IchigoThemePreview {
        ChampionsScreen(
            state = UiState.Success(
                ChampionsUiState(
                    version = "1.0.0",
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
        )
    }
}

@PreviewLightDark
@Composable
private fun ChampionItemShimmerPreview() {
    IchigoThemePreview {
        ChampionsScreen(
            state = UiState.Loading
        )
    }
}
