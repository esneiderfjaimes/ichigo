package com.nei.ichigo.feature.encyclopedia.spells

import com.nei.ichigo.common.PageUiState
import com.nei.ichigo.common.UiStateViewModel
import com.nei.ichigo.common.utils.and
import com.nei.ichigo.core.domain.GetSpellsUseCase
import com.nei.ichigo.core.model.Spell
import com.nei.ichigo.feature.encyclopedia.spells.SpellsViewModel.SpellsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SpellsViewModel @Inject constructor(
    getSpellsUseCase: GetSpellsUseCase,
) : UiStateViewModel<SpellsUiState>() {

    private val modes = MutableStateFlow(setOf("ARAM", "CLASSIC"))

    override val flow = combine(
        flow = getSpellsUseCase().map { pageResult ->
            val page = pageResult.getOrThrow()
            val spells = page.data.asSequence()
                .sortedWith(compareBy<Spell> { it.summonerLevel }.thenBy { it.name })

            val modesAvailable = spells
                .map { it.modes }
                .flatten()
                .distinct()
                .sorted()

            spells.toList() to modesAvailable.toList() and page.version
        },
        flow2 = modes
    ) { (spells, modesAvailable, version), modes ->
        val spells = if (modes.isNotEmpty()) {
            spells.filter { it.modes.any { mode -> mode in modes } }
        } else spells

        SpellsUiState(
            spells = spells,
            modesAvailable = modesAvailable,
            filteredModes = modes,
            version = version,
        )
    }

    fun onTagSelected(mode: String?) {
        val mode = mode?.uppercase()
        modes.update { modes ->
            when {
                mode == null -> emptySet()
                modes.contains(mode) -> modes - mode
                else -> modes + mode
            }
        }
    }

    data class SpellsUiState(
        val spells: List<Spell>,
        val modesAvailable: List<String>,
        val filteredModes: Set<String>,
        override val version: String,
    ) : PageUiState
}