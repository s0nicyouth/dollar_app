package com.syouth.dollarapp.data.repositories.currencies

import com.syouth.dollarapp.data.repositories.currencies.models.dto.CurrenciesDto
import com.syouth.dollarapp.domain.models.Currency
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.request
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class CurrenciesRemoteSource @Inject constructor(
    private val httpClient: HttpClient,
    private val dispatcher: CoroutineDispatcher,
) {
    suspend fun fetch(): List<Currency> = withContext(dispatcher) {
        httpClient
            .request("https://api.dolarapp.dev/v1/tickers-currencies")
            .body<CurrenciesDto>()
            .currencies
            .map(::Currency)
    }
}