package com.nei.ichigo.common

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.IndicatorMaxDistance
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import com.nei.ichigo.core.designsystem.R
import com.nei.ichigo.core.designsystem.component.ErrorScreen
import com.nei.ichigo.core.designsystem.component.LoadingScreen
import com.nei.ichigo.core.designsystem.component.ShimmerProvider
import com.nei.ichigo.core.designsystem.component.ShimmerScope
import com.nei.ichigo.core.designsystem.component.TransparentTopAppBar
import com.nei.ichigo.core.designsystem.component.appendTitle
import com.nei.ichigo.core.designsystem.component.appendVersion

@Composable
fun <T> BaseScreen(
    state: UiState<T>,
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    contentWindowInsets: WindowInsets = WindowInsets.safeDrawing.only(
        WindowInsetsSides.Vertical + WindowInsetsSides.End
    ),
    shimmerContent: (@Composable ShimmerScope.(innerPadding: PaddingValues) -> Unit)? = null,
    onRefresh: (() -> Unit)? = null,
    content: @Composable (state: T, innerPadding: PaddingValues) -> Unit
) {
    val pullToRefreshState = rememberPullToRefreshState()

    Scaffold(
        topBar = topBar,
        bottomBar = bottomBar,
        contentWindowInsets = contentWindowInsets
    ) { innerPadding ->
        when (state) {
            is UiState.Error -> {
                PullToRefreshBox(
                    isRefreshing = state.isRefreshing,
                    enabled = onRefresh != null,
                    onRefresh = {
                        onRefresh?.invoke()
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    ErrorScreen(
                        Modifier
                            .fillMaxSize(),
                        //  .padding(innerPadding),
                        message = stringResource(state.messageRes)
                    )
                }
            }

            UiState.Loading -> {
                if (shimmerContent == null) {
                    LoadingScreen(
                        Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    )
                } else {
                    ShimmerProvider {
                        shimmerContent(innerPadding)
                    }
                }
            }

            is UiState.Success -> {
                PullToRefreshBox(
                    state = pullToRefreshState,
                    isRefreshing = state.isRefreshing,
                    onRefresh = {
                        onRefresh?.invoke()
                    },
                    enabled = onRefresh != null,
                    indicator = {
                        innerPadding.calculateTopPadding()

                        Indicator(
                            modifier = Modifier
                                .align(Alignment.TopCenter),
                            isRefreshing = state.isRefreshing,
                            state = pullToRefreshState,
                            maxDistance = IndicatorMaxDistance// + topPadding
                        )

                    }
                ) {
                    content(state.content, innerPadding)
                }
            }
        }
    }
}

@Composable
fun <T : PageUiState> BaseTopAppBar(
    state: UiState<T>,
    @StringRes title: Int,
    actions: @Composable (RowScope.() -> Unit) = {}
) {
    TransparentTopAppBar(
        text = buildAnnotatedString {
            appendTitle(stringResource(title))
            if (state is UiState.Success) {
                appendVersion(state.content.version)
            }
        },
        actions = actions
    )
}

@Preview
@Composable
private fun BaseScreenPreview() {
    BaseScreen(UiState.Success("test")) { state, _ ->
        Text(state)
    }
}

@Preview
@Composable
private fun BaseScreenLoadingPreview() {
    BaseScreen(UiState.Loading) { _, _ ->
    }
}

@Preview
@Composable
private fun BaseScreenErrorPreview() {
    BaseScreen(UiState.Error(R.string.core_designsystemy_generic_error)) { _, _ ->
    }
}

