package com.nei.ichigo.feature.encyclopedia.items

import androidx.core.text.HtmlCompat
import com.nei.ichigo.common.PageUiState
import com.nei.ichigo.common.UiStateViewModel
import com.nei.ichigo.core.data.model.ItemsPage
import com.nei.ichigo.core.domain.GetItemsUseCase
import com.nei.ichigo.feature.encyclopedia.items.ItemsViewModel.ItemsUiState
import com.nei.ichigo.feature.encyclopedia.items.ItemsViewModel.ItemsUiState.ItemUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ItemsViewModel @Inject constructor(
    getItemsUseCase: GetItemsUseCase
) : UiStateViewModel<ItemsUiState>() {

    private val mapsFilter = MutableStateFlow<String?>("11")

    override val flow = combine(
        flow = getItemsUseCase(),
        flow2 = mapsFilter,
        transform = ::mapperResult
    )

    fun mapperResult(page: Result<ItemsPage>, mapsFilter: String?): ItemsUiState {
        val page = page.getOrThrow()
        val items = page.items
            .asSequence()
            .let { seq ->
                if (mapsFilter != null) {
                    // AND
                    // .filter { it.maps.containsAll(mapsFilter) }
                    // OR
                    seq.filter { it.maps.any { id -> id == mapsFilter } }
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
            maps = page.maps,
            mapsFilter = mapsFilter,
            version = page.version,
        )
    }

    fun onMapSelected(map: String?) {
        mapsFilter.update { map }
    }

    data class ItemsUiState(
        val itemsOrder: List<String>,
        val itemsMap: Map<String, ItemUi>,
        val maps: List<String>,
        val mapsFilter: String?,
        override val version: String,
    ) : PageUiState {
        data class ItemUi(
            val id: String,
            val name: String,
            val image: String,
            val from: List<String>,
            val into: List<String>,
        )
    }
}