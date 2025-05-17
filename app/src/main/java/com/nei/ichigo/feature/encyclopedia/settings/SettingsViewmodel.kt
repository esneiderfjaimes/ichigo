package com.nei.ichigo.feature.encyclopedia.settings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nei.ichigo.core.data.repository.ChampionsRepository
import com.nei.ichigo.core.data.repository.UserSettingsRepository
import com.nei.ichigo.core.model.UserSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewmodel @Inject constructor(
    repository: ChampionsRepository,
    private val userSettingsRepository: UserSettingsRepository
) : ViewModel() {
    val uiState: StateFlow<SettingsUiState> = flow {
        val versions = repository.getVersions()
        val languages = repository.getLanguages()
        emit(versions to languages)
    }.combine<Pair<List<String>, List<String>>, UserSettings, SettingsUiState>(
        userSettingsRepository.userSettings,
    ) { props, userSettings ->
        SettingsUiState.Success(
            version = userSettings.versionSelected,
            versions = props.first,
            language = userSettings.langSelected,
            languages = props.second
        )
    }.catch {
        Log.e("ChampionsSettingsView", ": ", it)
        emit(SettingsUiState.Error)
    }.flowOn(Dispatchers.IO).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = SettingsUiState.Loading,
    )

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

    sealed interface SettingsUiState {
        data object Loading : SettingsUiState
        data class Success(
            val version: String?,
            val versions: List<String>,
            val language: String?,
            val languages: List<String>
        ) : SettingsUiState

        data object Error : SettingsUiState
    }

}