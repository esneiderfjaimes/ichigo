package com.nei.ichigo.feature.encyclopedia.items

import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nei.ichigo.core.domain.GetItemsUseCase
import com.nei.ichigo.core.model.Item
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
                    val items = it.icons
                        // AND
                        // .filter { it.maps.containsAll(mapsFilter) }
                        // OR
                        .filter {
                            it.maps.any { mapId -> mapId in mapsFilter }
                                    && it.gold.total > 0
                        }
                        .map {
                            val name = if (it.name.contains("<")) {
                                HtmlCompat.fromHtml(it.name, HtmlCompat.FROM_HTML_MODE_LEGACY)
                                    .toString()
                            } else {
                                it.name
                            }
                            it.copy(name = name)
                        }
                        .sortedBy { it.gold.total }
                    //.sortedBy { it.name }

                    val itemsOrder = items.map { it.id }
                    val itemsMap = items.associateBy { it.id }

                    ItemsUiState.Success(
                        version = it.version,
                        lang = it.lang,
                        itemsOrder = itemsOrder,
                        itemsMap = itemsMap
                    )
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

    sealed interface ItemsUiState {
        object Loading : ItemsUiState
        data class Success(
            val version: String,
            val lang: String,
            val itemsOrder: List<String>,
            val itemsMap: Map<String, Item>,
        ) : ItemsUiState
    }

}