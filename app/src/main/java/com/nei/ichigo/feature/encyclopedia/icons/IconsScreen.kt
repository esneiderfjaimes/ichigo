package com.nei.ichigo.feature.encyclopedia.icons

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridItemSpan
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nei.ichigo.R
import com.nei.ichigo.common.BaseScreen
import com.nei.ichigo.common.BaseTopAppBar
import com.nei.ichigo.common.UiState
import com.nei.ichigo.common.layout.BaseShimmer
import com.nei.ichigo.common.layout.Grid
import com.nei.ichigo.common.utils.LocalAnimatedVisibilityScope
import com.nei.ichigo.common.utils.SharedTransitionPreviewProvider
import com.nei.ichigo.common.utils.withSharedTransitionScope
import com.nei.ichigo.core.designsystem.component.AsyncImagePreviewProvider
import com.nei.ichigo.core.designsystem.component.BottomPager
import com.nei.ichigo.core.designsystem.component.DEFAULT_ITEM_PADDING
import com.nei.ichigo.core.designsystem.component.DEFAULT_ITEM_SHAPE
import com.nei.ichigo.core.designsystem.component.DEFAULT_ITEM_SIZE
import com.nei.ichigo.core.designsystem.component.IchigoItemImage
import com.nei.ichigo.core.designsystem.component.IchigoItemLabel
import com.nei.ichigo.core.designsystem.component.IchigoItemShimmerImage
import com.nei.ichigo.core.designsystem.component.IchigoItemShimmerLabel
import com.nei.ichigo.core.designsystem.component.PageInfo
import com.nei.ichigo.core.designsystem.component.ShimmerScope
import com.nei.ichigo.core.designsystem.theme.IchigoThemePreview
import com.nei.ichigo.core.designsystem.utils.getProfileIconImage
import com.nei.ichigo.feature.encyclopedia.icons.IconsViewModel.IconsUiState

@Composable
fun IconsScreen(
    onIconClick: (IconUi, String) -> Unit,
) {
    val viewModel: IconsViewModel = hiltViewModel()
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    IconsScreen(
        state = state,
        onIconClick = onIconClick,
        onSelectPage = viewModel::onSelectPage,
        onPageSizeChange = viewModel::onPageSizeChange
    )
}

@Composable
private fun IconsScreen(
    state: UiState<out IconsUiState>,
    onIconClick: (IconUi, String) -> Unit = { _, _ -> },
    onSelectPage: (Int?) -> Unit = {},
    onPageSizeChange: (Int) -> Unit = {},
) {
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
        },
        shimmerContent = { ShimmerContent(it) },
    ) { state, innerPadding ->
        SuccessContent(
            innerPadding = innerPadding,
            icons = state.icons,
            total = state.totalIcons,
            version = state.version,
            onSelect = {
                onIconClick(it, state.version)
            }
        )
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
private fun SuccessContent(
    innerPadding: PaddingValues,
    icons: List<IconUi>,
    total: Int,
    version: String,
    onSelect: (IconUi) -> Unit = {},
) {
    Grid(
        minSize = GRID_MIN_SIZE,
        innerPadding = innerPadding,
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
                contentType = { it.id }
            ) { icon ->
                ProfileIconItem(
                    icon = icon,
                    size = DEFAULT_ITEM_SIZE,
                    version = version
                ) {
                    onSelect(icon)
                }
            }
        }
    )
}

@Composable
fun ProfileIconItem(
    icon: IconUi,
    size: Dp,
    version: String,
    onClick: (() -> Unit)? = null
) {
    val animatedVisibilityScope = LocalAnimatedVisibilityScope.current
    Column(
        modifier = Modifier
            .padding(DEFAULT_ITEM_PADDING),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IchigoItemImage(
            model = getProfileIconImage(icon.image, version),
            backgroundColor = Color.Transparent,
            modifier = Modifier
                .then(
                    other = if (onClick != null) {
                        Modifier.clickable(onClick = onClick)
                    } else {
                        Modifier
                    }
                )
                .withSharedTransitionScope { modifier ->
                    modifier.sharedBounds(
                        sharedContentState = rememberSharedContentState(key = "${icon.id}-image"),
                        animatedVisibilityScope = animatedVisibilityScope,
                    )
                }
                // requiered for shared transition
                .clip(DEFAULT_ITEM_SHAPE),
            size = size,
        )
        IchigoItemLabel(
            text = "#" + icon.id,
        )
    }
}

@Composable
private fun ShimmerScope.ShimmerContent(
    innerPadding: PaddingValues,
) {
    BaseShimmer(
        minSize = GRID_MIN_SIZE,
        idPlural = R.plurals.number_of_icons,
        innerPadding = innerPadding,
        sizeItems = 20,
        itemContent = { ProfileIconShimmerItem(size = DEFAULT_ITEM_SIZE) }
    )
}

@Composable
fun ShimmerScope.ProfileIconShimmerItem(size: Dp) {
    Column(
        modifier = Modifier
            .padding(DEFAULT_ITEM_PADDING),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IchigoItemShimmerImage(size = size)
        IchigoItemShimmerLabel(text = "      ")
    }
}

@PreviewLightDark
@Composable
fun IconsScreenPreview() {
    IchigoThemePreview {
        AsyncImagePreviewProvider {
            SharedTransitionPreviewProvider {
                IconsScreen(
                    state = UiState.Success(
                        content = IconsUiState(
                            icons = (1..100).map {
                                IconUi(
                                    id = it.toString(),
                                    image = "h_103,w_103"
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
    }
}

@PreviewLightDark
@Composable
fun IconsScreenPreview2() {
    IchigoThemePreview {
        AsyncImagePreviewProvider {
            SharedTransitionPreviewProvider {
                IconsScreen(
                    state = UiState.Success(
                        content = IconsUiState(
                            icons = (1..100).map {
                                IconUi(
                                    id = it.toString(),
                                    image = "h_103,w_103"
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
    }
}

@PreviewLightDark
@Composable
fun IconsScreenLoadingPreview() {
    IchigoThemePreview {
        AsyncImagePreviewProvider {
            SharedTransitionPreviewProvider {
                IconsScreen(
                    state = UiState.Loading
                )
            }
        }
    }
}