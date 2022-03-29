package com.red.code015.screens.register

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.red.code015.domain.SummonerSummary
import com.red.code015.usecases.SummonerByNameUserCase
import com.red.code015.utils.SettingsPref
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val summonerByNameUserCase: SummonerByNameUserCase,
    application: Application,
) : AndroidViewModel(application) {

    val pref: SharedPreferences =
        application.getSharedPreferences(SettingsPref.NAME, Context.MODE_PRIVATE)

    private val disposable = CompositeDisposable()

    private val _event = MutableLiveData<Event>()
    val event = _event

    fun summonerByName(name: String) {
        summonerByNameUserCase.invoke(name)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    _event.postValue(Event.SummonerFound(it))
                }, {
                    // TO DO: Show error
                    _event.postValue(Event.SummonerNotFound(it))
                }
            ).let { disposable.add(it) }
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

    fun saveSummonerName(name: String?) {
        with(pref.edit()) {
            putString(SettingsPref.KEY_SUMMONER, name)
            apply()
        }
    }

    sealed class Event {
        class SummonerFound(val summonerSummary: SummonerSummary) : Event()
        class SummonerNotFound(val exception: Throwable) : Event()
    }
}