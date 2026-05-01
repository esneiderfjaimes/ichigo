package com.nei.ichigo

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class Refresh<T>(
    private val upstream: () -> Flow<T>,
) {
    private val refreshTrigger = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

    // 1. Añadimos un StateFlow para exponer el estado de carga
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val flow = refreshTrigger
        .onStart { emit(Unit) }
        .flatMapLatest {
            upstream()
                .onStart { _isRefreshing.value = true } // Inicia el refresh
                .onEach { _isRefreshing.value = false }  // Termina cuando llega el primer dato
                .onCompletion { _isRefreshing.value = false } // Asegura reset si el flow termina o falla
        }

    fun refresh() {
        refreshTrigger.tryEmit(Unit)
    }
}


@OptIn(ExperimentalCoroutinesApi::class)
class RefreshTest {

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `refresh should trigger new emission from upstream`() = runTest(testDispatcher) {
        var count = 0
        val refresh = Refresh {
            flowOf(++count)
        }

        val results = mutableListOf<Int>()
        val job = launch {
            refresh.flow.collect { results.add(it) }
        }

        testScheduler.runCurrent()
        assertEquals(listOf(1), results)

        refresh.refresh()
        testScheduler.runCurrent()
        assertEquals(listOf(1, 2), results)

        refresh.refresh()
        testScheduler.runCurrent()
        assertEquals(listOf(1, 2, 3), results)

        job.cancel()
    }

    @Test
    fun `isRefreshing should be true while upstream is loading`() = runTest(testDispatcher) {
        val refresh = Refresh {
            flow {
                delay(1000)
                emit(1)
            }
        }

        val results = mutableListOf<Int>()
        val job = launch {
            refresh.flow.collect { results.add(it) }
        }

        // Al inicio, después de suscribirse pero antes de que pase el tiempo
        testScheduler.runCurrent()
        assertTrue("Debería estar refrescando", refresh.isRefreshing.value)
        assertEquals(0, results.size)

        // Avanzamos el tiempo para que el flow emita
        testScheduler.advanceTimeBy(1001)
        testScheduler.runCurrent()
        
        assertFalse("Ya no debería estar refrescando", refresh.isRefreshing.value)
        assertEquals(listOf(1), results)

        // Provocamos un refresh manual
        refresh.refresh()
        testScheduler.runCurrent()
        assertTrue("Debería estar refrescando otra vez", refresh.isRefreshing.value)

        testScheduler.advanceTimeBy(1001)
        testScheduler.runCurrent()
        assertFalse("Debería haber terminado el segundo refresh", refresh.isRefreshing.value)
        assertEquals(listOf(1, 1), results)

        job.cancel()
    }

    @Test
    fun `isRefreshing should be false if upstream fails`() = runTest(testDispatcher) {
        val refresh = Refresh<Int> {
            flow {
                delay(500)
                throw RuntimeException("Failure")
            }
        }

        val job = launch {
            try {
                refresh.flow.collect { }
            } catch (_: Exception) {
                // ignorar
            }
        }

        testScheduler.advanceTimeBy(100)
        assertTrue(refresh.isRefreshing.value)

        testScheduler.advanceTimeBy(500)
        testScheduler.runCurrent()

        assertFalse("Debería ser false tras un error", refresh.isRefreshing.value)
        job.cancel()
    }

}