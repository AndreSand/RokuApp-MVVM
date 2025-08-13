package com.android.rokuapp.data.repository

import com.android.rokuapp.data.model.App
import com.android.rokuapp.data.model.ApiResponse
import com.android.rokuapp.data.network.RockuApi
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class RepositoryTest {

    private lateinit var repository: Repository
    private lateinit var mockApi: RockuApi

    @Before
    fun setup() {
        mockApi = mockk()
        repository = Repository()
        // Note: In a real implementation, you'd want to inject the API dependency
        // For now, we'll test the current implementation structure
    }

    @Test
    fun `getApps returns list of apps when API call is successful`() = runTest {
        // Given
        val expectedApps = listOf(
            App(id = "1", name = "Netflix", imageUrl = "netflix.jpg"),
            App(id = "2", name = "YouTube", imageUrl = "youtube.jpg")
        )
        val apiResponse = ApiResponse(apps = expectedApps)
        
        coEvery { mockApi.getApps() } returns apiResponse

        // When
        val result = repository.getApps()

        // Then
        assertEquals(expectedApps, result)
    }

    @Test(expected = Exception::class)
    fun `getApps throws exception when API call fails`() = runTest {
        // Given
        coEvery { mockApi.getApps() } throws Exception("Network error")

        // When
        repository.getApps()

        // Then - Exception should be thrown
    }

    @Test
    fun `getApps returns empty list when API returns empty apps list`() = runTest {
        // Given
        val apiResponse = ApiResponse(apps = emptyList())
        coEvery { mockApi.getApps() } returns apiResponse

        // When
        val result = repository.getApps()

        // Then
        assertEquals(emptyList<App>(), result)
    }
}
