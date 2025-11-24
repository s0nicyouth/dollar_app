package com.syouth.dollarapp.ui.main_screen

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.DecimalMode
import com.ionspin.kotlin.bignum.decimal.RoundingMode
import com.syouth.dollarapp.domain.models.Currency
import com.syouth.dollarapp.domain.repositories.CurrenciesRepository
import com.syouth.dollarapp.domain.repositories.RatesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
internal class MainScreenViewModel(
    private val mapper: MainScreenStateMapper,
    private val ratesRepository: RatesRepository,
    private val currenciesRepository: CurrenciesRepository,
) : ViewModel(), MainScreenContract.Model {

    private val modelState = MutableStateFlow(MainScreenContract.ModelState())
    override fun run(lifecycle: Lifecycle) {
        viewModelScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                currenciesRepository
                    .currenciesFlow()
                    .onEach { currencies -> modelState.update { state -> state.copy(availableCurrencies = currencies) } }
                    .launchIn(lifecycle.coroutineScope)

                modelState
                    .flatMapLatest { state ->
                        if (state.toCurrency != Currency.USDC) {
                            ratesRepository.ratesFlow(listOfNotNull(state.toCurrency))
                        } else {
                            ratesRepository.ratesFlow(listOfNotNull(state.fromCurrency))
                        }
                    }.onEach { rates ->
                        val rate = rates.firstOrNull()
                        if (rate != null) modelState.update { state -> state.copy(conversionRate = rate) }
                        recalculateAmounts()
                    }.launchIn(lifecycle.coroutineScope)
            }
        }
    }

    override fun stateFlow(): Flow<MainScreenContract.ViewState> = modelState
        .map(mapper::map)

    override fun onIntent(intent: MainScreenContract.Intent) {
        when (intent) {
            MainScreenContract.Intent.ClickedOnFrom -> { modelState.update { state -> state.copy(inputInFrom = true) } }
            MainScreenContract.Intent.ClickedOnTo -> { modelState.update { state -> state.copy(inputInFrom = false) } }
            MainScreenContract.Intent.SwitchSides -> {
                modelState.update { state ->
                    state.copy(
                        fromCurrency = state.toCurrency,
                        toCurrency = state.fromCurrency,
                        sellingUsdc = state.sellingUsdc.not(),
                    )
                }
                recalculateAmounts()
            }

            is MainScreenContract.Intent.FromAmountChanged -> {
                modelState.update { state -> state.copy(fromAmount = intent.value) }
                recalculateAmounts()
            }
            is MainScreenContract.Intent.ToAmountChanged -> {
                modelState.update { state -> state.copy(toAmount = intent.value) }
                recalculateAmounts()
            }

            is MainScreenContract.Intent.CurrencySelected -> {
                if (modelState.value.sellingUsdc) {
                    modelState.update { state -> state.copy(toCurrency = intent.currency) }
                } else {
                    modelState.update { state -> state.copy(fromCurrency = intent.currency) }
                }
                recalculateAmounts()
            }
        }
    }

    private fun recalculateAmounts() {
        if (modelState.value.inputInFrom) {
            val value = BigDecimal.fromBigDecimal(modelState.value.fromAmount, decimalMode = DecimalMode(20, RoundingMode.ROUND_HALF_AWAY_FROM_ZERO))
            if (modelState.value.sellingUsdc) {
                modelState.update { state -> state.copy(toAmount = value * state.conversionRate.bid) }
            } else {
                modelState.update { state ->
                    state.copy(
                        toAmount = value.divide(state.conversionRate.ask, DecimalMode(20, RoundingMode.ROUND_HALF_AWAY_FROM_ZERO)),
                    )
                }
            }
        } else {
            val value = BigDecimal.fromBigDecimal(modelState.value.toAmount, decimalMode = DecimalMode(20, RoundingMode.ROUND_HALF_AWAY_FROM_ZERO))
            if (modelState.value.sellingUsdc) {
                modelState.update { state ->
                    state.copy(
                        fromAmount = value.divide(state.conversionRate.bid, DecimalMode(20, RoundingMode.ROUND_HALF_AWAY_FROM_ZERO))
                    )
                }
            } else {
                modelState.update { state -> state.copy(fromAmount = value * state.conversionRate.ask) }
            }
        }
    }
}