package com.nei.ichigo.feature.encyclopedia.skin.fullscreen

import com.nei.ichigo.common.PageUiState
import com.nei.ichigo.common.UiStateViewModel
import com.nei.ichigo.core.domain.GetChampionUseCase
import com.nei.ichigo.core.model.Skin
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import javax.inject.Named

@HiltViewModel(assistedFactory = SkinFullscreenViewModel.Factory::class)
class SkinFullscreenViewModel @AssistedInject constructor(
    getChampionUseCase: GetChampionUseCase,
    @Assisted args: Factory.Args
) : UiStateViewModel<SkinFullscreenViewModel.SkinFullscreenUiState>() {

    private val _selectedSkin = MutableStateFlow(
        DEFAULT_SELECTED_SKIN
    )

    override val flow = combine(
        flow = getChampionUseCase(args.championId),
        flow2 = _selectedSkin
    ) { result, selectedSkin ->
        val page = result.getOrThrow()
        val skins = page.data.skins
        val selectedSkinIndex = if (selectedSkin == DEFAULT_SELECTED_SKIN) {
            val index = skins.indexOfFirst { it.id == args.selectedSkinId }
            if (index < 0) 0 else index
        } else {
            selectedSkin
        }

        val skin = skins[selectedSkinIndex]
        SkinFullscreenUiState(
            skin = skin,
            selectedSkinIndex = selectedSkinIndex,
            skins = skins,
            championId = args.championId,
            version = page.version,
        )
    }.catch {
        it.printStackTrace()
    }

    fun updateSelectedSkin(skinId: Int) {
        _selectedSkin.update { skinId }
    }

    data class SkinFullscreenUiState(
        val skins: List<Skin>,
        val selectedSkinIndex: Int,
        val skin: Skin,
        val championId: String,
        override val version: String,
    ) : PageUiState

    @AssistedFactory
    interface Factory {
        fun create(
            args: Args
        ): SkinFullscreenViewModel

        data class Args(
            @param:Named("championId")
            val championId: String,
            @param:Named("selectedSkinId")
            val selectedSkinId: String?
        )
    }

    companion object {
        private const val DEFAULT_SELECTED_SKIN = -1
    }
}