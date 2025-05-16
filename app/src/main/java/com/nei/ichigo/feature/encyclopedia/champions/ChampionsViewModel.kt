package com.nei.ichigo.feature.encyclopedia.champions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nei.ichigo.core.domain.GetChampionsUseCase
import com.nei.ichigo.core.model.Champion
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

@HiltViewModel
class ChampionsViewModel @Inject constructor(
    getChampionsUseCase: GetChampionsUseCase
) : ViewModel() {
    private val tagSelected = MutableStateFlow<String?>(null)

    val uiState: StateFlow<ChampionsUiState> =
        combine(getChampionsUseCase(), tagSelected) { page, tagSelected ->
            val (version, lang, champions) = page.getOrElse {
                it.printStackTrace()
                return@combine ChampionsUiState.Error
            }
            val sortedUniqueTags = champions
                .flatMap { it.tags }
                .distinct()
                .sorted()

            val championsFiltered = if (tagSelected != null) {
                champions.filter { it.tags.contains(tagSelected) }
            } else {
                champions
            }

            ChampionsUiState.Success(
                version = version,
                lang = lang,
                champions = championsFiltered,
                tagSelected = tagSelected,
                tags = sortedUniqueTags
            )
        }.catch {
            it.printStackTrace()
            emit(ChampionsUiState.Error)
        }.flowOn(Dispatchers.IO).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ChampionsUiState.Loading,
        )

    fun onTagSelected(tagSelected: String?) {
        this.tagSelected.update { tagSelected }
    }

    sealed interface ChampionsUiState {
        data object Loading : ChampionsUiState
        data class Success(
            val version: String,
            val lang: String,
            val champions: List<Champion>,
            val tagSelected: String? = null,
            val tags: List<String>
        ) : ChampionsUiState

        data object Error : ChampionsUiState
    }
}