package com.nei.ichigo.common.refresh

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nei.ichigo.common.UiState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.stateIn
import kotlin.random.Random

/*
sealed class UiState<out T>(open val isRefreshing: Boolean) {
    data object Loading : UiState<Nothing>(false)

    data class Success<T>(
        val content: T,
        override val isRefreshing: Boolean = false
    ) : UiState<T>(isRefreshing)

    data class Error(
        @param:StringRes val messageRes: Int,
        val throwable: Throwable? = null,
        override val isRefreshing: Boolean = false
    ) : UiState<Nothing>(isRefreshing)
}
*/

class Refresh<T>(
    private val errorMapper: (Throwable) -> Int,
    private val upstream: () -> Flow<T>
) {
    private val refreshTrigger = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

    @OptIn(ExperimentalCoroutinesApi::class)
    val flow: Flow<UiState<T>> = refreshTrigger
        .onStart { emit(Unit) }
        .flatMapLatest {
            flow {
                emit(RefreshSignal.Refreshing)
                try {
                    upstream().collect { emit(RefreshSignal.Success(it)) }
                } catch (e: Exception) {
                    if (e is CancellationException) throw e
                    emit(
                        RefreshSignal.Error(
                            messageRes = errorMapper(e),
                            throwable = e
                        )
                    )
                }
            }
        }
        .scan<RefreshSignal<T>, UiState<T>>(UiState.Loading) { prev, signal ->
            Log.e("Refresh", "Signal: $signal, prev: $prev")
            when (signal) {
                is RefreshSignal.Success -> UiState.Success(signal.data)
                is RefreshSignal.Error -> UiState.Error(signal.messageRes, signal.throwable)
                // Use refresh signal to simulate refreshing state
                RefreshSignal.Refreshing -> when (prev) {
                    is UiState.Success -> prev.copy(isRefreshing = true)
                    is UiState.Error -> prev.copy(isRefreshing = true)
                    // If previous state is loading, do not change
                    UiState.Loading -> UiState.Loading
                }
            }
        }.retry(2) {
            Log.e("Refresh", "Error: ${it.message}")
            delay(1000)
            true
        }.onEach {
            Log.e("Refresh", "State: $it")
        }

    fun refresh() {
        refreshTrigger.tryEmit(Unit)
    }

    private sealed interface RefreshSignal<out T> {
        data object Refreshing : RefreshSignal<Nothing>
        data class Success<T>(val data: T) : RefreshSignal<T>
        data class Error(val messageRes: Int, val throwable: Throwable?) : RefreshSignal<Nothing>
    }
}

@Composable
fun <T> RefreshContent(
    state: UiState<T>,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (T) -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (state) {
                UiState.Loading -> CircularProgressIndicator()
                is UiState.Success -> {
                    content(state.content)
                    Button(
                        onClick = onRefresh,
                        modifier = Modifier.padding(top = 16.dp),
                        enabled = !state.isRefreshing
                    ) {
                        Text("Refresh Success")
                    }
                }

                is UiState.Error -> {
                    Text(
                        "Error (Res ID: ${state.messageRes})",
                        color = MaterialTheme.colorScheme.error
                    )
                    Button(
                        onClick = onRefresh,
                        modifier = Modifier.padding(top = 16.dp),
                        enabled = !state.isRefreshing
                    ) {
                        Text("Reintentar")
                    }
                }
            }
        }

        if (state.isRefreshing && state !is UiState.Loading) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RefreshDemoPreview() {
    val refresh = remember {
        Refresh({ 0 }) {
            flow {
                delay(2000)
                if (Random.nextBoolean()) {
                    emit("Dato: ${Random.nextInt(100)}")
                } else {
                    throw RuntimeException("Error")
                }
            }
        }
    }

    val state by refresh.flow.collectAsState(initial = UiState.Loading)

    MaterialTheme {
        Surface {
            RefreshContent(
                state = state,
                onRefresh = { refresh.refresh() }
            ) { data ->
                Text(text = data, style = MaterialTheme.typography.headlineMedium)
            }
        }
    }
}

abstract class UiStateViewModel2<UiStateType> : ViewModel() {

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

class DemoViewModel2 : UiStateViewModel2<String>() {
    override val flow: Flow<String>
        get() = flow {
            delay(2000)
            if (Random.nextBoolean()) {
                emit("Dato desde VM: ${Random.nextInt(100)}")
            } else {
                throw RuntimeException("Error en VM")
            }
        }

    fun refresh() {
        refresh.refresh()
    }
}

@Preview(showBackground = true)
@Composable
private fun RefreshViewModelDemoPreview() {
    val viewModel: DemoViewModel2 = viewModel()
    val state by viewModel.uiState.collectAsState()

    MaterialTheme {
        Surface {
            RefreshContent(
                state = state,
                onRefresh = viewModel::refresh
            ) { data ->
                Text(text = data, style = MaterialTheme.typography.headlineMedium)
            }
        }
    }
}