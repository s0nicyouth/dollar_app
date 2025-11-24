package com.syouth.dollarapp.data.repositories.currencies

import com.syouth.dollarapp.data.db.DollarAppDatabase
import com.syouth.dollarapp.data.db.dao.CurrenciesDao
import com.syouth.dollarapp.data.db.entities.CurrencyEntity
import com.syouth.dollarapp.data.repositories.currencies.mappers.CurrencyMapper
import com.syouth.dollarapp.domain.models.Currency
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CurrenciesLocalSourceTest {

    private lateinit var currenciesLocalSource: CurrenciesLocalSource
    private lateinit var db: DollarAppDatabase
    private lateinit var currenciesDao: CurrenciesDao
    private lateinit var currencyMapper: CurrencyMapper
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        db = mockk()
        currenciesDao = mockk()
        currencyMapper = mockk()

        every { db.currenciesDao() } returns currenciesDao
        currenciesLocalSource = CurrenciesLocalSource(db, currencyMapper, dispatcher)
    }

    @Test
    fun `given empty database when getAll called then return default currencies`() = runTest(dispatcher) {
        // Given
        coEvery { currenciesDao.queryAll() } returns emptyList()

        // When
        val result = currenciesLocalSource.getAll()

        // Then
        val expected = listOf(
            Currency.USDC,
            Currency.MXN,
            Currency.ARS,
            Currency.BRL,
            Currency.COP,
            Currency.EURC,
        )
        assertEquals(expected, result)
        coVerify { currenciesDao.queryAll() }
    }

    @Test
    fun `given populated database when getAll called then return stored currencies`() = runTest(dispatcher) {
        // Given
        val currencyEntity = mockk<CurrencyEntity>()
        val currency = Currency("USD")
        
        coEvery { currenciesDao.queryAll() } returns listOf(currencyEntity)
        every { currencyMapper.map(currencyEntity) } returns currency

        // When
        val result = currenciesLocalSource.getAll()

        // Then
        assertEquals(listOf(currency), result)
        coVerify { currenciesDao.queryAll() }
    }

    @Test
    fun `given list of currencies when save called then insert all currencies`() = runTest(dispatcher) {
        // Given
        val currency = Currency("USD")
        val currencyEntity = mockk<CurrencyEntity>()
        val currencies = listOf(currency)
        
        every { currencyMapper.map(currency) } returns currencyEntity
        coEvery { currenciesDao.insertAll(any()) } returns Unit

        // When
        currenciesLocalSource.save(currencies)

        // Then
        coVerify { currenciesDao.insertAll(listOf(currencyEntity)) }
    }
}
