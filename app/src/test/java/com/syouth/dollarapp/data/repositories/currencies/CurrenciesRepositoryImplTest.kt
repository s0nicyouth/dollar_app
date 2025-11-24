package com.syouth.dollarapp.data.repositories.currencies

import com.syouth.dollarapp.domain.models.Currency
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class CurrenciesRepositoryImplTest {

    private val localSource: CurrenciesLocalSource = mockk()
    private val remoteSource: CurrenciesRemoteSource = mockk()
    private val repository = CurrenciesRepositoryImpl(localSource, remoteSource)

    @Test
    fun `given remote fetch succeeds, when currenciesFlow collected, then saves to local and emits local data`() = runTest {
        // Given
        val remoteCurrencies = listOf(Currency("USD"), Currency("EUR"))
        val localCurrencies = listOf(Currency("USD"), Currency("EUR"))

        coEvery { remoteSource.fetch() } returns remoteCurrencies
        coEvery { localSource.save(remoteCurrencies) } returns Unit
        coEvery { localSource.getAll() } returns localCurrencies

        // When
        val result = repository.currenciesFlow().first()

        // Then
        coVerify { localSource.save(remoteCurrencies) }
        assertEquals(localCurrencies, result)
    }

    @Test
    fun `given remote fetch fails, when currenciesFlow collected, then does not save to local and emits local data`() = runTest {
        // Given
        val localCurrencies = listOf(Currency("GBP"))

        coEvery { remoteSource.fetch() } throws RuntimeException("Network error")
        coEvery { localSource.getAll() } returns localCurrencies

        // When
        val result = repository.currenciesFlow().first()

        // Then
        coVerify(exactly = 0) { localSource.save(any()) }
        assertEquals(localCurrencies, result)
    }
}
