package com.nei.ichigo.common

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nei.ichigo.common.refresh.Refresh
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

abstract class UiStateViewModel2<UiStateType> : ViewModel() {

    abstract val flow: Flow<UiStateType>

    open val dispatcher = Dispatchers.IO
    open val sharingStarted = SharingStarted.WhileSubscribed(5_000)

    val uiState: StateFlow<UiState<out UiStateType>> by lazy {
        flow
            .map<UiStateType, UiState<out UiStateType>> { uiState: UiStateType ->
                Log.d("BaseViewModel", "uiState: $uiState")
                UiState.Success(uiState)
            }.catch { e ->
                emit(UiState.Error(0))
            }
            .flowOn(dispatcher)
            .stateIn(
                viewModelScope,
                sharingStarted,
                UiState.Loading
            )
    }
}

abstract class UiStateViewModel<UiStateType> : ViewModel() {

    abstract val flow: Flow<UiStateType>

    open val dispatcher = Dispatchers.IO
    open val sharingStarted = SharingStarted.WhileSubscribed(5_000)

    val refresh = Refresh(
        errorMapper = { 0 },
        upstream = { flow }
    )

    val uiState: StateFlow<UiState<UiStateType>> by lazy {
        refresh.flow
            .flowOn(dispatcher)
            .stateIn(
                viewModelScope,
                sharingStarted,
                UiState.Loading
            )
    }
}