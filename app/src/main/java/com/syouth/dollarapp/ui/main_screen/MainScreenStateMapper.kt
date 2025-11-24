package com.syouth.dollarapp.ui.main_screen

import com.syouth.dollarapp.domain.models.Currency
import com.syouth.dollarapp.ui.CurrencyToSymbolMapper
import com.syouth.dollarapp.ui.common.AmountDecimalChooser
import com.syouth.dollarapp.ui.main_screen.mappers.CurrencyToFlagUrlMapper
import javax.inject.Inject

internal class MainScreenStateMapper @Inject constructor(
    private val currencyToFlagUrlMapper: CurrencyToFlagUrlMapper,
    private val currencyToSymbolMapper: CurrencyToSymbolMapper,
    private val amountDecimalChooser: AmountDecimalChooser,
) {
    suspend fun map(model: MainScreenContract.ModelState): MainScreenContract.ViewState =
        MainScreenContract.ViewState.Exchange(
            fromCurrency = model.fromCurrency,
            fromFlag = currencyToFlagUrlMapper.map(model.fromCurrency),
            toCurrency = model.toCurrency,
            toFlag = currencyToFlagUrlMapper.map(model.toCurrency),
            usdcToTargetRate = if (model.sellingUsdc) model.conversionRate.bid else model.conversionRate.ask,
            mainCurrency = Currency.USDC,
            oppositeCurrency = if (model.sellingUsdc) model.toCurrency else model.fromCurrency,
            fromAmount = amountDecimalChooser.format(model.fromAmount),
            toAmount = amountDecimalChooser.format(model.toAmount),
            fromSymbol = currencyToSymbolMapper.map(model.fromCurrency),
            toSymbol = currencyToSymbolMapper.map(model.toCurrency),
            availableCurrencies = model.availableCurrencies.filter { it != Currency.USDC }.map {
                it to currencyToFlagUrlMapper.map(it)
            },
            selectedCurrency = if (model.sellingUsdc) model.toCurrency else model.fromCurrency,
            chooser = if (model.sellingUsdc) MainScreenContract.ViewState.Exchange.Chooser.TO else MainScreenContract.ViewState.Exchange.Chooser.FROM,
        )
}