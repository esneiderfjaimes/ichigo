package com.nei.ichigo.common

import android.util.Log
import app.cash.turbine.test
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BaseViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0
        every { Log.e(any(), any(), any()) } returns 0
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkStatic(Log::class)
    }

    private class TestViewModel(
        private val _flow: Flow<String>
    ) : UiStateSplitViewModel<String>() {

        override fun errorMapper(e: Throwable): Int {
            return 0
        }

        override fun getFlow(): Flow<String> {
            return _flow
        }

        // We use Main (testDispatcher) to avoid multi-threading issues in unit tests
        //override val dispatcher = Dispatchers.Main
        //    override val sharingStarted = SharingStarted.Eagerly
    }

    @Test
    fun `uiState emits Loading then Success when flow emits value`() = runTest {
        val expectedValue = "Success Value"
        val viewModel = TestViewModel(
            flowOf(expectedValue)
        )

        viewModel.uiState.test {
            assertEquals(UiState.Loading, awaitItem())
            assertEquals(UiState.Success(expectedValue), awaitItem())
            expectNoEvents()
        }
    }

    @Test
    fun `uiState emits Loading then Error when flow throws exception`() = runTest {
        val runtimeException = RuntimeException("Test Exception")

        val viewModel = TestViewModel(
            flow { throw runtimeException }
        )

        viewModel.uiState.test {
            assertEquals(UiState.Loading, awaitItem())
            assertEquals(UiState.Error(0, runtimeException), awaitItem())
            expectNoEvents()
        }
    }

    @Test
    fun `uiState emits Success then Error when flow fails after emission`() = runTest {
        val runtimeException = RuntimeException("boom")

        val viewModel = TestViewModel(
            _flow = flow {
                emit("OK")
                throw runtimeException
            }
        )

        viewModel.uiState.test {
            assertEquals(UiState.Loading, awaitItem())
            assertEquals(UiState.Success("OK"), awaitItem())
            assertEquals(UiState.Error(0, runtimeException), awaitItem())
            expectNoEvents()
        }
    }
}
