package com.syouth.dollarapp.data.repositories.rates

import com.syouth.dollarapp.data.repositories.rates.mappers.RateDtoMapper
import com.syouth.dollarapp.data.repositories.rates.models.dto.RateDto
import com.syouth.dollarapp.domain.models.Currency
import com.syouth.dollarapp.domain.models.Rate
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.request
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class RatesRemoteSource @Inject constructor(
    private val httpClient: HttpClient,
    private val rateDtoMapper: RateDtoMapper,
    private val dispatcher: CoroutineDispatcher,
) {
    suspend fun fetch(currencies: List<Currency>): List<Rate> = withContext(dispatcher) {
        httpClient.request("https://api.dolarapp.dev/v1/tickers?currencies=${
            currencies.joinToString(
                ","
            ) { it.symbol }
        }").body<List<RateDto>>().map { r ->
            val (from, to) = RateDtoMapper.bookToSides(r.book)
            rateDtoMapper.map(
                d = r,
                from = from,
                to = to,
            )
        }
    }
}