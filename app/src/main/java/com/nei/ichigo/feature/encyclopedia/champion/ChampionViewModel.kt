package com.nei.ichigo.feature.encyclopedia.champion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nei.ichigo.core.domain.GetChampionUseCase
import com.nei.ichigo.core.model.ChampionDetail
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

@HiltViewModel(assistedFactory = ChampionViewModel.Factory::class)
class ChampionViewModel @AssistedInject constructor(
    getChampionUseCase: GetChampionUseCase,
    @Assisted val championId: String,
) : ViewModel() {

    private val _selectedSkin = MutableStateFlow<Int?>(null)

    val uiState: StateFlow<ChampionUiState> = championUiState(
        championId = championId,
        getChampionUseCase = getChampionUseCase
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ChampionUiState.Loading,
    )

    private fun championUiState(championId: String, getChampionUseCase: GetChampionUseCase) =
        getChampionUseCase.invoke(championId)
            .combine(_selectedSkin) { result, selectedSkin ->
                result.fold(
                    onSuccess = { page ->
                        ChampionUiState.Success(
                            page.champion,
                            page.version,
                            selectedSkin
                        )
                    },
                    onFailure = { ChampionUiState.Error }
                )
            }

    fun updateSelectedSkin(skinId: Int?) {
        _selectedSkin.update { skinId }
    }

    sealed interface ChampionUiState {
        data object Loading : ChampionUiState
        data object Error : ChampionUiState
        data class Success(
            val champion: ChampionDetail,
            val version: String,
            val selectedSkin: Int? = null
        ) : ChampionUiState
    }

    @AssistedFactory
    interface Factory {
        fun create(topicId: String): ChampionViewModel
    }
}