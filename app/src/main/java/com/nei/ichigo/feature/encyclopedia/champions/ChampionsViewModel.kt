package com.nei.ichigo.feature.encyclopedia.champions

import com.nei.ichigo.common.Base2ViewModel
import com.nei.ichigo.common.PageUiState
import com.nei.ichigo.core.domain.GetChampionsUseCase
import com.nei.ichigo.core.model.Champion
import com.nei.ichigo.feature.encyclopedia.champions.ChampionsViewModel.ChampionsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update

@HiltViewModel
class ChampionsViewModel @Inject constructor(
    getChampionsUseCase: GetChampionsUseCase
) : Base2ViewModel<ChampionsUiState>() {

    private val _tagSelected = MutableStateFlow<String?>(null)

    override val flow = combine(
        getChampionsUseCase(),
        _tagSelected
    ) { pageResult, tagSelected ->
        val page = pageResult.getOrThrow()
        val (version, lang, champions) = page
        val sortedUniqueTags = champions
            .flatMap { it.tags }
            .distinct()
            .sorted()

        val championsFiltered = if (tagSelected != null) {
            champions.filter { it.tags.contains(tagSelected) }
        } else {
            champions
        }

        ChampionsUiState(
            version = version,
            champions = championsFiltered,
            tagSelected = tagSelected,
            tags = sortedUniqueTags
        )
    }

    fun onTagSelected(tagSelected: String?) {
        _tagSelected.update { tagSelected }
    }

    data class ChampionsUiState(
        override val version: String,
        val champions: List<Champion>,
        val tagSelected: String? = null,
        val tags: List<String>
    ) : PageUiState
}