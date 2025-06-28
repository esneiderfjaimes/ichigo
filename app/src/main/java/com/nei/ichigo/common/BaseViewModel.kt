package com.nei.ichigo.common

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

abstract class BaseViewModel<T, UiStateType>() : ViewModel() {
    init {
        Log.d("BaseViewModel", "init")
    }

    abstract val flow: Flow<T>

    abstract fun mapper(data: T): UiStateType

    val uiState: StateFlow<UiState<out UiStateType>> by lazy {
        flow
            .map<T, UiState<out UiStateType>> { data: T ->
                val uiState = mapper(data)
                Log.d("BaseViewModel", "uiState: $uiState")
                UiState.Success(uiState)
            }.catch {
                it.printStackTrace()
                UiState.Error
            }
            .flowOn(Dispatchers.IO)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                UiState.Loading
            )
    }
}

abstract class UiStateViewModel<UiStateType>() : BaseViewModel<UiStateType, UiStateType>() {

    override fun mapper(data: UiStateType) = data

}