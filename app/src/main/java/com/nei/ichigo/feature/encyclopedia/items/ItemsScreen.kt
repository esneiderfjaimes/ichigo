package com.nei.ichigo.feature.encyclopedia.items

import androidx.compose.animation.AnimatedContent
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
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import com.nei.ichigo.core.designsystem.component.AsyncImage
import com.nei.ichigo.core.designsystem.component.AsyncImagePreviewProvider
import com.nei.ichigo.core.designsystem.component.LoadingScreen
import com.nei.ichigo.core.designsystem.component.TransparentTopAppBar
import com.nei.ichigo.core.designsystem.component.appendTitle
import com.nei.ichigo.core.designsystem.component.appendVersion
import com.nei.ichigo.core.designsystem.theme.Gold
import com.nei.ichigo.core.designsystem.utils.getItemImage
import com.nei.ichigo.core.model.Gold
import com.nei.ichigo.core.model.Item
import com.nei.ichigo.feature.encyclopedia.items.ItemsViewModel.ItemsUiState
import kotlinx.coroutines.launch

@Composable
fun ItemsScreen() {
    val viewModel = hiltViewModel<ItemsViewModel>()
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    ItemsScreen(
        state = state,
    )
}

@Composable
private fun ItemsScreen(state: ItemsUiState) {
    Scaffold(
        topBar = { ItemsTopAppBar(state) }
    ) { innerPadding ->
        when (state) {
            ItemsUiState.Loading -> {
                LoadingScreen(Modifier.padding(innerPadding))
            }

            is ItemsUiState.Success -> {
                SuccessScreen(state, innerPadding)
            }
        }
    }
}

@Composable
private fun ItemsTopAppBar(
    state: ItemsUiState,
) {
    TransparentTopAppBar(text = buildAnnotatedString {
        appendTitle(stringResource(R.string.items))
        if (state is ItemsUiState.Success) {
            appendVersion(state.version)
        }
    })
}

val BORDER_SIZE = 0.75.dp
val ITEM_SIZE = 70.dp
val IMAGE_PADDING = 8.dp
val ITEM_SPADING = 4.dp
val ITEM_SHAPE = RoundedCornerShape(25)
val GRID_MIN_SIZE = ITEM_SIZE + (ITEM_SPADING * 2) + (BORDER_SIZE * 2) + (IMAGE_PADDING * 2)

@Composable
private fun SuccessScreen(state: ItemsUiState.Success, innerPadding: PaddingValues) {
    val layoutDirection = LocalLayoutDirection.current
    val contentPadding = PaddingValues(
        top = innerPadding.calculateTopPadding(),
        bottom = innerPadding.calculateBottomPadding() + 8.dp,
        start = innerPadding.calculateStartPadding(layoutDirection) + 32.dp,
        end = innerPadding.calculateEndPadding(layoutDirection) + 32.dp
    )

    val lazyGridState = rememberLazyGridState()
    val scope = rememberCoroutineScope()
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
                        id = R.plurals.number_of_items,
                        count = state.itemsOrder.size,
                        state.itemsOrder.size
                    ),
                    modifier = Modifier
                        .padding(8.dp),
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                )
            }

            items(
                items = state.itemsOrder,
                key = { it },
                contentType = { it }
            ) { id ->
                val item = state.itemsMap[id] ?: return@items
                Column(
                    modifier = Modifier
                        .padding(ITEM_SPADING),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AsyncImage(
                        model = getItemImage(item.image, state.version),
                        modifier = Modifier
                            .clip(ITEM_SHAPE)
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .border(
                                width = BORDER_SIZE,
                                color = Gold,
                                shape = ITEM_SHAPE
                            )
                            .padding(BORDER_SIZE)
                            .size(ITEM_SIZE)
                            .clickable { currentItemId = item.id },
                    )
                    Text(
                        item.name,
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center
                    )
                    DropdownMenu(
                        expanded = currentItemId == item.id,
                        onDismissRequest = { currentItemId = null },
                        shape = RoundedCornerShape(8.dp),
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .padding(8.dp)
                        ) {
                            ItemPossibility(item, state.itemsMap, state.version) { id ->
                                if (id == item.id) return@ItemPossibility
                                scope.launch {
                                    val index = state.itemsOrder.indexOf(id)
                                    lazyGridState.animateScrollToItem(index)
                                    currentItemId = id
                                }
                            }

                            Text(
                                text = item.name,
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(4.dp)
                            )

                            if (item.description.isNotBlank()) {
                                Text(
                                    item.description,
                                    style = MaterialTheme.typography.bodySmall,
                                    textAlign = TextAlign.Center
                                )
                            }

                            ItemRecipeTree(item, state.itemsMap, state.version) { id ->
                                if (id == item.id) return@ItemRecipeTree
                                scope.launch {
                                    val index = state.itemsOrder.indexOf(id)
                                    lazyGridState.animateScrollToItem(index)
                                    currentItemId = id
                                }
                            }
                        }
                    }
                }
            }
        }
    )

    AnimatedContent(
        targetState = currentItemId,
        transitionSpec = { fadeIn() togetherWith fadeOut() },
        label = "ItemDetails",
    ) {
        Box(
            Modifier.fillMaxSize()
        ) {
            if (it != null) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f))
                )
            }
        }
    }
}

fun genItemPreview(id: String, from: List<String> = emptyList()) = Item(
    id = id,
    name = "Item $id",
    plaintext = "Item $id",
    description = "Item $id",
    image = "",
    from = from,
    into = emptyList(),
    maps = emptySet(),
    gold = Gold(
        base = 0,
        purchasable = false,
        total = 0,
        sell = 0
    )
)

@Preview
@Composable
fun ItemsScreenPreview() {
    AsyncImagePreviewProvider {
        ItemsScreen(
            state = ItemsUiState.Success(
                itemsOrder = List(15) { it.toString() },
                itemsMap = List(15) { index ->
                    genItemPreview(
                        index.toString(),
                        listOf("1", "2", "3"),
                    )
                }.associateBy { it.id },
                version = "1.0.0",
                lang = "en",
            )
        )
    }
}