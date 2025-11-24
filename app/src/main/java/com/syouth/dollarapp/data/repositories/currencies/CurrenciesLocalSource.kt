package com.syouth.dollarapp.data.repositories.currencies

import com.syouth.dollarapp.data.db.DollarAppDatabase
import com.syouth.dollarapp.data.repositories.currencies.mappers.CurrencyMapper
import com.syouth.dollarapp.domain.models.Currency
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class CurrenciesLocalSource @Inject constructor(
    private val db: DollarAppDatabase,
    private val currencyMapper: CurrencyMapper,
    private val dispatcher: CoroutineDispatcher,
) {
    suspend fun getAll(): List<Currency> = withContext(dispatcher) {
        val result = db.currenciesDao().queryAll().map(currencyMapper::map)
        result.ifEmpty { // Just fallback to the default set of currencies
            listOf(
                Currency.USDC,
                Currency.MXN,
                Currency.ARS,
                Currency.BRL,
                Currency.COP,
                Currency.EURC,
            )
        }
    }

    suspend fun save(data: List<Currency>) = withContext(dispatcher) {
        db.currenciesDao().insertAll(data.map(currencyMapper::map))
    }
}