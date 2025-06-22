package com.nei.ichigo.feature.encyclopedia.spells

import com.nei.ichigo.common.BaseViewModel
import com.nei.ichigo.common.PageUI
import com.nei.ichigo.core.data.model.Page
import com.nei.ichigo.core.domain.GetSpellsUseCase
import com.nei.ichigo.core.model.Spell
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SpellsViewModel @Inject constructor(
    getSpellsUseCase: GetSpellsUseCase,
) : BaseViewModel<Spell, SpellsViewModel.SpellsUiState>(getSpellsUseCase()) {
    private val modes = setOf("ARAM", "CLASSIC")

    override fun mapper(page: Page<Spell>): SpellsUiState {
        val spells = page.data.asSequence()
            .let {
                if (modes.isNotEmpty()) {
                    it.filter { it.modes.any { mode -> mode in modes } }
                } else it
            }
            .sortedWith(compareBy<Spell> { it.summonerLevel }.thenBy { it.name })
            .toList()
        return SpellsUiState(
            version = page.version,
            lang = page.lang,
            spells = spells,
        )
    }

    data class SpellsUiState(
        val spells: List<Spell>,
        override val version: String,
        override val lang: String,
    ) : PageUI
}