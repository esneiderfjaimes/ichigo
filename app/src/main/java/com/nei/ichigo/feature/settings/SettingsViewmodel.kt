@file:OptIn(ExperimentalCoroutinesApi::class)

package com.nei.ichigo.feature.settings

import androidx.lifecycle.viewModelScope
import com.nei.ichigo.common.UiStateViewModel
import com.nei.ichigo.core.data.model.ConfigValue
import com.nei.ichigo.core.data.repository.AppConfigRepository
import com.nei.ichigo.core.data.repository.DDragonRepository
import com.nei.ichigo.core.data.repository.UserSettingsRepository
import com.nei.ichigo.core.model.DarkThemeConfig
import com.nei.ichigo.feature.settings.SettingsViewmodel.SettingsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewmodel @Inject constructor(
    private val dDragonRepository: DDragonRepository,
    private val userSettingsRepository: UserSettingsRepository,
    appConfigRepository: AppConfigRepository
) : UiStateViewModel<SettingsUiState>() {

    override val flow: Flow<SettingsUiState> = combine(
        appConfigRepository.config,
        dDragonRepository.metaData,
        userSettingsRepository.userSettings
    ) { config, metadata, userSettings ->
        val (version, language) = config.getOrThrow()
        val (versions, languages) = metadata.getOrThrow()
        SettingsUiState(
            darkThemeConfig = userSettings.darkThemeConfig,
            useDynamicColor = userSettings.useDynamicColor,
            version = version,
            versions = versions,
            language = language,
            languages = languages
        )
    }

    fun onRefresh() {
        viewModelScope.launch(Dispatchers.IO) {
            dDragonRepository.forceUpdate()
        }
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
        val version: ConfigValue,
        val versions: List<String>,
        val language: ConfigValue,
        val languages: List<String>
    )
}