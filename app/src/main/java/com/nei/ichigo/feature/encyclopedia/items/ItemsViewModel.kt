package com.nei.ichigo.feature.encyclopedia.items

import androidx.core.text.HtmlCompat
import com.nei.ichigo.common.BaseViewModel
import com.nei.ichigo.common.PageUI
import com.nei.ichigo.core.data.model.ItemsPage
import com.nei.ichigo.core.domain.GetItemsUseCase
import com.nei.ichigo.feature.encyclopedia.items.ItemsViewModel.ItemsUiState
import com.nei.ichigo.feature.encyclopedia.items.ItemsViewModel.ItemsUiState.ItemUi
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ItemsViewModel @Inject constructor(
    getItemsUseCase: GetItemsUseCase
) : BaseViewModel<ItemsPage, ItemsUiState>(getItemsUseCase()) {

    val mapsFilter = setOf<String>("11")

    override fun mapper(page: ItemsPage): ItemsUiState {
        val items = page.icons
            .asSequence()
            .let { seq ->
                if (mapsFilter.isNotEmpty()) {
                    // AND
                    // .filter { it.maps.containsAll(mapsFilter) }
                    // OR
                    seq.filter { it.maps.any { id -> id in mapsFilter } }
                } else seq
            }
            .sortedBy { it.gold.total }
            .map {
                val name = if (it.name.contains("<")) {
                    HtmlCompat.fromHtml(it.name, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
                } else {
                    it.name
                }

                ItemUi(
                    id = it.id,
                    name = name,
                    image = it.image,
                    from = it.from,
                    into = it.into
                )
            }

        val itemsOrder = items.map { it.id }.toList()
        val itemsMap = items.associateBy { it.id }

        return ItemsUiState(
            itemsOrder = itemsOrder,
            itemsMap = itemsMap,
            version = page.version,
            lang = page.lang
        )
    }

    data class ItemsUiState(
        val itemsOrder: List<String>,
        val itemsMap: Map<String, ItemUi>,
        override val version: String,
        override val lang: String,
    ) : PageUI {
        data class ItemUi(
            val id: String,
            val name: String,
            val image: String,
            val from: List<String>,
            val into: List<String>,
        )
    }
}