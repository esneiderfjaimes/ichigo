package com.nei.ichigo
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nei.ichigo.common.UiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flattenConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
/*
abstract class RefreshableUiStateViewModel<UiStateType> : UiStateViewModel<UiStateType>() {

    val refreshableUseCase = RefreshableUseCase(
        viewModelScope
    ) { flow }

    fun refresh() {
        refreshableUseCase.refresh()
    }

}

class RefreshableUseCase<T>(
    private val scope: CoroutineScope,
    private val upstream: () -> Flow<T>
) {
    private val refreshTrigger = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

    private val _state = MutableStateFlow<UiState<out T>>(UiState.Loading)
    val state: StateFlow<UiState<out T>> = _state

    init {
        refreshTrigger
            .onStart { emit(Unit) }
            .onEach {
                val current = _state.value

                _state.value = when (current) {
                    is UiState.Success -> current.copy(isRefreshing = true)
                    is UiState.Error -> current.copy(isRefreshing = true)
                    UiState.Loading -> UiState.Loading
                }

                try {
                    upstream().collect { data ->
                        _state.value = UiState.Success(data, isRefreshing = false)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                  //  val previousData = (_state.value as? UiState.Success)?.content
                   // _state.value = UiState.Error(previousData, isRefreshing = false)
                    _state.update {
                        when (it) {
                            is UiState.Error -> it.copy(isRefreshing = false)
                            UiState.Loading -> UiState.Loading
                            is UiState.Success -> it.copy(isRefreshing = false)
                        }
                    }
                }
            }
            .launchIn(scope) // 👈 ahora sí correcto
    }

    fun refresh() {
        refreshTrigger.tryEmit(Unit)
    }
}

class RefreshableFlow<T>(
    upstream: () -> Flow<T>,
    scope: CoroutineScope,
    sharingStarted: SharingStarted = SharingStarted.WhileSubscribed(5_000)
) {
    private val refreshTrigger = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

    val state: StateFlow<UiState<out T>> =
        refreshTrigger
            .onStart { emit(Unit) }
            .scan(UiState.Loading) { currentState, _ ->

                flow {
                    val isFirstLoad = currentState is UiState.Loading

                    if (!isFirstLoad) {
                        // emit estado de refreshing sin perder data
                        when (currentState) {
                            is UiState.Success<*> -> emit(currentState.copy(isRefreshing = true))
                            is UiState.Error -> emit(currentState.copy(isRefreshing = true))
                            else -> {}
                        }
                    }

                    try {
                        upstream().collect { data ->
                            emit(UiState.Success(data, isRefreshing = false))
                        }
                    } catch (e: Exception) {
                 */
/*       val previousData = (currentState as? UiState.Success)?.data
                        emit(UiState.Error(previousData, isRefreshing = false))*//*


                        emit(   currentState.let {
                            when (it) {
                                is UiState.Error -> it.copy(isRefreshing = false)
                                UiState.Loading -> UiState.Loading
                                is UiState.Success<*> -> it.copy(isRefreshing = false)
                            }
                        })
                    }
                }.flattenConcat().last()

            }
            .stateIn(scope, sharingStarted, UiState.Loading)

    fun refresh() {
        refreshTrigger.tryEmit(Unit)
    }
}
*/
