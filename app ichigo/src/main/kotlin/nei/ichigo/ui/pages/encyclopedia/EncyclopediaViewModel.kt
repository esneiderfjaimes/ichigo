package nei.ichigo.ui.pages.encyclopedia

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ichigo.core.api.retrofit.DataDragonService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import nei.ichigo.data.mappers.toChampListItem
import nei.ichigo.data.modeles.ChampListItem

class EncyclopediaViewModel(
    dataDragonService: DataDragonService
) : ViewModel() {

    private val _uiState: MutableStateFlow<EncyclopediaState> =
        MutableStateFlow(EncyclopediaState.Loading)
    val uiState: StateFlow<EncyclopediaState> = _uiState

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val realm = dataDragonService.realm("lan")
            _uiState.value = EncyclopediaState.Success(realm.version!!)
            val (_, _, _, data) = dataDragonService.champions(
                realm.version!!,
                realm.lenguage!!
            )
            _uiState.value = EncyclopediaState.Success(
                lastVersion = realm.version!!,
                champions = data!!.toList().map { it.second.toChampListItem() },
                footer = "v${realm.version!!} • API"
            )
        }
    }

    sealed interface EncyclopediaState {
        object Loading : EncyclopediaState
        data class Success(
            val lastVersion: String,
            val champions: List<ChampListItem> = emptyList(),
            val footer: String? = null
        ) : EncyclopediaState
    }
}