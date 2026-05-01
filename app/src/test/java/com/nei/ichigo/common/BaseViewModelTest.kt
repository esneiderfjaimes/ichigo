package com.nei.ichigo.common

import android.util.Log
import app.cash.turbine.test
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
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
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkStatic(Log::class)
    }

    private class TestViewModel(
        override val flow: Flow<String>,
        override val dispatcher: CoroutineDispatcher
    ) : UiStateViewModel<String>() {
        // We use Main (testDispatcher) to avoid multi-threading issues in unit tests
        //override val dispatcher = Dispatchers.Main
        override val sharingStarted = SharingStarted.Eagerly
    }

    @Test
    fun `uiState emits Loading then Success when flow emits value`() = runTest {
        val expectedValue = "Success Value"
        val viewModel = TestViewModel(
            flowOf(expectedValue),
            dispatcher = testDispatcher
        )

        viewModel.uiState.test {
            assertEquals(UiState.Loading, awaitItem())
            assertEquals(UiState.Success(expectedValue), awaitItem())
            expectNoEvents()
        }
    }

    @Test
    fun `uiState emits Loading then Error when flow throws exception`() = runTest {
        val viewModel = TestViewModel(
            flow { throw RuntimeException("Test Exception") },
            dispatcher = testDispatcher
        )

        viewModel.uiState.test {
            assertEquals(UiState.Loading, awaitItem())
            assertEquals(UiState.Error(0), awaitItem())
            expectNoEvents()
        }
    }

    @Test
    fun `uiState emits Success then Error when flow fails after emission`() = runTest {
        val viewModel = TestViewModel(
            flow = flow {
                emit("OK")
                throw RuntimeException("boom")
            },
            dispatcher = testDispatcher
        )

        viewModel.uiState.test {
            assertEquals(UiState.Loading, awaitItem())
            assertEquals(UiState.Success("OK"), awaitItem())
            assertEquals(UiState.Error(0), awaitItem())
            expectNoEvents()
        }
    }
}
