package com.syouth.dollarapp.ui

import com.syouth.dollarapp.domain.models.Currency
import javax.inject.Inject

internal class CurrencyToSymbolMapper @Inject constructor() {

    private val currencyToSymbolMap = mapOf(
        Currency.USDC to "$",
        Currency.MXN to "MX$",
        Currency.EURC to "€",
        Currency.ARS to "AR$",
        Currency.BRL to "BR$",
        Currency.COP to "CO$",
    )

    fun map(currency: Currency): String = currencyToSymbolMap[currency] ?: currency.symbol
}