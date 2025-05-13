package com.red.code015.ui.pages.home.screens.summoner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.red.code015.data.model.MasteryUI
import com.red.code015.data.model.SummonerUI
import com.red.code015.data.model.toListMasteryUI
import com.red.code015.data.model.toUI
import com.red.code015.domain.PlatformID
import com.red.code015.usecases.MasteriesUserCase
import com.red.code015.usecases.SummonerBySummonerNameUserCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@OptIn(InternalCoroutinesApi::class)
class SummonerViewModel @Inject constructor(
    private val bySummonerName: SummonerBySummonerNameUserCase,
    private val masteriesUserCase: MasteriesUserCase,
) : ViewModel() {
    //region Fields

    private val _state = MutableStateFlow(State())
    val state: StateFlow<State> get() = _state

    private val isRefreshing = MutableStateFlow(false)
    private val summoner = MutableStateFlow<State.SectionSummoner>(State.SectionSummoner.Loading)
    private val exception = MutableStateFlow<Throwable?>(null)
    private val masteries = MutableStateFlow<List<MasteryUI>>(listOf())

    // endregion
    // region Override Methods & Callbacks

    init {
        viewModelScope.launch {
            combine(isRefreshing,
                exception,
                summoner,
                masteries) { isRefreshing, exception, summoner, masteries ->
                State(isRefreshing = isRefreshing,
                    throwable = exception,
                    sectionSummoner = summoner,
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
        if (!forceFetch) summoner.value = State.SectionSummoner.Loading
        viewModelScope.launch(Dispatchers.IO) {
            bySummonerName.invoke(platformID, name, forceFetch).catch {
                exception.value = it
            }.collect { summoner ->
                if (isRefreshing.value) isRefreshing.value = false
                this@SummonerViewModel.summoner.value = State.SectionSummoner.Show(
                    summoner = summoner.toUI()
                )
                masteriesUserCase.invoke(platformID, summoner.id, 3).catch {
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
        val throwable: Throwable? = null,
        val sectionSummoner: SectionSummoner = SectionSummoner.Loading,
        val masteries: List<MasteryUI> = listOf(),
    ) {
        sealed class SectionSummoner {
            object Loading : SectionSummoner()
            class Show(
                val summoner: SummonerUI,
            ) : SectionSummoner()
        }
    }

    // endregion
}