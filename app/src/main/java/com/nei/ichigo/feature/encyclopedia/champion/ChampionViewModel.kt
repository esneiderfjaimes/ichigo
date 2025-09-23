package com.nei.ichigo.feature.encyclopedia.champion

import com.nei.ichigo.common.PageUiState
import com.nei.ichigo.common.UiStateViewModel
import com.nei.ichigo.core.domain.GetChampionUseCase
import com.nei.ichigo.core.model.ChampionDetail
import com.nei.ichigo.feature.encyclopedia.champion.ChampionViewModel.ChampionUiState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map

@HiltViewModel(assistedFactory = ChampionViewModel.Factory::class)
class ChampionViewModel @AssistedInject constructor(
    getChampionUseCase: GetChampionUseCase,
    @Assisted val championId: String,
) : UiStateViewModel<ChampionUiState>() {
    override val flow = getChampionUseCase(championId).map { result ->
        val page = result.getOrThrow()
        ChampionUiState(
            champion = page.data,
            version = page.version,
        )
    }

    data class ChampionUiState(
        val champion: ChampionDetail,
        override val version: String,
    ) : PageUiState

    @AssistedFactory
    interface Factory {
        fun create(championId: String): ChampionViewModel
    }
}