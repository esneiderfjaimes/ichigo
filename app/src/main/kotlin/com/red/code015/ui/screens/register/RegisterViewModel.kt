package com.red.code015.ui.screens.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.red.code015.R
import com.red.code015.domain.Profile
import com.red.code015.data.model.Platform
import com.red.code015.ui.screens.register.RegisterViewModel.State.SearchBy
import com.red.code015.usecases.HelperRepository
import com.red.code015.usecases.ProfileByRiotIDUserCase
import com.red.code015.usecases.ProfileBySummonerNameUserCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val helperRepository: HelperRepository,
    private val byRiotID: ProfileByRiotIDUserCase,
    private val bySummonerName: ProfileBySummonerNameUserCase,
) : ViewModel() {
    //region Fields

    private val _state = MutableStateFlow(State())
    val state: StateFlow<State> get() = _state

    private val searchBy = MutableStateFlow(SearchBy.SummonerName)
    private val isLoading = MutableStateFlow(false)

    private val _event = MutableLiveData<Event>()
    val event: LiveData<Event> get() = _event

    // endregion
    // region Override Methods & Callbacks

    init {
        viewModelScope.launch {
            combine(searchBy, isLoading) { searchBy, loading ->
                State(searchBy, loading)
            }.catch { throwable ->
                throw throwable
            }.collect {
                _state.value = it
            }
        }
    }

    // endregion
    // region Public Methods

    fun setup(platform: Platform) {
        helperRepository.updateHost(platform.id)
    }

    fun setLoading(isLoading: Boolean) {
        this.isLoading.value = isLoading
    }

    fun setSearchBy(searchBy: SearchBy) {
        this.searchBy.value = searchBy
    }

    fun searchProfileBySummonerName(summonerName: String) {
        processSearch(bySummonerName.invoke(summonerName))
    }

    fun searchProfileByRiotID(gameName: String, tagline: String) {
        processSearch(byRiotID.invoke(gameName, tagline))
    }

    // endregion
    // region Private Methods

    private fun processSearch(flowProfile: Flow<Profile>) {
        isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            flowProfile.catch { exception ->
                _event.postValue(Event.Error(exception))
            }.firstOrNull()?.let {
                _event.postValue(Event.ProfileFound(it))
            } ?: _event.postValue(Event.ProfileNotFound)
        }
    }

    // endregion
    // region Inner, Data and Sealed Classes & Interfaces

    data class State(
        val searchBy: SearchBy = SearchBy.SummonerName,
        val isLoading: Boolean = false,
    ) {
        enum class SearchBy(val idRes: Int) { SummonerName(R.string.summoner_name), RiotID((R.string.riot_id)) }
    }

    sealed class Event {
        class ProfileFound(val profile: Profile) : Event()
        object ProfileNotFound : Event()
        class Error(val exception: Throwable) : Event()
    }

    // endregion
}