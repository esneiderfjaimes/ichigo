@file:OptIn(ExperimentalMaterial3Api::class)

package com.nei.ichigo.feature.encyclopedia.champions.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nei.ichigo.R
import com.nei.ichigo.core.designsystem.component.ErrorScreen
import com.nei.ichigo.core.designsystem.component.LoadingScreen
import com.nei.ichigo.core.designsystem.utils.languageCodeToString
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
            .padding(horizontal = 24.dp)
            .padding(bottom = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = stringResource(R.string.settings),
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
        )
        Spacer(Modifier.height(8.dp))
        when (state) {
            ChampionsSettingsUiState.Error -> {
                ErrorScreen()
            }

            ChampionsSettingsUiState.Loading -> {
                LoadingScreen()
            }

            is ChampionsSettingsUiState.Success -> {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(R.string.language),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(Modifier.weight(1f))
                    var showLanguageDialog by rememberSaveable { mutableStateOf(false) }
                    TextButton(onClick = {
                        showLanguageDialog = true
                    }) {
                        Text(
                            text = state.language?.let { languageCodeToString(it) }
                                ?: stringResource(R.string.automatic)
                        )
                    }
                    if (showLanguageDialog) {
                        LanguageDialog(
                            selectedLanguage = state.language,
                            languages = state.languages,
                            onLanguageSelected = {
                                showLanguageDialog = false
                                onLanguageSelected(it)
                            },
                            onDismiss = { showLanguageDialog = false }
                        )
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "Version",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(Modifier.weight(1f))
                    var showVersionDialog by rememberSaveable { mutableStateOf(false) }
                    TextButton(onClick = {
                        showVersionDialog = true
                    }) {
                        Text(state.version ?: stringResource(R.string.latest))
                    }
                    if (showVersionDialog) {
                        VersionDialog(
                            selectedVersion = state.version,
                            versions = state.versions,
                            onVersionSelected = {
                                showVersionDialog = false
                                onVersionSelected(it)
                            },
                            onDismiss = { showVersionDialog = false }
                        )
                    }
                }
            }
        }
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