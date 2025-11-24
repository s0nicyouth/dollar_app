package com.syouth.dollarapp.data.repositories.currencies

import com.syouth.dollarapp.domain.models.Currency
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Test

class CurrenciesRemoteSourceTest {

    private lateinit var currenciesRemoteSource: CurrenciesRemoteSource
    private val dispatcher = StandardTestDispatcher()

    @Test
    fun `given successful response when fetch called then return list of currencies`() = runTest(dispatcher) {
        // Given
        val jsonResponse = """
            {
                "currencies": ["USD", "ARS", "MXN"]
            }
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

        currenciesRemoteSource = CurrenciesRemoteSource(httpClient, dispatcher)

        // When
        val result = currenciesRemoteSource.fetch()

        // Then
        val expected = listOf(Currency("USD"), Currency("ARS"), Currency("MXN"))
        assertEquals(expected, result)
    }

    @Test
    fun `given empty response when fetch called then return empty list`() = runTest(dispatcher) {
        // Given
        val jsonResponse = """
            {
                "currencies": []
            }
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

        currenciesRemoteSource = CurrenciesRemoteSource(httpClient, dispatcher)

        // When
        val result = currenciesRemoteSource.fetch()

        // Then
        assertEquals(emptyList<Currency>(), result)
    }
}
