package com.nei.ichigo.feature.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nei.ichigo.BuildConfig
import com.nei.ichigo.R
import com.nei.ichigo.common.BaseScreen
import com.nei.ichigo.common.UiState
import com.nei.ichigo.core.data.model.ConfigValue
import com.nei.ichigo.core.designsystem.component.appendTitle
import com.nei.ichigo.core.designsystem.theme.supportsDynamicTheming
import com.nei.ichigo.core.designsystem.utils.languageCodeToString
import com.nei.ichigo.core.model.DarkThemeConfig
import com.nei.ichigo.feature.settings.SettingsViewmodel.SettingsUiState

@Composable
fun SettingsScreen(onLicenseClick: () -> Unit = {}) {
    val viewmodel: SettingsViewmodel = hiltViewModel()
    val state by viewmodel.uiState.collectAsStateWithLifecycle()

    SettingsScreen(
        state = state,
        onUseDynamicColorSelected = viewmodel::onUseDynamicColorSelected,
        onDarkThemeSelected = viewmodel::onDarkThemeSelected,
        onLanguageSelected = viewmodel::onLanguageSelected,
        onVersionSelected = viewmodel::onVersionSelected,
        onLicenseClick = onLicenseClick
    )
}

@Composable
private fun SettingsScreen(
    state: UiState<out SettingsUiState>,
    onUseDynamicColorSelected: (Boolean) -> Unit = {},
    onDarkThemeSelected: (DarkThemeConfig) -> Unit = {},
    onLanguageSelected: (String?) -> Unit = {},
    onVersionSelected: (String?) -> Unit = {},
    onLicenseClick: () -> Unit = {},
) {
    BaseScreen(
        state = state,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = buildAnnotatedString {
                            appendTitle(stringResource(R.string.settings))
                        }
                    )
                },
            )
        },
    ) { state, innerPadding ->
        Column(
            Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            SuccessContent(
                state = state,
                onUseDynamicColorSelected = onUseDynamicColorSelected,
                onDarkThemeSelected = onDarkThemeSelected,
                onLanguageSelected = onLanguageSelected,
                onVersionSelected = onVersionSelected,
                onLicenseClick = onLicenseClick
            )
        }
    }
}

@Composable
fun ColumnScope.SuccessContent(
    state: SettingsUiState,
    onUseDynamicColorSelected: (Boolean) -> Unit = {},
    onDarkThemeSelected: (DarkThemeConfig) -> Unit = {},
    onLanguageSelected: (String?) -> Unit = {},
    onVersionSelected: (String?) -> Unit = {},
    onLicenseClick: () -> Unit = {},
) {
    SectionTitle(
        text = stringResource(R.string.appearance),
    )

    if (supportsDynamicTheming()) {
        Item(
            text = stringResource(R.string.use_dynamic_colors),
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
        text = stringResource(R.string.dark_theme),
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
    AnimatedVisibility(
        visible = expanded,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            DarkThemeConfig.entries.forEach { config ->
                Item(
                    text = when (config) {
                        DarkThemeConfig.FOLLOW_SYSTEM -> stringResource(R.string.default_system_theme)
                        DarkThemeConfig.LIGHT -> stringResource(R.string.light)
                        DarkThemeConfig.DARK -> stringResource(R.string.dark)
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
        text = stringResource(R.string.data_dragon_provided_by_riot_games),
    )

    ItemListSelector(
        text = stringResource(R.string.version),
        value = versionToString(state.version),
        bottomSheetContent = { dismiss ->
            VersionDialog(
                selectedVersion = state.version.byUser,
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
        value = languageToString(state.language),
        bottomSheetContent = { dismiss ->
            LanguageDialog(
                selectedLanguage = state.language.byUser,
                languages = state.languages,
                onLanguageSelected = {
                    dismiss()
                    onLanguageSelected(it)
                },
                onDismiss = { dismiss() }
            )
        }
    )

    Spacer(
        modifier = Modifier
            .weight(1f)
    )

    Text(
        text = buildString {
            append(stringResource(R.string.version))
            append(" ")
            append(BuildConfig.VERSION_NAME)
            append(" (")
            append(BuildConfig.VERSION_CODE)
            append(")")
        },
        modifier = Modifier
            .padding(top = 150.dp)
            .padding(horizontal = 28.dp)
    )
    TextButton(
        onClick = onLicenseClick,
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(bottom = 16.dp)
    ) {
        Text(text = stringResource(R.string.licenses))
    }
}

@Composable
fun versionToString(value: ConfigValue) = when (value) {
    is ConfigValue.AutomaticSelection -> buildString {
        append(stringResource(R.string.latest))
        append(" • ")
        append(value.value)
    }

    is ConfigValue.SelectedByUser -> value.value
}

@Composable
fun languageToString(value: ConfigValue) = when (value) {
    is ConfigValue.AutomaticSelection -> buildString {
        append(stringResource(R.string.automatic))
        append(" • ")
        append(languageCodeToString(value.value))
    }

    is ConfigValue.SelectedByUser -> languageCodeToString(value.value)
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
                Text(value, textAlign = TextAlign.End)
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
                .minimumInteractiveComponentSize()
        )
        Spacer(Modifier.weight(1f))
        action()
        Spacer(Modifier.width(24.dp))
    }
}

@Preview
@Composable
fun ChampionsSettingsDialogContentPreview() {
    SettingsScreen(
        state = UiState.Success(
            SettingsUiState(
                darkThemeConfig = DarkThemeConfig.FOLLOW_SYSTEM,
                useDynamicColor = true,
                version = ConfigValue.AutomaticSelection("1.0.0"),
                language = ConfigValue.AutomaticSelection("en_US"),
                versions = emptyList(),
                languages = emptyList()
            )
        )
    )
}
