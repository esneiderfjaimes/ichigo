package com.nei.ichigo.common

import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nei.ichigo.common.refresh.Refresh
import com.nei.ichigo.core.designsystem.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

typealias UiStateViewModel<T> = UiStateRefreshViewModel<T>

abstract class UiStateLegacyViewModel<T> : ViewModel() {

    abstract fun getFlow(): Flow<T>

    open val dispatcher = Dispatchers.IO
    open val sharingStarted = SharingStarted.WhileSubscribed(5_000)

    val uiState: StateFlow<UiState<T>> by lazy {
        getFlow()
            .map<T, UiState<T>> { uiState: T ->
                Log.d("BaseViewModel", "uiState: $uiState")
                UiState.Success(uiState)
            }.catch { e ->
                emit(UiState.Error(R.string.core_designsystemy_generic_error, e))
            }
            .flowOn(dispatcher)
            .stateIn(
                viewModelScope,
                sharingStarted,
                UiState.Loading
            )
    }

    fun onRefresh() {

    }
}

abstract class UiStateSplitViewModel<T> : ViewModel() {

    abstract fun getFlow(): Flow<T>

    open val dispatcher = Dispatchers.IO

    private val _uiState = MutableStateFlow<UiState<T>>(UiState.Loading)
    val uiState: StateFlow<UiState<T>> = _uiState

    init {
        observe()
    }

    private fun observe() {
        viewModelScope.launch(dispatcher) {
            getFlow()
                .map<T, UiState<T>> { data ->
                    Log.d("BaseViewModel", "uiState: $data")
                    UiState.Success(data)
                }
                .catch { e ->
                    Log.e("BaseViewModel", "error: ${e.message}")
                    _uiState.value = UiState.Error(R.string.core_designsystemy_generic_error, e)
                }
                .collect { state ->
                    _uiState.value = state
                }
        }
    }

    fun onRefresh() {
        // aquí puedes reiniciar manualmente si quieres
        //  observe()
    }
}

abstract class UiStateRefreshViewModel<T> : ViewModel() {

    abstract fun getFlow(): Flow<T>

    private val refresh = Refresh(
        errorMapper = ::errorMapper,
        upstream = { getFlow() }
    )

    init {
        refresh.refresh()
    }

    @StringRes
    open fun errorMapper(e: Throwable): Int {
        return R.string.core_designsystemy_generic_error
    }

    open val dispatcher = Dispatchers.IO
    // open val sharingStarted = SharingStarted.WhileSubscribed(5_000)
    open val sharingStarted = SharingStarted.Eagerly

    val uiState: StateFlow<UiState<T>> =
        refresh.flow
            .flowOn(dispatcher)
            .stateIn(
                viewModelScope,
                sharingStarted,
                UiState.Loading
            )

    fun onRefresh() {
        refresh.refresh()
    }
}