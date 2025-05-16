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
class ChampionsSettingsViewmodel @Inject constructor(
    repository: ChampionsRepository,
    private val userSettingsRepository: UserSettingsRepository
) : ViewModel() {
    val uiState: StateFlow<ChampionsSettingsUiState> = flow {
        val versions = repository.getVersions()
        val languages = repository.getLanguages()
        emit(versions to languages)
    }.combine<Pair<List<String>, List<String>>, UserSettings, ChampionsSettingsUiState>(
        userSettingsRepository.userSettings,
    ) { props, userSettings ->
        ChampionsSettingsUiState.Success(
            version = userSettings.versionSelected,
            versions = props.first,
            language = userSettings.langSelected,
            languages = props.second
        )
    }.catch {
        Log.e("ChampionsSettingsView", ": ", it)
        emit(ChampionsSettingsUiState.Error)
    }.flowOn(Dispatchers.IO).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ChampionsSettingsUiState.Loading,
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

    sealed interface ChampionsSettingsUiState {
        data object Loading : ChampionsSettingsUiState
        data class Success(
            val version: String?,
            val versions: List<String>,
            val language: String?,
            val languages: List<String>
        ) : ChampionsSettingsUiState

        data object Error : ChampionsSettingsUiState
    }

}