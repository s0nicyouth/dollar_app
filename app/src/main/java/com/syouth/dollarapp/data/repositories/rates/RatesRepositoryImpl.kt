package com.syouth.dollarapp.data.repositories.rates

import com.syouth.dollarapp.domain.models.Currency
import com.syouth.dollarapp.domain.models.Rate
import com.syouth.dollarapp.domain.repositories.RatesRepository
import jakarta.inject.Inject
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import kotlin.time.Duration.Companion.seconds

internal class RatesRepositoryImpl @Inject constructor(
    private val localSource: RatesLocalSource,
    private val remoteSource: RatesRemoteSource,
) : RatesRepository {
    override fun ratesFlow(currencies: List<Currency>): Flow<List<Rate>> = flow {
        while (currentCoroutineContext().isActive) {
            if (currencies.isEmpty()) return@flow
            val remoteData = runCatching { remoteSource.fetch(currencies) }
            if (remoteData.isSuccess) localSource.save(remoteData.getOrThrow())
            emit(localSource.get(currencies))
            delay(5.seconds)
        }
    }
}