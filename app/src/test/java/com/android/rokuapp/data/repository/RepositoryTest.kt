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
        repository = Repository(mockApi)
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
        coVerify { mockApi.getApps() }
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
        coVerify { mockApi.getApps() }
    }

    @Test
    fun `getApps calls API exactly once`() = runTest {
        // Given
        val apiResponse = ApiResponse(apps = emptyList())
        coEvery { mockApi.getApps() } returns apiResponse

        // When
        repository.getApps()

        // Then
        coVerify(exactly = 1) { mockApi.getApps() }
    }

    @Test
    fun `getApps extracts apps from ApiResponse correctly`() = runTest {
        // Given
        val app1 = App(id = "31012", name = "Fandango Now", imageUrl = "31012.jpeg")
        val app2 = App(id = "12", name = "Netflix", imageUrl = "12.jpeg")
        val apiResponse = ApiResponse(apps = listOf(app1, app2))
        
        coEvery { mockApi.getApps() } returns apiResponse

        // When
        val result = repository.getApps()

        // Then
        assertEquals(2, result.size)
        assertEquals(app1, result[0])
        assertEquals(app2, result[1])
    }

    @Test
    fun `getApps handles single app in response`() = runTest {
        // Given
        val singleApp = App(id = "12", name = "Netflix", imageUrl = "12.jpeg")
        val apiResponse = ApiResponse(apps = listOf(singleApp))
        
        coEvery { mockApi.getApps() } returns apiResponse

        // When
        val result = repository.getApps()

        // Then
        assertEquals(1, result.size)
        assertEquals(singleApp, result[0])
    }
}
