package com.syouth.dollarapp.ui.common

import com.syouth.dollarapp.ui.common.EditDistance.EditOperation
import org.junit.Assert.assertEquals
import org.junit.Test

class EditDistanceTest {

    @Test
    fun `given identical strings, when calculateEditOperations called, then returns empty list`() {
        // Given
        val source = "kitten"
        val target = "kitten"

        // When
        val result = EditDistance.calculateEditOperations(source, target)

        // Then
        assertEquals(emptyList<EditOperation>(), result)
    }

    @Test
    fun `given empty source, when calculateEditOperations called, then returns all insertions`() {
        // Given
        val source = ""
        val target = "abc"

        // When
        val result = EditDistance.calculateEditOperations(source, target)

        // Then
        val expected = listOf(
            EditOperation.Insert('a', 0),
            EditOperation.Insert('b', 1),
            EditOperation.Insert('c', 2)
        )
        assertEquals(expected, result)
    }

    @Test
    fun `given empty target, when calculateEditOperations called, then returns all deletions`() {
        // Given
        val source = "abc"
        val target = ""

        // When
        val result = EditDistance.calculateEditOperations(source, target)

        // Then
        val expected = listOf(
            EditOperation.Delete('a', 0),
            EditOperation.Delete('b', 1),
            EditOperation.Delete('c', 2)
        )
        assertEquals(expected, result)
    }

    @Test
    fun `given strings requiring replacement, when calculateEditOperations called, then returns replacement operation`() {
        // Given
        val source = "cat"
        val target = "bat"

        // When
        val result = EditDistance.calculateEditOperations(source, target)

        // Then
        val expected = listOf(
            EditOperation.Replace('c', 'b', 0)
        )
        assertEquals(expected, result)
    }

    @Test
    fun `given strings requiring insertion, when calculateEditOperations called, then returns insertion operation`() {
        // Given
        val source = "cat"
        val target = "cats"

        // When
        val result = EditDistance.calculateEditOperations(source, target)

        // Then
        val expected = listOf(
            EditOperation.Insert('s', 3)
        )
        assertEquals(expected, result)
    }

    @Test
    fun `given strings requiring deletion, when calculateEditOperations called, then returns deletion operation`() {
        // Given
        val source = "cats"
        val target = "cat"

        // When
        val result = EditDistance.calculateEditOperations(source, target)

        // Then
        val expected = listOf(
            EditOperation.Delete('s', 3)
        )
        assertEquals(expected, result)
    }

    @Test
    fun `given complex difference, when calculateEditOperations called, then returns correct sequence of operations`() {
        // Given
        val source = "kitten"
        val target = "sitting"

        // When
        val result = EditDistance.calculateEditOperations(source, target)

        // Then
        
        val expected = listOf(
            EditOperation.Replace('k', 's', 0),
            EditOperation.Replace('e', 'i', 4),
            EditOperation.Insert('g', 6)
        )
        
        assertEquals(expected, result)
    }
    
    @Test
    fun `given completely different strings, when calculateEditOperations called, then returns all replacements`() {
        // Given
        val source = "abc"
        val target = "def"

        // When
        val result = EditDistance.calculateEditOperations(source, target)
        
        // Then
        val expected = listOf(
            EditOperation.Replace('a', 'd', 0),
            EditOperation.Replace('b', 'e', 1),
            EditOperation.Replace('c', 'f', 2)
        )
        assertEquals(expected, result)
    }
}
