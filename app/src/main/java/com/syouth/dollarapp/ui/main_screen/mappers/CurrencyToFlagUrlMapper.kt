package com.syouth.dollarapp.ui.main_screen.mappers

import com.syouth.dollarapp.domain.models.Currency
import com.syouth.dollarapp.ui.widgets.Image
import javax.inject.Inject

internal class CurrencyToFlagUrlMapper @Inject constructor() {

    // Just add more flags here for other countries
    // To make it bullet proof additional endpoint is needed which would return flags.
    private val currencyToFlagUrlMap = mapOf(
        Currency("USDc") to "https://flagcdn.com/us.svg",
        Currency("MXN") to "https://flagcdn.com/mx.svg",
        Currency("COP") to "https://flagcdn.com/co.svg",
        Currency("BRL") to "https://flagcdn.com/br.svg",
        Currency("ARS") to "https://flagcdn.com/ar.svg",
        Currency("EURc") to "https://flagcdn.com/eu.svg",
    )

    fun map(c: Currency): Image.WidgetModel.RemoteImage =
        (currencyToFlagUrlMap[c] ?: "https://flagcdn.com/us.svg").let(Image.WidgetModel::RemoteImage)
}