package com.nei.ichigo.feature.encyclopedia.champion

import com.nei.ichigo.common.Base2ViewModel
import com.nei.ichigo.common.PageUiState
import com.nei.ichigo.core.domain.GetChampionUseCase
import com.nei.ichigo.core.model.ChampionDetail
import com.nei.ichigo.feature.encyclopedia.champion.ChampionViewModel.ChampionUiState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update

@HiltViewModel(assistedFactory = ChampionViewModel.Factory::class)
class ChampionViewModel @AssistedInject constructor(
    getChampionUseCase: GetChampionUseCase,
    @Assisted val championId: String,
) : Base2ViewModel<ChampionUiState>() {

    private val _selectedSkin = MutableStateFlow<Int?>(null)

    override val flow = combine(
        flow = getChampionUseCase.invoke(championId),
        flow2 = _selectedSkin
    ) { result, selectedSkin ->
        val page = result.getOrThrow()
        ChampionUiState(
            champion = page.champion,
            selectedSkin = selectedSkin,
            version = page.version,
        )
    }

    fun updateSelectedSkin(skinId: Int?) {
        _selectedSkin.update { skinId }
    }

    data class ChampionUiState(
        val champion: ChampionDetail,
        val selectedSkin: Int? = null,
        override val version: String,
    ) : PageUiState

    @AssistedFactory
    interface Factory {
        fun create(topicId: String): ChampionViewModel
    }
}