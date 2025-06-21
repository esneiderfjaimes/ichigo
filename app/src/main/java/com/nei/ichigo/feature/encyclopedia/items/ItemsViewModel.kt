package com.nei.ichigo.feature.encyclopedia.items

import android.util.Log
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nei.ichigo.core.data.model.ItemsPage
import com.nei.ichigo.core.domain.GetItemsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ItemsViewModel @Inject constructor(
    getItemsUseCase: GetItemsUseCase
) : ViewModel() {

    val mapsFilter = setOf<String>("11")

    val uiState: StateFlow<ItemsUiState> = getItemsUseCase()
        .map { result ->
            result.fold(
                onSuccess = {
                    mapper(it, mapsFilter)
                },
                onFailure = {
                    it.printStackTrace()
                    ItemsUiState.Loading
                }
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            ItemsUiState.Loading
        )

    private fun mapper(page: ItemsPage, mapsFilter: Set<String>): ItemsUiState.Success {
        val items = page.icons
            .asSequence().apply {
                // AND
                // .filter { it.maps.containsAll(mapsFilter) }
                // OR
                if (mapsFilter.isNotEmpty()) {
                    filter {
                        it.maps.any { mapId -> mapId in mapsFilter }
                        //  && it.gold.total > 0
                    }
                }
                sortedBy { it.gold.total }
            }
            .map {
                val name = if (it.name.contains("<")) {
                    HtmlCompat.fromHtml(it.name, HtmlCompat.FROM_HTML_MODE_LEGACY)
                        .toString()
                } else {
                    it.name
                }
                ItemsUiState.Success.ItemUi(
                    id = it.id,
                    name = name,
                    image = it.image,
                    from = it.from,
                    into = it.into
                )
            }

        //.sortedBy { it.name }

        items.forEach {
            Log.d(it.id, it.toString())
        }

        val itemsOrder = items.map { it.id }.toList()
        val itemsMap = items.associateBy { it.id }

        return ItemsUiState.Success(
            version = page.version,
            lang = page.lang,
            itemsOrder = itemsOrder,
            itemsMap = itemsMap
        )
    }

    sealed interface ItemsUiState {
        object Loading : ItemsUiState
        data class Success(
            val version: String,
            val lang: String,
            val itemsOrder: List<String>,
            val itemsMap: Map<String, ItemUi>,
        ) : ItemsUiState {
            data class ItemUi(
                val id: String,
                val name: String,
                val image: String,
                val from: List<String>,
                val into: List<String>,
            )
        }
    }
}