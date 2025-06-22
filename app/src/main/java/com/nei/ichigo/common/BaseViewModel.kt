package com.nei.ichigo.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nei.ichigo.core.data.model.Page
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

abstract class BaseViewModel<T, UiStateType>(flow: Flow<Result<Page<T>>>) : ViewModel() {

    abstract fun mapper(page: Page<T>): UiStateType

    val uiState: StateFlow<UiState<out UiStateType>> = flow
        .map<Result<Page<T>>, UiState<out UiStateType>> { result: Result<Page<T>> ->
            result.fold(
                onSuccess = {
                    UiState.Success(mapper(it))
                },
                onFailure = {
                    it.printStackTrace()
                    UiState.Error
                }
            )
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
