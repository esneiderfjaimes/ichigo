package com.red.code015.ui.pages.home.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.red.code015.data.ForbiddenException
import com.red.code015.data.model.Platform
import com.red.code015.data.model.SummonerSummaryUI
import com.red.code015.data.model.toSummaryUI
import com.red.code015.domain.Profile
import com.red.code015.ui.pages.home.screens.home.HomeViewModel.State.CardMySummoner
import com.red.code015.usecases.HelperRepository
import com.red.code015.usecases.SummonerByPuuIDUserCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch


@OptIn(InternalCoroutinesApi::class)
class HomeViewModel constructor(
    private val helperRepository: HelperRepository,
    private val byPuuID: SummonerByPuuIDUserCase,
) : ViewModel() {
    //region Fields

    private val _state = MutableStateFlow(State())
    val state: StateFlow<State> get() = _state

    private val isRefreshing = MutableStateFlow(false)
    private val exception = MutableStateFlow<String?>(null)
    private val cardMySummoner = MutableStateFlow<CardMySummoner>(CardMySummoner.Loading)

    // endregion
    // region Override Methods & Callbacks

    init {
        viewModelScope.launch {
            combine(isRefreshing, exception, cardMySummoner) { isRefreshing, e, cardMySummoner ->
                State(isRefreshing, e, cardMySummoner)
            }.catch { throwable ->
                throw throwable
            }.collect {
                _state.value = it
            }
        }
    }

    // endregion
    // region Public Methods

    fun setup(platform: Platform, puuID: String?) {
        helperRepository.updateHost(platform.id)
        exception.value = null
        if (puuID != null) loadSummonerByPuuID(puuID)
    }

    fun refresh(profile: Profile?) {
        if (profile != null) {
            isRefreshing.value = true
            loadSummonerByPuuID(profile.puuID, true)
        }
    }

    // endregion
    // region Private Methods

    private fun loadSummonerByPuuID(puuID: String, forceFetch: Boolean = false) {
        if (!forceFetch) cardMySummoner.value = CardMySummoner.Loading
        viewModelScope.launch(Dispatchers.IO) {
            byPuuID.invoke(puuID, forceFetch).catch {
                isRefreshing.value = false
                if (it is ForbiddenException)
                    exception.value = it.message
            }.collect {
                if (isRefreshing.value) isRefreshing.value = false
                cardMySummoner.value = CardMySummoner.Show(
                    isLoading = false,
                    summonerUI = it.toSummaryUI()
                )
            }
        }
    }

    // endregion
    // region Inner, Data and Sealed Classes & Interfaces

    data class State(
        val isRefreshing: Boolean = false,
        val exception: String? = null,
        val cardMySummoner: CardMySummoner = CardMySummoner.Loading,
    ) {
        sealed class CardMySummoner {
            object Loading : CardMySummoner()
            class Show(
                val isLoading: Boolean = false,
                val summonerUI: SummonerSummaryUI,
            ) : CardMySummoner()
        }
    }

    // endregion
}