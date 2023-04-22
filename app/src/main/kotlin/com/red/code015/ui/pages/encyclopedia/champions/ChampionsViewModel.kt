package com.red.code015.ui.pages.encyclopedia.champions

import android.os.Parcelable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.red.code015.domain.ChampListItem
import com.red.code015.domain.ChampionsRotation
import com.red.code015.domain.RotationChamp
import com.red.code015.usecases.ChampionsRotationsUserCase
import com.red.code015.usecases.EncyclopediaChampionUserCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize


@OptIn(InternalCoroutinesApi::class)
class ChampionsViewModel constructor(
    private val encyclopediaChampion: EncyclopediaChampionUserCase,
    private val championsRotation: ChampionsRotationsUserCase,
) : ViewModel() {

    //region Fields

    private lateinit var latestVersion: String

    private val _state = MutableStateFlow(State())
    val state: StateFlow<State> get() = _state

    private val champions = MutableStateFlow<List<ChampListItem>>(listOf())
    private val tags = MutableStateFlow<List<String>>(listOf())
    private val footer = MutableStateFlow<String?>(null)

    // endregion
    // region Override Methods & Callbacks

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                combine(champions, tags, footer) { champions, tags, footer ->
                    State(champions, tags, footer)
                }.flowOn(Dispatchers.IO).catch { throw it }.collect {
                    _state.value = it
                }
            }
        }
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                encyclopediaChampion.invoke().catch { it.printStackTrace() }.collect {
                    champions.value = it.data
                    generateFilters(champions.value)
                    footer.value =
                        "v${it.version} • ${it.dataSource.dataSources.name.substring(0..2)}"
                }
                championsRotation.invoke().catch { it.printStackTrace() }.collect { it.load() }
            }
        }
    }

    fun setup(latestVersion: String) {
        this.latestVersion = latestVersion
    }

    // endregion
    // region Public Methods

    // endregion
    // region Private Methods

    private suspend fun generateFilters(champions: List<ChampListItem>) {
        withContext(Dispatchers.IO) {
            val filtersByRol = mutableListOf<String>()
            champions.forEach { champ ->
                champ.tags.forEach { tag ->
                    filtersByRol.add(tag)
                }
            }
            tags.value = filtersByRol.distinct().toList().sortedBy { it }
        }
    }

    private suspend fun ChampionsRotation.load() {
        withContext(Dispatchers.IO) {
            val champs = champions.value.toMutableList()
            champs.forEachIndexed { index, champ ->
                val isFree = freeChampionIds.contains(champ.key)
                val isFreeForNewPlayers = freeChampionIdsForNewPlayers.contains(champ.key)
                when {
                    isFree && isFreeForNewPlayers ->
                        champs[index] = champ.copy(rotation = RotationChamp.Both)
                    isFree ->
                        champs[index] = champ.copy(rotation = RotationChamp.Free)
                    isFreeForNewPlayers ->
                        champs[index] = champ.copy(rotation = RotationChamp.FreeForNewPlayers)
                }
            }
            champions.value = champs.toList()
        }
    }

    // endregion
    // region Inner, Data and Sealed Classes & Interfaces

    data class State(
        val list: List<ChampListItem> = listOf(),
        val filterByTags: List<String> = listOf(),
        val footer: String? = null,
    )

    @Parcelize
    data class Filters(
        val tag: String? = null,
        val rotation: RotationChamp? = null,
    ) : Parcelable {
        @IgnoredOnParcel
        val isFiltering = tag != null || rotation != null

        fun predicate(champ: ChampListItem): Boolean =
            (tag == null || champ.tags.contains(tag)) &&
                    (rotation == null || champ.rotation == rotation)

    }

    // endregion

}