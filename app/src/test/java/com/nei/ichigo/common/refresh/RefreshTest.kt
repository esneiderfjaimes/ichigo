package com.nei.ichigo.common.refresh

import com.nei.ichigo.common.UiState
import io.mockk.every
import io.mockk.mockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
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

@OptIn(ExperimentalCoroutinesApi::class)
class RefreshTest {

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        mockkStatic(android.util.Log::class)
        every { android.util.Log.d(any(), any()) } returns 0
        every { android.util.Log.e(any(), any()) } returns 0
        every { android.util.Log.e(any(), any(), any()) } returns 0
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `refresh should trigger new emission from upstream`() = runTest(testDispatcher) {
        var count = 0
        val refresh = Refresh({ 0 }) {
            flowOf(++count)
        }

        val results = mutableListOf<UiState<Int>>()
        val job = launch {
            refresh.flow.collect { results.add(it) }
        }

        testScheduler.runCurrent()
        assertEquals(UiState.Success(1), results.last())

        refresh.refresh()
        testScheduler.runCurrent()
        assertEquals(UiState.Success(2), results.last())

        job.cancel()
    }

    @Test
    fun `isRefreshing should be true while upstream is loading`() = runTest(testDispatcher) {
        val refresh = Refresh({ 0 }) {
            flow {
                delay(1000)
                emit(1)
            }
        }

        val results = mutableListOf<UiState<Int>>()
        val job = launch {
            refresh.flow.collect { results.add(it) }
        }

        // Al inicio: Loading
        testScheduler.runCurrent()
        assertEquals(UiState.Loading, results.last())

        // Avanzamos el tiempo: Success
        testScheduler.advanceTimeBy(1001)
        testScheduler.runCurrent()
        assertEquals(UiState.Success(1), results.last())

        // Provocamos un refresh manual
        refresh.refresh()
        testScheduler.runCurrent()
        // Debería ser Success(1, isRefreshing = true)
        val stateDuringRefresh = results.last()
        assertTrue("Debería estar refrescando", stateDuringRefresh.isRefreshing)
        assertTrue("Debería ser Success", stateDuringRefresh is UiState.Success)
        assertEquals(1, (stateDuringRefresh as UiState.Success).content)

        testScheduler.advanceTimeBy(1001)
        testScheduler.runCurrent()
        assertEquals(UiState.Success(1, isRefreshing = false), results.last())

        job.cancel()
    }

    @Test
    fun `isRefreshing should be false if upstream fails`() = runTest(testDispatcher) {
        val refresh = Refresh<Int>({ 0 }) {
            flow {
                delay(500)
                throw RuntimeException("Failure")
            }
        }

        val results = mutableListOf<UiState<Int>>()
        val job = launch {
            refresh.flow.collect { results.add(it) }
        }

        testScheduler.advanceTimeBy(100)
        testScheduler.runCurrent()
        assertEquals(UiState.Loading, results.last())

        testScheduler.advanceTimeBy(500)
        testScheduler.runCurrent()

        val finalState = results.last()
        assertTrue("Debería ser Error", finalState is UiState.Error)
        assertFalse("No debería estar refrescando ya", finalState.isRefreshing)

        job.cancel()
    }

    @Test
    fun `should recover from error after refresh`() = runTest(testDispatcher) {
        var shouldFail = true
        val refresh = Refresh({ 0 }) {
            flow {
                delay(100)
                if (shouldFail) {
                    shouldFail = false
                    throw RuntimeException("Failure")
                } else {
                    emit("Success")
                }
            }
        }

        val results = mutableListOf<UiState<String>>()
        val job = launch {
            refresh.flow.collect { results.add(it) }
        }

        // 1. Intento inicial: falla
        testScheduler.advanceTimeBy(101)
        testScheduler.runCurrent()
        assertTrue("Debería ser Error", results.last() is UiState.Error)

        // 2. Trigger refresh
        refresh.refresh()
        testScheduler.runCurrent()
        // Estado intermedio: Error + isRefreshing = true
        val stateDuringRefresh = results.last()
        assertTrue("Debería ser Error mientras refresca", stateDuringRefresh is UiState.Error)
        assertTrue("isRefreshing debería ser true", stateDuringRefresh.isRefreshing)

        // 3. Segunda carga: funciona
        testScheduler.advanceTimeBy(101)
        testScheduler.runCurrent()
        assertEquals(UiState.Success("Success"), results.last())
        assertFalse(results.last().isRefreshing)

        job.cancel()
    }

}
