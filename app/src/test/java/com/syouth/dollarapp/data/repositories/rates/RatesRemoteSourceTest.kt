package com.syouth.dollarapp.data.repositories.rates

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.syouth.dollarapp.data.repositories.rates.mappers.RateDtoMapper
import com.syouth.dollarapp.data.repositories.rates.models.dto.RateDto
import com.syouth.dollarapp.domain.models.Currency
import com.syouth.dollarapp.domain.models.Rate
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class RatesRemoteSourceTest {

    private lateinit var ratesRemoteSource: RatesRemoteSource
    private lateinit var rateDtoMapper: RateDtoMapper
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        rateDtoMapper = mockk()
    }

    @Test
    fun `given currencies when fetch called then return list of rates`() = runTest(dispatcher) {
        // Given
        val currencies = listOf(Currency.USDC, Currency.ARS)
        val jsonResponse = """
            [
                {
                    "ask": "1.0",
                    "bid": "0.99",
                    "book": "usd_ars"
                }
            ]
        """.trimIndent()

        val mockEngine = MockEngine { _ ->
            respond(
                content = jsonResponse,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val httpClient = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }

        val expectedFrom = Currency("USD")
        val expectedTo = Currency("ARS")
        val expectedRate = Rate(
            ask = BigDecimal.parseString("1.0"),
            bid = BigDecimal.parseString("0.99"),
            from = expectedFrom,
            to = expectedTo
        )
        
        val expectedDto = RateDto("1.0", "0.99", "usd_ars")

        every { 
            rateDtoMapper.map(
                d = expectedDto, 
                from = expectedFrom, 
                to = expectedTo
            ) 
        } returns expectedRate

        ratesRemoteSource = RatesRemoteSource(httpClient, rateDtoMapper, dispatcher)

        // When
        val result = ratesRemoteSource.fetch(currencies)

        // Then
        assertEquals(1, result.size)
        assertEquals(expectedRate, result[0])
    }

    @Test
    fun `given empty response when fetch called then return empty list`() = runTest(dispatcher) {
        // Given
        val currencies = listOf(Currency.USDC)
        val jsonResponse = "[]"

        val mockEngine = MockEngine { _ ->
            respond(
                content = jsonResponse,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val httpClient = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }

        ratesRemoteSource = RatesRemoteSource(httpClient, rateDtoMapper, dispatcher)

        // When
        val result = ratesRemoteSource.fetch(currencies)

        // Then
        assertEquals(0, result.size)
    }
}
