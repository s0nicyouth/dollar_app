package com.syouth.dollarapp.domain.repositories

import com.syouth.dollarapp.domain.models.Currency
import com.syouth.dollarapp.domain.models.Rate
import kotlinx.coroutines.flow.Flow

interface RatesRepository {
    fun ratesFlow(currencies: List<Currency>): Flow<List<Rate>>
}