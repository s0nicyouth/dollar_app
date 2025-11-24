package com.syouth.dollarapp.data.repositories.rates

import com.syouth.dollarapp.data.db.DollarAppDatabase
import com.syouth.dollarapp.data.db.dao.RatesDao
import com.syouth.dollarapp.data.db.entities.RateEntity
import com.syouth.dollarapp.data.repositories.rates.mappers.RateEntityMapper
import com.syouth.dollarapp.domain.models.Currency
import com.syouth.dollarapp.domain.models.Rate
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class RatesLocalSourceTest {

    private lateinit var ratesLocalSource: RatesLocalSource
    private lateinit var db: DollarAppDatabase
    private lateinit var ratesDao: RatesDao
    private lateinit var rateEntityMapper: RateEntityMapper
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        db = mockk()
        ratesDao = mockk()
        rateEntityMapper = mockk()

        every { db.ratesDao() } returns ratesDao
        ratesLocalSource = RatesLocalSource(db, dispatcher, rateEntityMapper)
    }

    @Test
    fun `given currencies when get called then return list of rates`() = runTest(dispatcher) {
        // Given
        val currencies = listOf(Currency.USDC)
        val rateEntity = mockk<RateEntity>()
        val rate = mockk<Rate>()
        
        coEvery { ratesDao.query(currencies) } returns listOf(rateEntity)
        every { rateEntityMapper.map(rateEntity) } returns rate

        // When
        val result = ratesLocalSource.get(currencies)

        // Then
        assertEquals(1, result.size)
        assertEquals(rate, result[0])
        coVerify { ratesDao.query(currencies) }
    }

    @Test
    fun `given list of rates when save called then insert all rates`() = runTest(dispatcher) {
        // Given
        val rate = mockk<Rate>()
        val rateEntity = mockk<RateEntity>()
        val rates = listOf(rate)
        
        every { rateEntityMapper.map(rate) } returns rateEntity
        coEvery { ratesDao.insertAll(any()) } returns Unit

        // When
        ratesLocalSource.save(rates)

        // Then
        coVerify { ratesDao.insertAll(listOf(rateEntity)) }
    }
}
