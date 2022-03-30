package com.red.code015.screens.home

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.red.code015.domain.SummonerSummary
import com.red.code015.screens.Destination
import com.red.code015.usecases.SummonerByNameUserCase
import com.red.code015.utils.SettingsPref
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val summonerByNameUserCase: SummonerByNameUserCase,
    application: Application,
) : AndroidViewModel(application) {

    private val pref: SharedPreferences =
        application.getSharedPreferences(SettingsPref.NAME, Context.MODE_PRIVATE)
    private val disposable = CompositeDisposable()

    private val _event = MutableLiveData<State>()
    val event = _event
    val nav = MutableLiveData<Destination>()

    init {
        pref.getString(SettingsPref.KEY_SUMMONER, null)?.let { summonerName ->
            summonerByName(summonerName)
        } ?: _event.postValue(State.UnregisteredSummoner)
    }

    private fun summonerByName(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            summonerByNameUserCase.invoke(name)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .doOnSubscribe {
                    viewModelScope.launch(Dispatchers.Main) {
                        _event.value = State.Loading
                    }
                }
                .subscribe({
                    viewModelScope.launch(Dispatchers.Main) {
                        _event.postValue(State.SummonerFound(it))
                    }
                }, {
                    viewModelScope.launch(Dispatchers.Main) {
                        it.printStackTrace()
                        _event.postValue(State.SummonerNotFound(it, name))
                    }
                }).let { disposable.add(it) }
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

    fun registerSummoner() {
        nav.value = Destination.Register
    }

    fun summonerDetail() {
        nav.value = Destination.Summoner
    }

    fun removeSummoner() {
        with(pref.edit()) {
            remove(SettingsPref.KEY_SUMMONER)
            apply()
        }
        _event.postValue(State.UnregisteredSummoner)
    }

    sealed class State {
        object Loading : State()
        class SummonerFound(val summonerSummary: SummonerSummary) : State()
        class SummonerNotFound(val exception: Throwable, val summonerName: String) : State()
        object UnregisteredSummoner : State()
    }
}