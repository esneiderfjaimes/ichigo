package com.red.code015.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.red.code015.domain.Profile
import com.red.code015.data.model.Platform
import com.red.code015.data.model.SummonerUI
import com.red.code015.data.model.toUI
import com.red.code015.ui.screens.home.HomeViewModel.State.CardMySummoner
import com.red.code015.usecases.HelperRepository
import com.red.code015.usecases.SummonerByPuuIDUserCase
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
class HomeViewModel @Inject constructor(
    private val helperRepository: HelperRepository,
    private val byPuuID: SummonerByPuuIDUserCase,
) : ViewModel() {
    //region Fields

    private val _state = MutableStateFlow(State())
    val state: StateFlow<State> get() = _state

    private val isRefreshing = MutableStateFlow(false)
    private val cardMySummoner = MutableStateFlow<CardMySummoner>(CardMySummoner.Loading)

    // endregion
    // region Override Methods & Callbacks

    init {
        viewModelScope.launch {
            combine(isRefreshing, cardMySummoner) { isRefreshing, cardMySummoner ->
                State(isRefreshing, cardMySummoner)
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
        if (puuID != null) loadSummonerByPuuID(puuID)
    }

    fun refresh(profile: Profile?) {
        isRefreshing.value = true
        if (profile != null) loadSummonerByPuuID(profile.puuID, true)
    }

    // endregion
    // region Private Methods

    private fun loadSummonerByPuuID(puuID: String, forceFetch: Boolean = false) {
        if (!forceFetch) cardMySummoner.value = CardMySummoner.Loading
        viewModelScope.launch(Dispatchers.IO) {
            byPuuID.invoke(puuID, forceFetch).catch {
                // TODO: emit a UI error here. For now we'll just rethrow
            }.collect {
                if (isRefreshing.value) isRefreshing.value = false
                cardMySummoner.value = CardMySummoner.Show(
                    isLoading = false,
                    summonerUI = it.toUI()
                )
            }
        }
    }

    // endregion
    // region Inner, Data and Sealed Classes & Interfaces

    data class State(
        val isRefreshing: Boolean = false,
        val cardMySummoner: CardMySummoner = CardMySummoner.Loading,
    ) {
        sealed class CardMySummoner {
            object Loading : CardMySummoner()
            class Show(
                val isLoading: Boolean = false,
                val summonerUI: SummonerUI,
            ) : CardMySummoner()
        }
    }

    // endregion
}