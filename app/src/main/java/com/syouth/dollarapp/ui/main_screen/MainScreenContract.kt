package com.syouth.dollarapp.ui.main_screen

import androidx.lifecycle.Lifecycle
import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.syouth.dollarapp.domain.models.Currency
import com.syouth.dollarapp.domain.models.Rate
import com.syouth.dollarapp.ui.widgets.Image
import kotlinx.coroutines.flow.Flow

internal interface MainScreenContract {
    interface Model {
        fun run(lifecycle: Lifecycle)

        fun stateFlow(): Flow<ViewState>
        fun onIntent(intent: Intent)
    }

    sealed interface Intent {
        data object SwitchSides : Intent
        data object ClickedOnFrom : Intent
        data object ClickedOnTo : Intent
        data class FromAmountChanged(val value: BigDecimal) : Intent
        data class ToAmountChanged(val value: BigDecimal) : Intent
        data class CurrencySelected(val currency: Currency) : Intent
    }

    data class ModelState(
        val fromCurrency: Currency = Currency.USDC,
        val toCurrency: Currency = Currency.MXN,
        val fromAmount: BigDecimal = BigDecimal.fromInt(0),
        val toAmount: BigDecimal = BigDecimal.fromInt(0),
        val availableCurrencies: List<Currency> = emptyList(),
        val conversionRate: Rate = Rate(
            ask = BigDecimal.fromInt(1),
            bid = BigDecimal.fromInt(1),
            from = Currency.USDC,
            to = Currency.MXN,
        ),
        val sellingUsdc: Boolean = true,
        val inputInFrom: Boolean = true,
    )

    sealed interface ViewState {
        data object Loading : ViewState
        data class Exchange(
            val fromCurrency: Currency,
            val toCurrency: Currency,
            val fromFlag: Image.WidgetModel,
            val toFlag: Image.WidgetModel,
            val usdcToTargetRate: BigDecimal,
            val mainCurrency: Currency,
            val oppositeCurrency: Currency,
            val fromAmount: BigDecimal,
            val toAmount: BigDecimal,
            val fromSymbol: String,
            val toSymbol: String,
            val availableCurrencies: List<Pair<Currency, Image.WidgetModel>>,
            val selectedCurrency: Currency,
            val chooser: Chooser,
        ) : ViewState {
            enum class Chooser {
                FROM,
                TO,
            }
        }
    }
}