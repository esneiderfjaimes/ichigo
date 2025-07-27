package com.nei.ichigo.feature.encyclopedia.icons

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nei.ichigo.R
import com.nei.ichigo.common.BaseScreen
import com.nei.ichigo.common.BaseTopAppBar
import com.nei.ichigo.common.UiState
import com.nei.ichigo.core.designsystem.component.AsyncImagePreviewProvider
import com.nei.ichigo.core.designsystem.component.BottomPager
import com.nei.ichigo.core.designsystem.component.DEFAULT_ITEM_PADDING
import com.nei.ichigo.core.designsystem.component.DEFAULT_ITEM_SHAPE
import com.nei.ichigo.core.designsystem.component.DEFAULT_ITEM_SIZE
import com.nei.ichigo.core.designsystem.component.IchigoItemImage
import com.nei.ichigo.core.designsystem.component.IchigoItemLabel
import com.nei.ichigo.core.designsystem.component.PageInfo
import com.nei.ichigo.core.designsystem.utils.getProfileIconImage
import com.nei.ichigo.feature.encyclopedia.icons.IconsViewModel.IconsUiState

@Composable
fun IconsScreen() {
    val viewModel: IconsViewModel = hiltViewModel()
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    IconsScreen(
        state = state,
        onSelectPage = viewModel::onSelectPage,
        onPageSizeChange = viewModel::onPageSizeChange
    )
}

@Composable
private fun IconsScreen(
    state: UiState<out IconsUiState>,
    onSelectPage: (Int?) -> Unit = {},
    onPageSizeChange: (Int) -> Unit = {},
) {
    SharedTransitionLayout(modifier = Modifier.fillMaxSize()) {
        var selectedProfileIcon by rememberSaveable(stateSaver = IconUi.Saver) {
            mutableStateOf(null)
        }

        BaseScreen(
            state = state,
            topBar = { IconsTopAppBar(state, onSelectPage, onPageSizeChange) },
            bottomBar = {
                if (state is UiState.Success) {
                    state.content.pageInfo?.let { pageInfo ->
                        BottomPager(pageInfo) {
                            onSelectPage(it)
                        }
                    }
                }
            }
        ) { state, innerPadding ->
            SuccessContent(
                innerPadding = innerPadding,
                icons = state.icons,
                total = state.totalIcons,
                version = state.version,
                selectedProfileIcon = selectedProfileIcon,
                onSelect = { selectedProfileIcon = it }
            )
        }

        if (state is UiState.Success) {
            IconDetails(
                selectedProfileIcon = selectedProfileIcon,
                version = state.content.version,
                requestClose = { selectedProfileIcon = null }
            )
        }
    }
}

@Composable
private fun IconsTopAppBar(
    uiState: UiState<out IconsUiState>,
    onSelectPage: (Int?) -> Unit = {},
    onPageSizeChange: (Int) -> Unit = {},
) {
    BaseTopAppBar(uiState, R.string.icons) {
        if (uiState is UiState.Success) {
            val state = uiState.content
            var showMenu by remember { mutableStateOf(false) }
            IconButton(onClick = { showMenu = true }) {
                Icon(Icons.Rounded.MoreVert, contentDescription = null)
                DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                    fun switchPagination() {
                        showMenu = false
                        if (state.pageInfo == null) {
                            onSelectPage(0)
                        } else {
                            onSelectPage(null)
                        }
                    }

                    DropdownMenuItem(
                        onClick = {
                            switchPagination()
                        },
                        text = {
                            Text(text = stringResource(R.string.paginate_items))
                        },
                        trailingIcon = {
                            Switch(
                                checked = state.pageInfo != null,
                                onCheckedChange = {
                                    switchPagination()
                                }
                            )
                        }
                    )

                    if (state.pageInfo != null) {
                        HorizontalDivider()
                        Text(
                            stringResource(R.string.icons_per_page),
                            modifier = Modifier.padding(12.dp),
                            style = MaterialTheme.typography.titleSmall
                                .copy(fontWeight = FontWeight.Bold)
                        )

                        IconsUiState.PAGE_SIZES.forEach { pageSize ->
                            DropdownMenuItem(
                                onClick = {
                                    showMenu = false
                                    onPageSizeChange(pageSize)
                                },
                                text = {
                                    Text(
                                        text = pluralStringResource(
                                            id = R.plurals.number_of_icons,
                                            count = pageSize,
                                            pageSize
                                        )
                                    )
                                },
                                trailingIcon = {
                                    if (state.pageSize == pageSize) {
                                        Icon(Icons.Rounded.Check, contentDescription = null)
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

private val GRID_MIN_SIZE = DEFAULT_ITEM_SIZE + (DEFAULT_ITEM_PADDING * 2)

@Composable
private fun SharedTransitionScope.SuccessContent(
    innerPadding: PaddingValues,
    icons: List<IconUi>,
    total: Int,
    version: String,
    selectedProfileIcon: IconUi? = null,
    onSelect: (IconUi) -> Unit = {},
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
                        id = R.plurals.number_of_icons,
                        count = total,
                        total
                    ),
                    modifier = Modifier
                        .padding(8.dp),
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                )
            }

            items(
                items = icons,
                key = { it.id },
                contentType = { it.id + it.id != selectedProfileIcon?.id }
            ) { icon ->
                AnimatedVisibility(
                    visible = icon.id != selectedProfileIcon?.id,
                    modifier = Modifier.animateItem()
                ) {
                    ProfileIconItem(
                        icon = icon,
                        size = DEFAULT_ITEM_SIZE,
                        version = version
                    ) {
                        onSelect(icon)
                    }
                }
            }
        }
    )
}

context(visibilityScope: AnimatedVisibilityScope)
@Composable
fun SharedTransitionScope.ProfileIconItem(
    icon: IconUi,
    size: Dp,
    version: String,
    onClick: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .padding(DEFAULT_ITEM_PADDING)
            .sharedBounds(
                sharedContentState = rememberSharedContentState(key = "${icon.id}-bounds"),
                animatedVisibilityScope = visibilityScope,
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IchigoItemImage(
            model = getProfileIconImage(icon.image, version),
            modifier = Modifier
                .then(
                    other = if (onClick != null) {
                        Modifier.clickable(onClick = onClick)
                    } else {
                        Modifier
                    }
                )
                .sharedBounds(
                    sharedContentState = rememberSharedContentState(key = "${icon.id}-image"),
                    animatedVisibilityScope = visibilityScope,
                    clipInOverlayDuringTransition = OverlayClip(DEFAULT_ITEM_SHAPE)
                ),
            size = size,
        )
        IchigoItemLabel(
            text = "#" + icon.id,
            modifier = Modifier
                .sharedBounds(
                    sharedContentState = rememberSharedContentState(key = "${icon.id}-label"),
                    animatedVisibilityScope = visibilityScope,
                )
        )
    }
}

@Composable
fun SharedTransitionScope.IconDetails(
    selectedProfileIcon: IconUi?,
    version: String,
    requestClose: () -> Unit
) {
    AnimatedContent(
        targetState = selectedProfileIcon,
        transitionSpec = { fadeIn() togetherWith fadeOut() },
        label = "IconDetails"
    ) { icon ->
        Box(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            if (icon != null) {
                IconFullscreen(
                    icon = icon,
                    version = version,
                    requestClose = requestClose
                )
            }
        }
    }
}

@Preview
@Composable
fun IconsScreenPreview() {
    AsyncImagePreviewProvider {
        IconsScreen(
            state = UiState.Success(
                content = IconsUiState(
                    icons = (1..100).map {
                        IconUi(
                            id = it.toString(),
                            image = ""
                        )
                    },
                    totalIcons = 100,
                    pageInfo = null,
                    pageSize = 20,
                    version = "1.0.0"
                )
            )
        )
    }
}


@Preview
@Composable
fun IconsScreenPreview2() {
    AsyncImagePreviewProvider {
        IconsScreen(
            state = UiState.Success(
                content = IconsUiState(
                    icons = (1..100).map {
                        IconUi(
                            id = it.toString(),
                            image = ""
                        )
                    },
                    totalIcons = 100,
                    pageInfo = PageInfo(
                        pageIndex = 0,
                        totalPages = 10
                    ),
                    pageSize = 20,
                    version = "1.0.0"
                )
            )
        )
    }
}