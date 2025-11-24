package com.syouth.dollarapp.data.repositories.rates

import com.syouth.dollarapp.data.db.DollarAppDatabase
import com.syouth.dollarapp.data.repositories.rates.mappers.RateEntityMapper
import com.syouth.dollarapp.domain.models.Currency
import com.syouth.dollarapp.domain.models.Rate
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class RatesLocalSource @Inject constructor(
    private val db: DollarAppDatabase,
    private val dispatcher: CoroutineDispatcher,
    private val rateEntityMapper: RateEntityMapper,
) {
    suspend fun get(currencies: List<Currency>): List<Rate> = withContext(dispatcher) {
        db.ratesDao().query(currencies).map(rateEntityMapper::map)
    }

    suspend fun save(currencies: List<Rate>) = withContext(dispatcher) {
        db.ratesDao().insertAll(currencies.map { rateEntityMapper.map(it) })
    }
}