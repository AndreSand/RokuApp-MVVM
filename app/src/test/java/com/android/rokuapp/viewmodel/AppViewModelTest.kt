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
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state should have empty apps list and not loading`() = runTest {
        // Given
        coEvery { mockRepository.getApps() } returns emptyList()
        
        // When
        viewModel = AppViewModel(mockRepository)
        advanceUntilIdle()
        
        // Then
        val finalState = viewModel.state.value
        assertTrue(finalState.apps.isEmpty())
        assertFalse(finalState.isLoading)
        assertNull(finalState.error)
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
        viewModel = AppViewModel(mockRepository)
        
        viewModel.state.test {
            // Wait for initial loading and then final state
            advanceUntilIdle()
            
            // Skip any initial emissions and get the final state
            val finalState = expectMostRecentItem()
            
            // Then
            assertEquals(expectedApps, finalState.apps)
            assertFalse(finalState.isLoading)
            assertNull(finalState.error)
        }
    }

    @Test
    fun `fetchApps should set loading state correctly`() = runTest {
        // Given
        coEvery { mockRepository.getApps() } returns emptyList()

        // When
        viewModel = AppViewModel(mockRepository)
        
        viewModel.state.test {
            // Initial state
            val initialState = awaitItem()
            assertTrue(initialState.apps.isEmpty())
            assertFalse(initialState.isLoading)
            assertNull(initialState.error)
            
            // Loading state
            val loadingState = awaitItem()
            assertTrue(loadingState.isLoading)
            assertNull(loadingState.error)
            
            advanceUntilIdle()
            
            // Final state
            val finalState = awaitItem()
            assertFalse(finalState.isLoading)
        }
    }

    @Test
    fun `fetchApps should handle error correctly`() = runTest {
        // Given
        val errorMessage = "Network error"
        coEvery { mockRepository.getApps() } throws Exception(errorMessage)

        // When
        viewModel = AppViewModel(mockRepository)
        
        viewModel.state.test {
            // Initial state
            awaitItem()
            
            // Loading state
            awaitItem()
            
            advanceUntilIdle()
            
            // Error state
            val errorState = awaitItem()
            assertFalse(errorState.isLoading)
            assertEquals(errorMessage, errorState.error)
            assertTrue(errorState.apps.isEmpty())
        }
    }

    @Test
    fun `fetchApps should clear previous error when called again`() = runTest {
        // Given - Repository will fail first, then succeed
        coEvery { mockRepository.getApps() } throws Exception("Network error")
        
        viewModel = AppViewModel(mockRepository)
        
        viewModel.state.test {
            // Wait for initial error state
            awaitItem() // initial
            awaitItem() // loading
            advanceUntilIdle()
            val errorState = awaitItem() // error
            assertEquals("Network error", errorState.error)
            
            // When - Repository now succeeds
            val apps = listOf(App(id = "1", name = "Netflix", imageUrl = "netflix.jpg"))
            coEvery { mockRepository.getApps() } returns apps
            
            viewModel.fetchApps()
            
            // Loading state for retry
            val retryLoadingState = awaitItem()
            assertTrue(retryLoadingState.isLoading)
            assertNull(retryLoadingState.error) // Error should be cleared
            
            advanceUntilIdle()
            
            // Success state
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
        viewModel = AppViewModel(mockRepository)
        advanceUntilIdle()

        // Then
        coVerify { mockRepository.getApps() }
    }

    @Test
    fun `manual fetchApps call should work correctly`() = runTest {
        // Given
        val apps = listOf(App(id = "1", name = "Netflix", imageUrl = "netflix.jpg"))
        coEvery { mockRepository.getApps() } returns apps

        viewModel = AppViewModel(mockRepository)
        advanceUntilIdle()

        // When - Call fetchApps manually
        viewModel.fetchApps()
        advanceUntilIdle()

        // Then
        val finalState = viewModel.state.value
        assertEquals(apps, finalState.apps)
        assertFalse(finalState.isLoading)
        assertNull(finalState.error)
        
        // Should have called getApps twice (once in init, once manually)
        coVerify(exactly = 2) { mockRepository.getApps() }
    }
}
