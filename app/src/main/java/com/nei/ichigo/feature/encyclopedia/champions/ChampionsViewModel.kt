package com.nei.ichigo.feature.encyclopedia.champions

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nei.ichigo.core.data.repository.ChampionsRepository
import com.nei.ichigo.core.model.Champion
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class ChampionsViewModel @Inject constructor(
    private val repository: ChampionsRepository
) : ViewModel() {
    init {
        viewModelScope.launch {
            repository.getVersions().let {
                Log.i("ChampionsViewModel", ": versions: $it")
            }
        }
    }

    val uiState: StateFlow<ChampionsUiState> = flow<ChampionsUiState> {
        val (version, lang, champions) = repository.getChampionsPage()
        emit(ChampionsUiState.Success(version, lang, champions))
    }.catch {
        Log.e("ChampionsViewModel", ": ", it)
        emit(ChampionsUiState.Error)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ChampionsUiState.Loading,
    )

    sealed interface ChampionsUiState {
        data object Loading : ChampionsUiState
        data class Success(
            val version: String,
            val lang: String,
            val champions: List<Champion>
        ) : ChampionsUiState

        data object Error : ChampionsUiState
    }
}