package com.nei.ichigo.feature.encyclopedia.champions.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nei.ichigo.R
import com.nei.ichigo.core.designsystem.component.ErrorScreen
import com.nei.ichigo.core.designsystem.component.LoadingScreen
import com.nei.ichigo.feature.encyclopedia.champions.settings.ChampionsSettingsViewmodel.ChampionsSettingsUiState

@Composable
fun ChampionsSettingsDialog(onDismiss: () -> Unit) {
    val viewmodel: ChampionsSettingsViewmodel = hiltViewModel()
    val state by viewmodel.uiState.collectAsStateWithLifecycle()
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        modifier = Modifier.padding(24.dp),
        shape = MaterialTheme.shapes.extraLarge,
    ) {
        ChampionsSettingsDialogContent(
            state,
            viewmodel::onLanguageSelected,
            viewmodel::onVersionSelected
        )
    }
}

@Composable
private fun ChampionsSettingsDialogContent(
    state: ChampionsSettingsUiState,
    onLanguageSelected: (String?) -> Unit = {},
    onVersionSelected: (String?) -> Unit = {}
) {
    Column(
        Modifier
            .padding(bottom = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = stringResource(R.string.settings),
            style = MaterialTheme.typography.titleLarge
                .copy(fontWeight = FontWeight.Bold),
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .padding(bottom = 12.dp)
        )
        when (state) {
            ChampionsSettingsUiState.Error -> {
                ErrorScreen()
            }

            ChampionsSettingsUiState.Loading -> {
                LoadingScreen()
            }

            is ChampionsSettingsUiState.Success -> {
                Item(
                    text = stringResource(R.string.version),
                    value = state.version ?: stringResource(R.string.latest),
                    bottomSheetContent = { dismiss ->
                        VersionDialog(
                            selectedVersion = state.version,
                            versions = state.versions,
                            onVersionSelected = {
                                dismiss()
                                onVersionSelected(it)
                            },
                            onDismiss = { dismiss() }
                        )
                    }
                )

                Item(
                    text = stringResource(R.string.language),
                    value = state.language ?: stringResource(R.string.automatic),
                    bottomSheetContent = { dismiss ->
                        LanguageDialog(
                            selectedLanguage = state.language,
                            languages = state.languages,
                            onLanguageSelected = {
                                dismiss()
                                onLanguageSelected(it)
                            },
                            onDismiss = { dismiss() }
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun Item(
    text: String,
    value: String,
    bottomSheetContent: @Composable (() -> Unit) -> Unit
) {
    var showBottomSheet by rememberSaveable { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(MaterialTheme.shapes.large)
            .clickable { showBottomSheet = true }
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = 24.dp)
        )
        Spacer(Modifier.weight(1f))
        TextButton(
            onClick = { showBottomSheet = true },
            modifier = Modifier.padding(end = 24.dp)
        ) {
            Text(value)
        }
    }

    if (showBottomSheet) {
        bottomSheetContent { showBottomSheet = false }
    }
}

@Preview
@Composable
fun ChampionsSettingsDialogContentPreview() {
    Surface {
        ChampionsSettingsDialogContent(
            state = ChampionsSettingsUiState.Success(
                version = null,
                language = null,
                versions = emptyList(),
                languages = emptyList()
            )
        )
    }
}

@Preview
@Composable
fun ChampionsSettingsDialogContentErrorPreview() {
    Surface {
        ChampionsSettingsDialogContent(state = ChampionsSettingsUiState.Error)
    }
}

@Preview
@Composable
fun ChampionsSettingsDialogContentLoadingPreview() {
    Surface {
        ChampionsSettingsDialogContent(state = ChampionsSettingsUiState.Loading)
    }
}