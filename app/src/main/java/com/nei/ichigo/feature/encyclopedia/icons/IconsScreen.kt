package com.nei.ichigo.feature.encyclopedia.icons

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
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
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nei.ichigo.R
import com.nei.ichigo.core.designsystem.component.AsyncImage
import com.nei.ichigo.core.designsystem.component.AsyncImagePreviewProvider
import com.nei.ichigo.core.designsystem.component.BottomPager
import com.nei.ichigo.core.designsystem.component.ErrorScreen
import com.nei.ichigo.core.designsystem.component.LoadingScreen
import com.nei.ichigo.core.designsystem.component.PageInfo
import com.nei.ichigo.core.designsystem.component.TransparentTopAppBar
import com.nei.ichigo.core.designsystem.component.appendTitle
import com.nei.ichigo.core.designsystem.component.appendVersion
import com.nei.ichigo.core.designsystem.theme.Gold
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
    state: IconsUiState,
    onSelectPage: (Int?) -> Unit = {},
    onPageSizeChange: (Int) -> Unit = {},
) {
    SharedTransitionLayout(modifier = Modifier.fillMaxSize()) {
        var selectedProfileIcon by rememberSaveable(stateSaver = IconUi.Saver) {
            mutableStateOf(null)
        }

        Scaffold(
            topBar = {
                IconsTopAppBar(state, onSelectPage, onPageSizeChange)
            },
            bottomBar = {
                if (state is IconsUiState.Success) {
                    state.pageInfo?.let {
                        BottomPager(it) {
                            onSelectPage(it)
                        }
                    }
                }
            },
            contentWindowInsets = WindowInsets.safeDrawing
        ) { innerPadding ->
            when (state) {
                IconsUiState.Error -> {
                    ErrorScreen(modifier = Modifier.padding(innerPadding))
                }

                IconsUiState.Loading -> {
                    LoadingScreen(modifier = Modifier.padding(innerPadding))
                }

                is IconsUiState.Success -> {
                    SuccessContent(
                        innerPadding = innerPadding,
                        icons = state.icons,
                        total = state.totalIcons,
                        version = state.version,
                        selectedProfileIcon = selectedProfileIcon,
                        onSelect = { selectedProfileIcon = it }
                    )
                }
            }
        }

        if (state is IconsUiState.Success) {
            IconDetails(
                selectedProfileIcon = selectedProfileIcon,
                version = state.version,
                requestClose = { selectedProfileIcon = null }
            )
        }
    }
}

@Composable
private fun IconsTopAppBar(
    state: IconsUiState,
    onSelectPage: (Int?) -> Unit = {},
    onPageSizeChange: (Int) -> Unit = {},
) {
    TransparentTopAppBar(text = buildAnnotatedString {
        appendTitle(stringResource(R.string.icons))
        if (state is IconsUiState.Success) {
            appendVersion(" v${state.version}")
        }
    }) {
        if (state is IconsUiState.Success) {
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

private val BORDER_SIZE = 0.75.dp

private val ITEM_SIZE = 70.dp

private val ITEM_SPADING = 4.dp
private val ITEM_SHAPE = RoundedCornerShape(25)
private val GRID_MIN_SIZE = ITEM_SIZE + (ITEM_SPADING * 2) + (BORDER_SIZE * 2)

context(SharedTransitionScope)
@Composable
private fun SuccessContent(
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
                        size = ITEM_SIZE,
                        version = version
                    ) {
                        onSelect(icon)
                    }
                }
            }
        }
    )
}

context(SharedTransitionScope, AnimatedVisibilityScope)
@Composable
fun ProfileIconItem(
    icon: IconUi,
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
        AsyncImage(
            model = getProfileIconImage(icon.image, version),
            modifier = Modifier
                .clip(ITEM_SHAPE)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .border(
                    width = BORDER_SIZE,
                    color = Gold,
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

context(SharedTransitionScope)
@Composable
fun IconDetails(
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
            contentAlignment = Alignment.Center
        ) {
            if (icon != null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable(
                            interactionSource = null,
                            indication = null,
                            onClick = requestClose
                        )
                        .background(Color.Black.copy(alpha = 0.5f))
                )

                ProfileIconItem(
                    icon = icon,
                    version = version,
                    size = ITEM_SIZE * 2,
                    onClick = requestClose
                )

                BackHandler {
                    requestClose()
                }
            }
        }

    }
}

@Preview
@Composable
fun IconsScreenPreview() {
    AsyncImagePreviewProvider {
        IconsScreen(
            state = IconsUiState.Success(
                version = "1.0.0",
                lang = "en",
                totalIcons = 100,
                icons = (1..100).map {
                    IconUi(
                        id = it.toString(),
                        image = ""
                    )
                },
                pageInfo = null,
                pageSize = 20
            )
        )
    }
}


@Preview
@Composable
fun IconsScreenPreview2() {
    AsyncImagePreviewProvider {
        IconsScreen(
            state = IconsUiState.Success(
                version = "1.0.0",
                lang = "en",
                totalIcons = 100,
                icons = (1..100).map {
                    IconUi(
                        id = it.toString(),
                        image = ""
                    )
                },
                pageInfo = PageInfo(
                    pageIndex = 0,
                    totalPages = 10
                ),
                pageSize = 20
            )
        )
    }
}