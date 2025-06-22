package com.nei.ichigo.common

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

abstract class BaseViewModel<T, UiStateType>() : ViewModel() {
    init {
        Log.d("BaseViewModel", "init")
    }

    abstract val flow: Flow<T>

    abstract fun mapper(page: T): UiStateType

    val uiState: StateFlow<UiState<out UiStateType>> by lazy {
        flow
            .map<T, UiState<out UiStateType>> { result: T ->
                UiState.Success(mapper(result))
            }.catch {
                it.printStackTrace()
                UiState.Error
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                UiState.Loading
            )
    }
}

abstract class Base2ViewModel<UiStateType>() : BaseViewModel<UiStateType, UiStateType>() {

    override fun mapper(page: UiStateType) = page

}

abstract class BaseResultViewModel<T, UiStateType>() : BaseViewModel<Result<T>, UiStateType>() {

    abstract fun mapperResult(page: T): UiStateType

    override fun mapper(page: Result<T>) = page.fold(
        onSuccess = { mapperResult(it) },
        onFailure = { throw it }
    )
}
