package com.nei.ichigo.feature.encyclopedia.icons

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.SubcomposeAsyncImage
import com.nei.ichigo.R
import com.nei.ichigo.core.designsystem.component.ErrorScreen
import com.nei.ichigo.core.designsystem.component.LoadingScreen
import com.nei.ichigo.core.designsystem.component.TransparentTopAppBar
import com.nei.ichigo.core.designsystem.utils.getProfileIconImage
import com.nei.ichigo.core.model.ProfileIcon
import com.nei.ichigo.feature.encyclopedia.icons.IconsViewModel.IconsUiState

@Composable
fun IconsScreen() {
    val viewModel: IconsViewModel = hiltViewModel()
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    IconsScreen(state = state)
}

@Composable
private fun IconsScreen(state: IconsUiState) {
    Scaffold(
        topBar = {
            IconsTopAppBar(state)
        }
    ) { innerPadding ->
        when (state) {
            IconsUiState.Error -> {
                ErrorScreen()
            }

            IconsUiState.Loading -> {
                LoadingScreen()
            }

            is IconsUiState.Success -> {
                val layoutDirection = LocalLayoutDirection.current
                val contentPadding = PaddingValues(
                    top = innerPadding.calculateTopPadding(),
                    bottom = innerPadding.calculateBottomPadding() + 8.dp,
                    start = innerPadding.calculateStartPadding(layoutDirection) + 32.dp,
                    end = innerPadding.calculateEndPadding(layoutDirection) + 32.dp
                )

                LazyVerticalGrid(
                    modifier = Modifier,
                    columns = GridCells.Adaptive(minSize = ITEM_SIZE + (ITEM_SPADING * 2) + (BORDER_SIZE * 2)),
                    horizontalArrangement = Arrangement.SpaceAround,
                    contentPadding = contentPadding,
                    content = {
                        item(span = { GridItemSpan(maxLineSpan) }) {
                            Text(
                                text = pluralStringResource(
                                    id = R.plurals.number_of_icons,
                                    count = state.icons.size,
                                    state.icons.size
                                ),
                                modifier = Modifier
                                    .padding(8.dp),
                                style = MaterialTheme.typography.bodySmall,
                                textAlign = TextAlign.Center,
                            )
                        }

                        items(state.icons) { icon ->
                            ProfileIconItem(icon, state.version, Modifier.animateItem())
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun IconsTopAppBar(
    state: IconsUiState
) {
    TransparentTopAppBar(text = buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        ) {
            append(stringResource(R.string.icons))
        }
        if (state is IconsUiState.Success) {
            if (state.version.isNotBlank()) withStyle(
                style = SpanStyle(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    fontSize = MaterialTheme.typography.titleSmall.fontSize
                )
            ) {
                append(" v${state.version}")
            }
        }
    })
}

private val BORDER_SIZE = 0.75.dp

private val ITEM_SIZE = 70.dp
private val ITEM_SPADING = 4.dp

@Composable
fun ProfileIconItem(icon: ProfileIcon, version: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .padding(ITEM_SPADING),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SubcomposeAsyncImage(
            model = getProfileIconImage(icon.image, version),
            contentDescription = null,
            modifier = Modifier
                .clip(RoundedCornerShape(25))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .border(
                    width = BORDER_SIZE,
                    color = Color(0xFFC28F2C),
                    shape = RoundedCornerShape(25)
                )
                .padding(BORDER_SIZE)
                .size(ITEM_SIZE),
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
                    CircularProgressIndicator(Modifier.size(24.dp))
                }
            }
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = "#" + icon.id,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center
        )
    }
}