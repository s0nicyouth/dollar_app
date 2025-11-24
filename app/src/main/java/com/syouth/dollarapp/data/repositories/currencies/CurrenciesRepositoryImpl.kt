package com.syouth.dollarapp.data.repositories.currencies

import com.syouth.dollarapp.domain.models.Currency
import com.syouth.dollarapp.domain.repositories.CurrenciesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class CurrenciesRepositoryImpl @Inject constructor(
    private val localSource: CurrenciesLocalSource,
    private val remoteSource: CurrenciesRemoteSource,
) : CurrenciesRepository {
    override fun currenciesFlow(): Flow<List<Currency>> = flow {
        val remoteData = runCatching { remoteSource.fetch() }
        if (remoteData.isSuccess) localSource.save(remoteData.getOrThrow())
        emit(localSource.getAll())
    }
}