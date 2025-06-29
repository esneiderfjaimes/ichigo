package com.nei.ichigo.feature.settings

import androidx.lifecycle.viewModelScope
import com.nei.ichigo.common.UiStateViewModel
import com.nei.ichigo.core.data.repository.ChampionsRepository
import com.nei.ichigo.core.data.repository.UserSettingsRepository
import com.nei.ichigo.core.model.DarkThemeConfig
import com.nei.ichigo.feature.settings.SettingsViewmodel.SettingsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewmodel @Inject constructor(
    repository: ChampionsRepository,
    private val userSettingsRepository: UserSettingsRepository
) : UiStateViewModel<SettingsUiState>() {

    override val flow: Flow<SettingsUiState> = combine(
        flow<Pair<List<String>, List<String>>> {
            val versions = repository.getVersions()
            val languages = repository.getLanguages()
            emit(versions to languages)
        },
        userSettingsRepository.userSettings
    ) { props, userSettings ->
        SettingsUiState(
            darkThemeConfig = userSettings.darkThemeConfig,
            useDynamicColor = userSettings.useDynamicColor,
            version = userSettings.versionSelected,
            versions = props.first,
            language = userSettings.langSelected,
            languages = props.second
        )
    }

    fun onLanguageSelected(languageSelected: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            userSettingsRepository.saveLanguageSelected(languageSelected)
        }
    }

    fun onVersionSelected(versionSelected: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            userSettingsRepository.saveVersionSelected(versionSelected)
        }
    }

    fun onDarkThemeSelected(darkThemeConfig: DarkThemeConfig) {
        viewModelScope.launch(Dispatchers.IO) {
            userSettingsRepository.saveDarkThemeConfig(darkThemeConfig)
        }
    }

    fun onUseDynamicColorSelected(useDynamicColor: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            userSettingsRepository.saveUseDynamicColor(useDynamicColor)
        }
    }

    data class SettingsUiState(
        val darkThemeConfig: DarkThemeConfig,
        val useDynamicColor: Boolean,
        val version: String?,
        val versions: List<String>,
        val language: String?,
        val languages: List<String>
    )
}