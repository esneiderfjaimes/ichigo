package com.red.code015.ui.pages.home.screens.summoner.masteries

import android.os.Parcelable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.red.code015.data.model.MasteryUI
import com.red.code015.data.model.toListMasteryUI
import com.red.code015.domain.PlatformID
import com.red.code015.usecases.MasteriesUserCase
import com.red.code015.usecases.SummonerBySummonerNameUserCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize


@OptIn(InternalCoroutinesApi::class)
class MasteriesViewModel constructor(
    private val bySummonerName: SummonerBySummonerNameUserCase,
    private val masteriesUserCase: MasteriesUserCase,
) : ViewModel() {
    //region Fields

    private val _state = MutableStateFlow(State())
    val state: StateFlow<State> get() = _state

    private val isRefreshing = MutableStateFlow(false)
    private val isLoading = MutableStateFlow(true)
    private val exception = MutableStateFlow<Throwable?>(null)
    private val masteries = MutableStateFlow<List<MasteryUI>>(listOf())

    // endregion
    // region Override Methods & Callbacks

    init {
        viewModelScope.launch {
            combine(isRefreshing,
                exception,
                isLoading,
                masteries) { isRefreshing, exception, isLoading, masteries ->
                State(isRefreshing = isRefreshing,
                    throwable = exception,
                    isLoading = isLoading,
                    masteries = masteries
                )
            }.catch { throwable ->
                throw throwable
            }.collect {
                _state.value = it
            }
        }
    }

    // endregion
    // region Public Methods

    fun setup(platformID: PlatformID, name: String) {
        loadSummonerByName(platformID, name)
    }

    // endregion
    // region Private Methods

    private fun loadSummonerByName(
        platformID: PlatformID,
        name: String,
        forceFetch: Boolean = false,
    ) {
        if (!forceFetch) isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            bySummonerName.invoke(platformID, name, forceFetch).catch {
                exception.value = it
            }.collect { summoner ->
                if (isRefreshing.value) isRefreshing.value = false
                if (isLoading.value) isLoading.value = false
                masteriesUserCase.invoke(platformID, summoner.id).catch {
                    exception.value = it
                }.collect { list ->
                    masteries.value = list.toListMasteryUI()
                }
            }
        }
    }

    // endregion
    // region Inner, Data and Sealed Classes & Interfaces

    data class State(
        val isRefreshing: Boolean = false,
        val isLoading: Boolean = false,
        val throwable: Throwable? = null,
        val masteries: List<MasteryUI> = listOf(),
    )

    @Parcelize
    data class Filters(
        val championLevel: Int? = null,
        val chestGranted: Boolean? = null,
        val search: String = "",
        val sortBy: MasteryUI.SortBy = MasteryUI.SortBy.Points,
        var isReverse: Boolean = MasteryUI.SortBy.Points.defaultIsReverse,
    ) : Parcelable {
        @IgnoredOnParcel
        val isFiltering = championLevel != null || chestGranted != null

        fun predicate(mastery: MasteryUI): Boolean =
            (championLevel == null || mastery.championLevel == championLevel) &&
                    (chestGranted == null || mastery.chestGranted == chestGranted) &&
                    (search.isBlank() || mastery.champListItem.name.contains(search, true))

    }

    enum class OrderBy { ASC, DESC }

    // endregion
}