package com.android.rokuapp.data.model

import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Test

class AppTest {

    private val json = Json { ignoreUnknownKeys = true }

    @Test
    fun `App serialization and deserialization works correctly`() {
        // Given
        val app = App(
            id = "12345",
            name = "Netflix",
            imageUrl = "netflix.jpg"
        )

        // When
        val jsonString = json.encodeToString(App.serializer(), app)
        val deserializedApp = json.decodeFromString(App.serializer(), jsonString)

        // Then
        assertEquals(app, deserializedApp)
    }

    @Test
    fun `App can be created with all parameters`() {
        // Given
        val id = "12345"
        val name = "Netflix"
        val imageUrl = "netflix.jpg"

        // When
        val app = App(id = id, name = name, imageUrl = imageUrl)

        // Then
        assertEquals(id, app.id)
        assertEquals(name, app.name)
        assertEquals(imageUrl, app.imageUrl)
    }

    @Test
    fun `App deserialization from JSON with correct field names`() {
        // Given
        val jsonString = """
            {
                "id": "12345",
                "name": "Netflix", 
                "imageUrl": "netflix.jpg"
            }
        """.trimIndent()

        // When
        val app = json.decodeFromString(App.serializer(), jsonString)

        // Then
        assertEquals("12345", app.id)
        assertEquals("Netflix", app.name)
        assertEquals("netflix.jpg", app.imageUrl)
    }

    @Test
    fun `ApiResponse serialization and deserialization works correctly`() {
        // Given
        val apps = listOf(
            App(id = "1", name = "Netflix", imageUrl = "netflix.jpg"),
            App(id = "2", name = "YouTube", imageUrl = "youtube.jpg")
        )
        val apiResponse = ApiResponse(apps = apps)

        // When
        val jsonString = json.encodeToString(ApiResponse.serializer(), apiResponse)
        val deserializedResponse = json.decodeFromString(ApiResponse.serializer(), jsonString)

        // Then
        assertEquals(apiResponse, deserializedResponse)
        assertEquals(apps.size, deserializedResponse.apps.size)
        assertEquals(apps[0], deserializedResponse.apps[0])
        assertEquals(apps[1], deserializedResponse.apps[1])
    }

    @Test
    fun `ApiResponse creates with empty list by default`() {
        // When
        val apiResponse = ApiResponse()

        // Then
        assertEquals(emptyList<App>(), apiResponse.apps)
    }

    @Test
    fun `ApiResponse deserialization from real API JSON format`() {
        // Given
        val jsonString = """
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

        // When
        val apiResponse = json.decodeFromString(ApiResponse.serializer(), jsonString)

        // Then
        assertEquals(2, apiResponse.apps.size)
        assertEquals("31012", apiResponse.apps[0].id)
        assertEquals("Fandango Now", apiResponse.apps[0].name)
        assertEquals("31012.jpeg", apiResponse.apps[0].imageUrl)
        assertEquals("12", apiResponse.apps[1].id)
        assertEquals("Netflix", apiResponse.apps[1].name)
        assertEquals("12.jpeg", apiResponse.apps[1].imageUrl)
    }

    @Test
    fun `ApiResponse handles empty apps array`() {
        // Given
        val jsonString = """
            {
                "apps": []
            }
        """.trimIndent()

        // When
        val apiResponse = json.decodeFromString(ApiResponse.serializer(), jsonString)

        // Then
        assertEquals(0, apiResponse.apps.size)
        assertEquals(emptyList<App>(), apiResponse.apps)
    }
}
