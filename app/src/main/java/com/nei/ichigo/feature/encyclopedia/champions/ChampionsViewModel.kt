package com.nei.ichigo.feature.encyclopedia.champions

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nei.ichigo.core.data.model.ChampionsPage
import com.nei.ichigo.core.data.repository.ChampionsRepository
import com.nei.ichigo.core.model.Champion
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ChampionsViewModel @Inject constructor(
    private val repository: ChampionsRepository
) : ViewModel() {
    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getVersions().let {
                Log.i("ChampionsViewModel", ": versions: $it")
            }
        }
    }

    private val tagSelected = MutableStateFlow<String?>(null)

    private val pageFlow = flow<ChampionsPage?> {
        val page = repository.getChampionsPage()
        emit(page)
    }.catch {
        Log.e("ChampionsViewModel", ": ", it)
        emit(null)
    }

    val uiState: StateFlow<ChampionsUiState> =
        combine(pageFlow, tagSelected) { page, tagSelected ->
            val (version, lang, champions) = page ?: return@combine ChampionsUiState.Error
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
            Log.e("ChampionsViewModel", ": ", it)
            ChampionsUiState.Error
        }.stateIn(
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