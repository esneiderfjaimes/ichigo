package com.nei.ichigo.feature.encyclopedia.champions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nei.ichigo.core.data.repository.ChampionsRepository
import com.nei.ichigo.core.data.repository.FakeChampionsRepository
import com.nei.ichigo.core.model.Champion
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

class ChampionsViewModel(
    private val repository: ChampionsRepository
) : ViewModel() {
    val uiState: StateFlow<ChampionsUiState> = flow<ChampionsUiState> {
        val champions = repository.getChampions()
        emit(ChampionsUiState.Success(champions))
    }.catch {
        emit(ChampionsUiState.Error)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ChampionsUiState.Loading,
    )


    companion object {
        val Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(ChampionsViewModel::class.java)) {
                    return ChampionsViewModel(
                        FakeChampionsRepository()
                    ) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }

    sealed interface ChampionsUiState {
        data object Loading : ChampionsUiState
        data class Success(val champions: List<Champion>) : ChampionsUiState
        data object Error : ChampionsUiState
    }
}