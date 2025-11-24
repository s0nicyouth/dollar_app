package com.syouth.dollarapp.domain.repositories

import com.syouth.dollarapp.domain.models.Currency
import kotlinx.coroutines.flow.Flow

interface CurrenciesRepository {
    fun currenciesFlow(): Flow<List<Currency>>
}