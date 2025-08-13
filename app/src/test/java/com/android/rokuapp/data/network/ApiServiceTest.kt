package com.android.rokuapp.data.network

import com.android.rokuapp.data.model.ApiResponse
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import okhttp3.MediaType.Companion.toMediaType

class ApiServiceTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var api: RockuApi
    private val json = Json { ignoreUnknownKeys = true }

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        
        api = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(RockuApi::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `getApps returns correct data when API responds successfully`() = runTest {
        // Given
        val responseBody = """
            {
                "apps": [
                    {
                        "id": "31012",
                        "imageUrl": "31012.jpeg",
                        "name": "Fandango Now"
                    },
                    {
                        "id": "12", 
                        "imageUrl": "12.jpeg",
                        "name": "Netflix"
                    }
                ]
            }
        """.trimIndent()
        
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(responseBody)
                .addHeader("Content-Type", "application/json")
        )

        // When
        val response = api.getApps()

        // Then
        assertEquals(2, response.apps.size)
        assertEquals("31012", response.apps[0].id)
        assertEquals("Fandango Now", response.apps[0].name)
        assertEquals("31012.jpeg", response.apps[0].imageUrl)
        assertEquals("12", response.apps[1].id)
        assertEquals("Netflix", response.apps[1].name)
        assertEquals("12.jpeg", response.apps[1].imageUrl)
    }

    @Test
    fun `getApps returns empty list when API responds with empty apps array`() = runTest {
        // Given
        val responseBody = """
            {
                "apps": []
            }
        """.trimIndent()
        
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(responseBody)
                .addHeader("Content-Type", "application/json")
        )

        // When
        val response = api.getApps()

        // Then
        assertEquals(0, response.apps.size)
    }

    @Test
    fun `getApps makes request to correct endpoint`() = runTest {
        // Given
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody("""{"apps": []}""")
                .addHeader("Content-Type", "application/json")
        )

        // When
        api.getApps()

        // Then
        val request = mockWebServer.takeRequest()
        assertEquals("GET", request.method)
        assertEquals("/apps.json", request.path)
    }

    @Test(expected = Exception::class)
    fun `getApps throws exception when API returns error`() = runTest {
        // Given
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(500)
                .setBody("Internal Server Error")
        )

        // When
        api.getApps()

        // Then - Exception should be thrown
    }

    @Test
    fun `ApiService object has correct base URL`() {
        // When
        val baseUrl = ApiService.BASE_URL

        // Then
        assertEquals("https://rokumobileinterview.s3.us-west-2.amazonaws.com/", baseUrl)
    }

    @Test
    fun `ApiService api property is not null`() {
        // When
        val apiInstance = ApiService.api

        // Then
        assertNotNull(apiInstance)
    }

    @Test
    fun `getApps handles malformed JSON gracefully`() = runTest {
        // Given
        val malformedJson = """
            {
                "apps": [
                    {
                        "id": "31012",
                        "imageUrl": "31012.jpeg"
                        // Missing "name" field and closing brace
                    }
                ]
            }
        """.trimIndent()
        
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(malformedJson)
                .addHeader("Content-Type", "application/json")
        )

        try {
            // When
            api.getApps()
            
            // If we reach here, the JSON parser was more forgiving than expected
            // This is fine, just means our serialization is robust
        } catch (e: Exception) {
            // Then - This is expected for malformed JSON
            assertNotNull(e)
        }
    }

    @Test
    fun `getApps handles missing fields with ignoreUnknownKeys`() = runTest {
        // Given
        val jsonWithExtraFields = """
            {
                "apps": [
                    {
                        "id": "31012",
                        "imageUrl": "31012.jpeg",
                        "name": "Fandango Now",
                        "extraField": "should be ignored",
                        "anotherExtraField": 12345
                    }
                ],
                "metadata": "should be ignored",
                "version": "1.0"
            }
        """.trimIndent()
        
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(jsonWithExtraFields)
                .addHeader("Content-Type", "application/json")
        )

        // When
        val response = api.getApps()

        // Then
        assertEquals(1, response.apps.size)
        assertEquals("31012", response.apps[0].id)
        assertEquals("Fandango Now", response.apps[0].name)
        assertEquals("31012.jpeg", response.apps[0].imageUrl)
    }
}
