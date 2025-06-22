package com.nei.ichigo.feature.encyclopedia.spells

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
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
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nei.ichigo.R
import com.nei.ichigo.common.BaseScreen
import com.nei.ichigo.common.BaseTopAppBar
import com.nei.ichigo.common.UiState
import com.nei.ichigo.core.designsystem.component.AsyncImagePreviewProvider
import com.nei.ichigo.core.designsystem.component.DEFAULT_ITEM_PADDING
import com.nei.ichigo.core.designsystem.component.DEFAULT_ITEM_SIZE
import com.nei.ichigo.core.designsystem.component.IchigoItemImage
import com.nei.ichigo.core.designsystem.component.IchigoItemLabel
import com.nei.ichigo.core.designsystem.utils.getSpellImage
import com.nei.ichigo.core.model.Spell

@Composable
fun SpellsScreen() {
    val viewModel = hiltViewModel<SpellsViewModel>()
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    SpellsScreen(
        state = state,
    )
}

@Composable
private fun SpellsScreen(state: UiState<out SpellsViewModel.SpellsUiState>) {
    BaseScreen(
        state = state,
        topBar = { BaseTopAppBar(state, R.string.spells) },
    ) { state, innerPadding ->
        SpellsContent(state = state, innerPadding = innerPadding)
    }
}

val EXTRA_WIDTH = 4.dp
val ITEM_SHAPE = RectangleShape
val GRID_MIN_SIZE = DEFAULT_ITEM_SIZE + (DEFAULT_ITEM_PADDING * 2) + EXTRA_WIDTH

@Composable
private fun SpellsContent(
    state: SpellsViewModel.SpellsUiState,
    innerPadding: PaddingValues
) {
    val layoutDirection = LocalLayoutDirection.current
    val contentPadding = PaddingValues(
        top = innerPadding.calculateTopPadding(),
        bottom = innerPadding.calculateBottomPadding() + 8.dp,
        start = innerPadding.calculateStartPadding(layoutDirection) + 32.dp,
        end = innerPadding.calculateEndPadding(layoutDirection) + 32.dp
    )

    val lazyGridState = rememberLazyGridState()
    var currentItemId by rememberSaveable { mutableStateOf<String?>(null) }

    LazyVerticalGrid(
        modifier = Modifier,
        columns = GridCells.Adaptive(minSize = GRID_MIN_SIZE),
        state = lazyGridState,
        horizontalArrangement = Arrangement.SpaceAround,
        contentPadding = contentPadding,
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

@Preview
@Composable
private fun SpellsScreenPreview() {
    AsyncImagePreviewProvider {
        SpellsScreen(
            state = UiState.Success(
                SpellsViewModel.SpellsUiState(
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
                    lang = "en_US",
                )
            )
        )
    }
}