package com.nei.ichigo.feature.encyclopedia.items

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
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
import com.nei.ichigo.core.designsystem.component.IchigoItemImage
import com.nei.ichigo.core.designsystem.component.IchigoItemLabel
import com.nei.ichigo.core.designsystem.component.LoadingScreen
import com.nei.ichigo.core.designsystem.component.TransparentTopAppBar
import com.nei.ichigo.core.designsystem.component.appendTitle
import com.nei.ichigo.core.designsystem.component.appendVersion
import com.nei.ichigo.core.designsystem.utils.getItemImage
import com.nei.ichigo.feature.encyclopedia.items.ItemsViewModel.ItemsUiState
import com.nei.ichigo.feature.encyclopedia.items.ItemsViewModel.ItemsUiState.Success.ItemUi

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

val EXTRA_WIDTH = 16.dp
val ITEM_SHAPE = RectangleShape
val GRID_MIN_SIZE = DEFAULT_ITEM_SIZE + (DEFAULT_ITEM_PADDING * 2) + EXTRA_WIDTH

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
                Item(
                    item = item,
                    version = state.version,
                    itemsMap = state.itemsMap,
                    currentItemId = currentItemId,
                    onClick = { currentItemId = it },
                    onLongClick = { currentItemId = it },
                    onDismissRequest = { currentItemId = null },
                    scrollToItem = scrollToItem@{ it ->
                        val index = state.itemsOrder.indexOf(it)
                        if (index == -1) return@scrollToItem
                        currentItemId = it
                        lazyGridState.requestScrollToItem(index)
                    }
                )
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

@Composable
fun Item(
    item: ItemUi,
    version: String,
    itemsMap: Map<String, ItemUi>,
    currentItemId: String?,
    onDismissRequest: () -> Unit,
    onClick: (String) -> Unit,
    scrollToItem: (String) -> Unit,
    onLongClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(DEFAULT_ITEM_PADDING),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IchigoItemImage(
            model = getItemImage(item.image, version),
            modifier = Modifier.combinedClickable(
                onClick = { onClick(item.id) },
                onLongClick = { onLongClick(item.id) }
            ),
            shape = ITEM_SHAPE,
        )
        IchigoItemLabel(
            text = item.name,
        )
        ItemPopUp(
            item = item,
            itemsMap = itemsMap,
            version = version,
            currentItemId = currentItemId,
            onDismissRequest = onDismissRequest,
            scrollToItem = scrollToItem
        )
    }
}

fun genItemPreview(
    id: String,
    from: List<String> = emptyList(),
    into: List<String> = emptyList()
) = ItemUi(
    id = id,
    name = "Item $id",
    image = "",
    from = from,
    into = into,
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