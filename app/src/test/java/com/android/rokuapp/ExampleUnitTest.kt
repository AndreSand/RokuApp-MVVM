package com.android.rokuapp

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

/**
 * Basic unit tests to verify project setup and fundamental functionality.
 */
class ProjectSetupTest {
    
    @Test
    fun `basic math operations work correctly`() {
        assertEquals(4, 2 + 2)
        assertEquals(6, 2 * 3)
        assertEquals(2, 4 / 2)
        assertEquals(1, 3 - 2)
    }
    
    @Test
    fun `string operations work correctly`() {
        val appName = "RokuApp"
        assertEquals("RokuApp", appName)
        assertEquals(7, appName.length)
        assertEquals("ROKUAPP", appName.uppercase())
    }
    
    @Test
    fun `collections work correctly`() {
        val list = listOf(1, 2, 3, 4, 5)
        assertEquals(5, list.size)
        assertEquals(1, list.first())
        assertEquals(5, list.last())
        
        val filtered = list.filter { it > 3 }
        assertEquals(listOf(4, 5), filtered)
    }
    
    @Test
    fun `null safety works correctly`() {
        val nullableString: String? = null
        val nonNullString: String? = "Test"
        
        assertEquals(null, nullableString)
        assertNotNull(nonNullString)
        assertEquals("Test", nonNullString)
    }
}
