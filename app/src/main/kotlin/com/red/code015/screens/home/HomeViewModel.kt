package com.red.code015.screens.home

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.red.code015.domain.SummonerSummary
import com.red.code015.screens.Destination
import com.red.code015.usecases.SummonerByNameUserCase
import com.red.code015.utils.SettingsPref
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val summonerByNameUserCase: SummonerByNameUserCase,
    application: Application,
) : AndroidViewModel(application) {

    val nav = MutableLiveData<Destination>()

    private val _event = MutableLiveData<State>()
    val event = _event

    private val disposable = CompositeDisposable()
    private val _mySummoner = MutableLiveData<SummonerSummary?>()

    init {
        val pref: SharedPreferences =
            application.getSharedPreferences(SettingsPref.NAME, Context.MODE_PRIVATE)
        val nameSummoner = pref.getString(SettingsPref.KEY_SUMMONER, null)
        if (nameSummoner != null) {
            summonerByNameUserCase.invoke(nameSummoner)
                .doOnSubscribe {
                    _event.value = State.Loading
                }
                .subscribe(
                    {
                        _event.postValue(State.SummonerFound(it))
                    }, {
                        // TO DO: Show error
                        _event.postValue(State.SummonerNotFound(it, nameSummoner))
                    }
                ).let { disposable.add(it) }
        } else {
            _event.postValue(State.UnregisteredSummoner)
        }
    }

    fun summonerByName(name: String): MutableLiveData<SummonerSummary?> {
        summonerByNameUserCase.invoke(name)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    _mySummoner.postValue(it)
                }, {
                    // TO DO: Show error
                }
            ).let { disposable.add(it) }
        return _mySummoner
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
        _mySummoner.postValue(null)
    }

    sealed class State {
        object Loading : State()
        class SummonerFound(val summonerSummary: SummonerSummary) : State()
        class SummonerNotFound(val exception: Throwable, val summonerName: String) : State()
        object UnregisteredSummoner : State()
    }
}