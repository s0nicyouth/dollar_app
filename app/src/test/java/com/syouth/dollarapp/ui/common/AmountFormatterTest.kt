package com.syouth.dollarapp.ui.common

import android.icu.text.NumberFormat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test
import java.math.BigDecimal as JavaBigDecimal

class AmountFormatterTest {

    private val numberFormat: NumberFormat = mockk()
    private val amountFormatter = AmountFormatter(numberFormat)

    @Test
    fun `given empty string, when format is called, then return empty string`() {
        // Given
        val input = ""

        // When
        val result = amountFormatter.format(input)

        // Then
        assertEquals(input, result)
    }

    @Test
    fun `given invalid number string, when format is called, then return original input`() {
        // Given
        val input = "invalid"

        // When
        val result = amountFormatter.format(input)

        // Then
        assertEquals(input, result)
    }

    @Test
    fun `given valid number string, when format is called, then return formatted number`() {
        // Given
        val input = "1234.56"
        val expectedFormatted = "1,234.56"
        every { numberFormat.format(any<JavaBigDecimal>()) } returns expectedFormatted

        // When
        val result = amountFormatter.format(input)

        // Then
        assertEquals(expectedFormatted, result)
        verify { numberFormat.format(any<JavaBigDecimal>()) }
    }

    @Test
    fun `given number string ending with dot, when format is called, then return formatted number with dot`() {
        // Given
        val input = "1234."
        val formattedPart = "1,234"
        every { numberFormat.format(any<JavaBigDecimal>()) } returns formattedPart

        // When
        val result = amountFormatter.format(input)

        // Then
        assertEquals("$formattedPart.", result)
        verify { numberFormat.format(any<JavaBigDecimal>()) }
    }
}
