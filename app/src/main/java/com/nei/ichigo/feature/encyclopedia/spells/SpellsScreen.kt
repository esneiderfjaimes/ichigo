package com.nei.ichigo.feature.encyclopedia.spells

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nei.ichigo.R
import com.nei.ichigo.common.BaseScreen
import com.nei.ichigo.common.BaseTopAppBar
import com.nei.ichigo.common.UiState
import com.nei.ichigo.common.layout.BaseShimmer
import com.nei.ichigo.common.layout.Grid
import com.nei.ichigo.common.onSuccess
import com.nei.ichigo.core.designsystem.component.DEFAULT_ITEM_PADDING
import com.nei.ichigo.core.designsystem.component.DEFAULT_ITEM_SIZE
import com.nei.ichigo.core.designsystem.component.IchigoItemImage
import com.nei.ichigo.core.designsystem.component.IchigoItemLabel
import com.nei.ichigo.core.designsystem.component.IchigoItemShimmerImage
import com.nei.ichigo.core.designsystem.component.IchigoItemShimmerLabel
import com.nei.ichigo.core.designsystem.component.ShimmerScope
import com.nei.ichigo.core.designsystem.theme.IchigoThemePreview
import com.nei.ichigo.core.designsystem.utils.getSpellImage
import com.nei.ichigo.core.model.Spell
import com.nei.ichigo.feature.encyclopedia.spells.SpellsViewModel.SpellsUiState

@Composable
fun SpellsScreen() {
    val viewModel = hiltViewModel<SpellsViewModel>()
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    SpellsScreen(
        state = state,
        onTagSelected = viewModel::onTagSelected
    )
}

@Composable
private fun SpellsScreen(
    state: UiState<out SpellsUiState>,
    onTagSelected: (String?) -> Unit = {},
) {
    BaseScreen(
        state = state,
        topBar = { SpellsTopAppBar(state, onTagSelected) },
        shimmerContent = { innerPadding -> SpellsShimmer(innerPadding) }
    ) { state, innerPadding ->
        SpellsContent(state = state, innerPadding = innerPadding)
    }
}

@Composable
fun SpellsTopAppBar(
    uiState: UiState<out SpellsUiState>,
    onTagSelected: (String?) -> Unit = {},
) {
    BaseTopAppBar(uiState, R.string.spells, actions = {
        uiState.onSuccess { state ->
            var openFilterDialog by rememberSaveable { mutableStateOf(false) }
            IconButton(onClick = { openFilterDialog = true }) {
                BadgedBox(
                    badge = {
                        if (state.filteredModes.isNotEmpty()) {
                            Badge()
                        }
                    }
                ) {
                    Icon(Icons.Rounded.FilterList, contentDescription = null)
                }
            }

            if (openFilterDialog) {
                SpellsFilterDialog(
                    modesSelected = state.filteredModes,
                    modes = state.modesAvailable,
                    onDismiss = { openFilterDialog = false },
                    onTagSelected = onTagSelected
                )
            }
        }
    })
}

val EXTRA_WIDTH = 4.dp
val ITEM_SHAPE = RectangleShape
val GRID_MIN_SIZE = DEFAULT_ITEM_SIZE + (DEFAULT_ITEM_PADDING * 2) + EXTRA_WIDTH

@Composable
private fun SpellsContent(
    state: SpellsUiState,
    innerPadding: PaddingValues,
) {
    val lazyGridState = rememberLazyGridState()
    var currentItemId by rememberSaveable { mutableStateOf<String?>(null) }

    Grid(
        minSize = GRID_MIN_SIZE,
        state = lazyGridState,
        innerPadding = innerPadding,
        content = {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Text(
                    text = pluralStringResource(
                        id = R.plurals.number_of_spells,
                        count = state.spells.size,
                        state.spells.size
                    ),
                    modifier = Modifier
                        .padding(8.dp),
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                )
            }

            items(
                items = state.spells,
                key = { it.id },
                contentType = { it }
            ) { spell ->
                ItemSpell(
                    item = spell,
                    currentItemId = currentItemId,
                    version = state.version,
                    onClick = { currentItemId = it },
                    onDismissRequest = { currentItemId = null },
                )
            }
        }
    )
}

@Composable
private fun ShimmerScope.SpellsShimmer(innerPadding: PaddingValues) {
    BaseShimmer(
        minSize = GRID_MIN_SIZE,
        sizeItems = 11,
        idPlural = R.plurals.number_of_spells,
        innerPadding = innerPadding,
        itemContent = { ItemSpellShimmer() }
    )
}

@Composable
fun ItemSpell(
    item: Spell,
    currentItemId: String?,
    version: String,
    onClick: (String) -> Unit,
    onDismissRequest: () -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(DEFAULT_ITEM_PADDING),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IchigoItemImage(
            model = getSpellImage(item.image, version),
            modifier = Modifier.clickable {
                onClick(item.id)
            },
            shape = ITEM_SHAPE,
        )
        IchigoItemLabel(
            text = item.name,
        )
        SpellPopUp(
            currentItemId = currentItemId,
            item = item,
            onDismissRequest = onDismissRequest
        )
    }
}

@Composable
fun ShimmerScope.ItemSpellShimmer() {
    Column(
        modifier = Modifier
            .padding(DEFAULT_ITEM_PADDING),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IchigoItemShimmerImage(shape = ITEM_SHAPE)
        IchigoItemShimmerLabel(text = "      ")
    }
}

@PreviewLightDark
@Composable
private fun SpellsScreenPreview() {
    IchigoThemePreview {
        SpellsScreen(
            state = UiState.Success(
                SpellsUiState(
                    spells = List(10) {
                        Spell(
                            id = "$it",
                            name = "Spell $it",
                            description = "",
                            tooltip = "",
                            image = "",
                            summonerLevel = 0,
                            cooldown = 0.0,
                            modes = emptyList()
                        )
                    },
                    version = "1.0",
                    filteredModes = emptySet(),
                    modesAvailable = emptyList(),
                )
            )
        )
    }
}

@PreviewLightDark
@Composable
private fun SpellsScreenShimmerPreview() {
    IchigoThemePreview {
        SpellsScreen(
            state = UiState.Loading
        )
    }
}