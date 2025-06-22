package com.nei.ichigo.common

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import com.nei.ichigo.core.designsystem.component.ErrorScreen
import com.nei.ichigo.core.designsystem.component.LoadingScreen
import com.nei.ichigo.core.designsystem.component.TransparentTopAppBar
import com.nei.ichigo.core.designsystem.component.appendTitle
import com.nei.ichigo.core.designsystem.component.appendVersion

@Composable
fun <T> BaseScreen(
    state: UiState<T>,
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    content: @Composable (state: T, innerPadding: PaddingValues) -> Unit
) {
    Scaffold(
        topBar = topBar,
        bottomBar = bottomBar
    ) { innerPadding ->
        when (state) {
            UiState.Error -> {
                ErrorScreen(
                    Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )
            }

            UiState.Loading -> {
                LoadingScreen(
                    Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )
            }

            is UiState.Success -> {
                content(state.content, innerPadding)
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
    BaseScreen(UiState.Error) { _, _ ->
    }
}

