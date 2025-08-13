package com.android.rokuapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.android.rokuapp.data.model.App
import com.android.rokuapp.data.repository.Repository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import app.cash.turbine.test

@ExperimentalCoroutinesApi
class AppViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: AppViewModel
    private lateinit var mockRepository: Repository
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        mockRepository = mockk()
        
        // Setup default behavior to prevent init block from failing
        coEvery { mockRepository.getApps() } returns emptyList()
        
        viewModel = AppViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state should have empty apps list and not loading`() = runTest {
        // Given - ViewModel is initialized
        
        // When - Checking initial state
        val initialState = viewModel.state.value
        
        // Then
        assertTrue(initialState.apps.isEmpty())
        assertFalse(initialState.isLoading)
        assertNull(initialState.error)
    }

    @Test
    fun `fetchApps should update state correctly when successful`() = runTest {
        // Given
        val expectedApps = listOf(
            App(id = "1", name = "Netflix", imageUrl = "netflix.jpg"),
            App(id = "2", name = "YouTube", imageUrl = "youtube.jpg")
        )
        coEvery { mockRepository.getApps() } returns expectedApps

        // When
        viewModel.state.test {
            // Skip initial emissions from init
            skipItems(2) // Skip initial state and loading state
            
            viewModel.fetchApps()
            advanceUntilIdle()

            // Then
            val finalState = awaitItem()
            assertEquals(expectedApps, finalState.apps)
            assertFalse(finalState.isLoading)
            assertNull(finalState.error)
        }
    }

    @Test
    fun `fetchApps should set loading state correctly`() = runTest {
        // Given
        coEvery { mockRepository.getApps() } returns emptyList()

        // When/Then
        viewModel.state.test {
            // Skip initial state
            awaitItem()
            
            viewModel.fetchApps()
            
            // Should set loading to true
            val loadingState = awaitItem()
            assertTrue(loadingState.isLoading)
            assertNull(loadingState.error)
            
            advanceUntilIdle()
            
            // Should set loading to false when done
            val finalState = awaitItem()
            assertFalse(finalState.isLoading)
        }
    }

    @Test
    fun `fetchApps should handle error correctly`() = runTest {
        // Given
        val errorMessage = "Network error"
        coEvery { mockRepository.getApps() } throws Exception(errorMessage)

        // When/Then
        viewModel.state.test {
            // Skip initial emissions
            skipItems(2)
            
            viewModel.fetchApps()
            advanceUntilIdle()

            val errorState = awaitItem()
            assertFalse(errorState.isLoading)
            assertEquals(errorMessage, errorState.error)
            assertTrue(errorState.apps.isEmpty())
        }
    }

    @Test
    fun `fetchApps should clear previous error when called again`() = runTest {
        // Given - First call fails
        coEvery { mockRepository.getApps() } throws Exception("Network error")
        
        viewModel.state.test {
            skipItems(2)
            viewModel.fetchApps()
            advanceUntilIdle()
            
            val errorState = awaitItem()
            assertEquals("Network error", errorState.error)
            
            // When - Second call succeeds
            val apps = listOf(App(id = "1", name = "Netflix", imageUrl = "netflix.jpg"))
            coEvery { mockRepository.getApps() } returns apps
            
            viewModel.fetchApps()
            advanceUntilIdle()
            
            // Then - Error should be cleared
            val successState = awaitItem()
            assertNull(successState.error)
            assertEquals(apps, successState.apps)
            assertFalse(successState.isLoading)
        }
    }

    @Test
    fun `fetchApps should call repository getApps method`() = runTest {
        // Given
        coEvery { mockRepository.getApps() } returns emptyList()

        // When
        viewModel.fetchApps()
        advanceUntilIdle()

        // Then
        coVerify { mockRepository.getApps() }
    }
}
