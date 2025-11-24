package com.syouth.dollarapp.data.repositories.rates

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.syouth.dollarapp.domain.models.Currency
import com.syouth.dollarapp.domain.models.Rate
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RatesRepositoryImplTest {

    private val localSource: RatesLocalSource = mockk()
    private val remoteSource: RatesRemoteSource = mockk()
    private val repository = RatesRepositoryImpl(localSource, remoteSource)

    @Test
    fun `given empty currencies, when ratesFlow collected, then emits nothing`() = runTest {
        // Given
        val currencies = emptyList<Currency>()

        // When
        val result = repository.ratesFlow(currencies).toList()

        // Then
        assertTrue(result.isEmpty())
    }

    @Test
    fun `given remote fetch succeeds, when ratesFlow collected, then saves to local and emits local data`() = runTest {
        // Given
        val currencies = listOf(Currency("USD"), Currency("MXN"))
        val remoteRates = listOf(
            Rate(BigDecimal.fromInt(20), BigDecimal.fromInt(19), Currency("USD"), Currency("MXN"))
        )
        val localRates = listOf(
            Rate(BigDecimal.fromInt(20), BigDecimal.fromInt(19), Currency("USD"), Currency("MXN"))
        )

        coEvery { remoteSource.fetch(currencies) } returns remoteRates
        coEvery { localSource.save(remoteRates) } returns Unit
        coEvery { localSource.get(currencies) } returns localRates

        // When
        val result = repository.ratesFlow(currencies).first()

        // Then
        coVerify { localSource.save(remoteRates) }
        assertEquals(localRates, result)
    }

    @Test
    fun `given remote fetch fails, when ratesFlow collected, then does not save to local and emits local data`() = runTest {
        // Given
        val currencies = listOf(Currency("USD"), Currency("MXN"))
        val localRates = listOf(
            Rate(BigDecimal.fromInt(20), BigDecimal.fromInt(19), Currency("USD"), Currency("MXN"))
        )

        coEvery { remoteSource.fetch(currencies) } throws RuntimeException("Network error")
        coEvery { localSource.get(currencies) } returns localRates

        // When
        val result = repository.ratesFlow(currencies).first()

        // Then
        coVerify(exactly = 0) { localSource.save(any()) }
        assertEquals(localRates, result)
    }

    @Test
    fun `given valid currencies, when ratesFlow collected, then emits twice per 10 seconds`() = runTest {
        // Given
        val currencies = listOf(Currency("USD"))
        val localRates = listOf(
            Rate(BigDecimal.fromInt(20), BigDecimal.fromInt(19), Currency("USD"), Currency("MXN"))
        )

        coEvery { remoteSource.fetch(currencies) } returns emptyList()
        coEvery { localSource.save(any()) } returns Unit
        coEvery { localSource.get(currencies) } returns localRates

        val emissions = mutableListOf<List<Rate>>()

        // When
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            repository.ratesFlow(currencies).collect {
                emissions.add(it)
            }
        }

        // Then
        assertEquals(1, emissions.size)

        advanceTimeBy(5001)
        assertEquals(2, emissions.size)

        advanceTimeBy(5000)
        assertEquals(3, emissions.size)
    }
}
