package com.nei.ichigo.feature.encyclopedia.champion

import androidx.compose.runtime.Immutable
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
    @Assisted val championId: String,
    private val getChampionUseCase: GetChampionUseCase,
) : UiStateViewModel<ChampionUiState>() {

    override fun getFlow() = getChampionUseCase(championId).map { result ->
        val page = result.getOrThrow()
        ChampionUiState(
            champion = page.data,
            version = page.version,
        )
    }

    @Immutable
    data class ChampionUiState(
        val champion: ChampionDetail,
        override val version: String,
    ) : PageUiState

    @AssistedFactory
    interface Factory {
        fun create(championId: String): ChampionViewModel
    }
}