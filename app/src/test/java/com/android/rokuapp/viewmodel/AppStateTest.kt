package com.android.rokuapp.viewmodel

import com.android.rokuapp.data.model.App
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class AppStateTest {

    @Test
    fun `AppState has correct default values`() {
        // When
        val state = AppState()

        // Then
        assertEquals(emptyList<App>(), state.apps)
        assertFalse(state.isLoading)
        assertNull(state.error)
    }

    @Test
    fun `AppState can be created with custom values`() {
        // Given
        val apps = listOf(
            App(id = "1", name = "Netflix", imageUrl = "netflix.jpg")
        )
        val isLoading = true
        val error = "Network error"

        // When
        val state = AppState(
            apps = apps,
            isLoading = isLoading,
            error = error
        )

        // Then
        assertEquals(apps, state.apps)
        assertTrue(state.isLoading)
        assertEquals(error, state.error)
    }

    @Test
    fun `AppState copy function works correctly`() {
        // Given
        val originalState = AppState(
            apps = emptyList(),
            isLoading = false,
            error = null
        )

        // When
        val newApps = listOf(App(id = "1", name = "Netflix", imageUrl = "netflix.jpg"))
        val copiedState = originalState.copy(
            apps = newApps,
            isLoading = true
        )

        // Then
        assertEquals(newApps, copiedState.apps)
        assertTrue(copiedState.isLoading)
        assertNull(copiedState.error) // Should remain unchanged
        
        // Original state should not be modified
        assertEquals(emptyList<App>(), originalState.apps)
        assertFalse(originalState.isLoading)
    }

    @Test
    fun `AppState copy with only apps change`() {
        // Given
        val originalState = AppState(isLoading = true, error = "Some error")
        val newApps = listOf(
            App(id = "1", name = "Netflix", imageUrl = "netflix.jpg"),
            App(id = "2", name = "YouTube", imageUrl = "youtube.jpg")
        )

        // When
        val copiedState = originalState.copy(apps = newApps)

        // Then
        assertEquals(newApps, copiedState.apps)
        assertTrue(copiedState.isLoading) // Should remain unchanged
        assertEquals("Some error", copiedState.error) // Should remain unchanged
    }

    @Test
    fun `AppState copy with only loading change`() {
        // Given
        val apps = listOf(App(id = "1", name = "Netflix", imageUrl = "netflix.jpg"))
        val originalState = AppState(apps = apps, error = "Some error")

        // When
        val copiedState = originalState.copy(isLoading = true)

        // Then
        assertEquals(apps, copiedState.apps) // Should remain unchanged
        assertTrue(copiedState.isLoading)
        assertEquals("Some error", copiedState.error) // Should remain unchanged
    }

    @Test
    fun `AppState copy with only error change`() {
        // Given
        val apps = listOf(App(id = "1", name = "Netflix", imageUrl = "netflix.jpg"))
        val originalState = AppState(apps = apps, isLoading = true)

        // When
        val copiedState = originalState.copy(error = "New error")

        // Then
        assertEquals(apps, copiedState.apps) // Should remain unchanged
        assertTrue(copiedState.isLoading) // Should remain unchanged
        assertEquals("New error", copiedState.error)
    }

    @Test
    fun `AppState copy clearing error`() {
        // Given
        val originalState = AppState(error = "Some error")

        // When
        val copiedState = originalState.copy(error = null)

        // Then
        assertNull(copiedState.error)
    }

    @Test
    fun `AppState equality works correctly`() {
        // Given
        val apps = listOf(App(id = "1", name = "Netflix", imageUrl = "netflix.jpg"))
        val state1 = AppState(apps = apps, isLoading = true, error = "Error")
        val state2 = AppState(apps = apps, isLoading = true, error = "Error")
        val state3 = AppState(apps = apps, isLoading = false, error = "Error")

        // Then
        assertEquals(state1, state2)
        assertFalse(state1 == state3)
    }
}
