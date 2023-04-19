package nei.ichigo.ui.pages.encyclopedia

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ichigo.core.api.retrofit.DataDragonService
import ichigo.core.api.retrofit.responses.ChampionRS
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EncyclopediaViewModel(
    dataDragonService: DataDragonService
) : ViewModel() {

    private val _uiState: MutableStateFlow<EncyclopediaUiState> =
        MutableStateFlow(EncyclopediaUiState.Loading)
    val uiState: StateFlow<EncyclopediaUiState> = _uiState

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val realm = dataDragonService.realm("lan")
            _uiState.value = EncyclopediaUiState.Success(realm.version!!)
            val (_, _, _, data) = dataDragonService.champions(
                realm.version!!,
                realm.lenguage!!
            )
            _uiState.value = EncyclopediaUiState.Success(
                lastVersion = realm.version!!,
                champions = data!!.toList().map { it.second }
            )
        }
    }

    sealed interface EncyclopediaUiState {
        object Loading : EncyclopediaUiState
        data class Success(
            val lastVersion: String,
            val champions: List<ChampionRS> = emptyList()
        ) : EncyclopediaUiState
    }
}