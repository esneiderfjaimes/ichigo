package com.nei.ichigo.feature.encyclopedia.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.ExpandLess
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nei.ichigo.R
import com.nei.ichigo.core.designsystem.component.ErrorScreen
import com.nei.ichigo.core.designsystem.component.LoadingScreen
import com.nei.ichigo.core.designsystem.theme.supportsDynamicTheming
import com.nei.ichigo.core.designsystem.utils.languageCodeToString
import com.nei.ichigo.core.model.DarkThemeConfig
import com.nei.ichigo.feature.encyclopedia.settings.SettingsViewmodel.SettingsUiState

@Composable
fun SettingsDialog(onDismiss: () -> Unit) {
    val viewmodel: SettingsViewmodel = hiltViewModel()
    val state by viewmodel.uiState.collectAsStateWithLifecycle()
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        /*
        modifier = Modifier.padding(24.dp),
        shape = MaterialTheme.shapes.extraLarge,
        */
    ) {
        ChampionsSettingsDialogContent(
            state = state,
            onUseDynamicColorSelected = viewmodel::onUseDynamicColorSelected,
            onDarkThemeSelected = viewmodel::onDarkThemeSelected,
            onLanguageSelected = viewmodel::onLanguageSelected,
            onVersionSelected = viewmodel::onVersionSelected
        )
    }
}

@Composable
private fun ChampionsSettingsDialogContent(
    state: SettingsUiState,
    onUseDynamicColorSelected: (Boolean) -> Unit = {},
    onDarkThemeSelected: (DarkThemeConfig) -> Unit = {},
    onLanguageSelected: (String?) -> Unit = {},
    onVersionSelected: (String?) -> Unit = {}
) {
    Column(
        Modifier
            .padding(bottom = 24.dp)
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
            SettingsUiState.Error -> {
                ErrorScreen()
            }

            SettingsUiState.Loading -> {
                LoadingScreen()
            }

            is SettingsUiState.Success -> {
                SectionTitle(
                    text = "Appearance",
                )

                if (supportsDynamicTheming()) {
                    Item(
                        text = "Use dynamic colors",
                        onClick = { onUseDynamicColorSelected(!state.useDynamicColor) },
                    ) {
                        Switch(
                            checked = state.useDynamicColor,
                            onCheckedChange = { onUseDynamicColorSelected(it) },
                        )
                    }
                }

                var expanded by rememberSaveable { mutableStateOf(false) }
                Item(
                    text = "Dark theme",
                    onClick = { expanded = !expanded },
                ) {
                    IconToggleButton(
                        checked = expanded,
                        onCheckedChange = { expanded = !expanded },
                    ) {
                        Icon(
                            imageVector = if (expanded) Icons.Rounded.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = null
                        )
                    }
                }
                AnimatedVisibility(visible = expanded) {
                    Column {
                        DarkThemeConfig.entries.forEach { config ->
                            Item(
                                text = when (config) {
                                    DarkThemeConfig.FOLLOW_SYSTEM -> "Default system theme"
                                    DarkThemeConfig.LIGHT -> "Light"
                                    DarkThemeConfig.DARK -> "Dark"
                                },
                                style = MaterialTheme.typography.bodyMedium,
                                onClick = { onDarkThemeSelected(config) },
                            ) {
                                Box(
                                    modifier = Modifier.minimumInteractiveComponentSize()
                                ) {
                                    if (state.darkThemeConfig == config) {
                                        Icon(
                                            imageVector = Icons.Rounded.Check,
                                            contentDescription = null
                                        )
                                    }
                                }
                            }
                        }
                        HorizontalDivider()
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                SectionTitle(
                    text = "Data provided by Riot Games",
                )

                ItemListSelector(
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

                ItemListSelector(
                    text = stringResource(R.string.language),
                    value = state.language?.let { languageCodeToString(it) }
                        ?: stringResource(R.string.automatic),
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
fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall
            .copy(fontWeight = FontWeight.Black),
        modifier = Modifier
            .alpha(0.75f)
            .padding(horizontal = 24.dp, vertical = 12.dp)
    )
}

@Composable
fun ItemListSelector(
    text: String,
    value: String,
    bottomSheetContent: @Composable (() -> Unit) -> Unit
) {
    var showBottomSheet by rememberSaveable { mutableStateOf(false) }

    Item(
        onClick = { showBottomSheet = true },
        text = text,
        action = {
            TextButton(
                onClick = { showBottomSheet = true },
            ) {
                Text(value)
            }
        }
    )

    if (showBottomSheet) {
        bottomSheetContent { showBottomSheet = false }
    }
}

@Composable
fun Item(
    onClick: () -> Unit,
    text: String,
    style: TextStyle = MaterialTheme.typography.bodyLarge,
    action: @Composable () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(MaterialTheme.shapes.large)
            .clickable(onClick = onClick)
    ) {
        Text(
            text = text,
            style = style,
            modifier = Modifier
                .padding(start = 24.dp)
                .weight(1f)
                .minimumInteractiveComponentSize()
        )
        action()
        Spacer(Modifier.width(24.dp))
    }
}

@Preview
@Composable
fun ChampionsSettingsDialogContentPreview() {
    MaterialTheme {
        Surface {
            ChampionsSettingsDialogContent(
                state = SettingsUiState.Success(
                    darkThemeConfig = DarkThemeConfig.FOLLOW_SYSTEM,
                    useDynamicColor = true,
                    version = null,
                    language = null,
                    versions = emptyList(),
                    languages = emptyList()
                )
            )
        }
    }
}

@Preview
@Composable
fun ChampionsSettingsDialogContentErrorPreview() {
    Surface {
        ChampionsSettingsDialogContent(state = SettingsUiState.Error)
    }
}

@Preview
@Composable
fun ChampionsSettingsDialogContentLoadingPreview() {
    Surface {
        ChampionsSettingsDialogContent(state = SettingsUiState.Loading)
    }
}