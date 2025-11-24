package com.syouth.dollarapp.ui.main_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.syouth.dollarapp.R
import com.syouth.dollarapp.ui.common.AmountFormatter
import com.syouth.dollarapp.ui.main_screen.MainScreenContract.ViewState.Exchange.Chooser
import com.syouth.dollarapp.ui.main_screen.di.MainScreenComponent
import com.syouth.dollarapp.ui.widgets.CurrencyWithAmount
import com.syouth.dollarapp.ui.widgets.RadioButton
import com.syouth.dollarapp.ui.widgets.SwitchSides
import com.syouth.dollarapp.ui.widgets.TextWithImage

object MainScreen {

    @Composable
    fun Widget(api: MainScreenApi) {
        val model = (api as MainScreenComponent).model
        val amountFormatter = api.amountFormatter
        model.run(LocalLifecycleOwner.current.lifecycle)
        val state by api.model.stateFlow().collectAsStateWithLifecycle(MainScreenContract.ViewState.Loading)
        when (val s = state) {
            is MainScreenContract.ViewState.Exchange -> ExchangeWidget(s, model, amountFormatter)
            MainScreenContract.ViewState.Loading -> LoadingWidget()
        }
    }

    @Composable
    private fun LoadingWidget() {}

    @Composable
    private fun ExchangeWidget(
        state: MainScreenContract.ViewState.Exchange,
        model: MainScreenContract.Model,
        amountFormatter: AmountFormatter,
    ) {
        Box(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.background)
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center,
        ) {
            val showBottomSheet = rememberSaveable { mutableStateOf(false) }
            CurrencyChooser(state, showBottomSheet, model) { showBottomSheet.value = false }
            Column {
                Text(
                    text = stringResource(R.string.main_screen_title),
                    fontSize = 32.sp,
                )
                Text(
                    text = "1 ${state.mainCurrency.symbol} = ${amountFormatter.format(state.usdcToTargetRate.toPlainString())} ${state.oppositeCurrency.symbol}", color = MaterialTheme.colorScheme.surfaceBright,
                    fontSize = 16.sp,
                )
                Box(
                    modifier = Modifier
                        .padding(top = 32.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Column {
                        val modifierClicked = { showBottomSheet.value = true }
                        val empty = {}
                        val chooserFrom = state.chooser == Chooser.FROM
                        CurrencyWithAmount.Widget(
                            CurrencyWithAmount.WidgetModel(
                                state.fromCurrency,
                                state.fromFlag,
                                state.fromAmount,
                                chooserFrom,
                                currencySymbol = state.fromSymbol,
                            ),
                            onFocusReceived = { model.onIntent(MainScreenContract.Intent.ClickedOnFrom) },
                            onAmountChanged = { model.onIntent(MainScreenContract.Intent.FromAmountChanged(it)) },
                            onModifierClicked = if (chooserFrom) modifierClicked else empty,
                            formatAmount = amountFormatter::format,
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        CurrencyWithAmount.Widget(
                            CurrencyWithAmount.WidgetModel(
                                state.toCurrency,
                                state.toFlag,
                                state.toAmount,
                                !chooserFrom,
                                currencySymbol = state.toSymbol,
                            ),
                            onFocusReceived = { model.onIntent(MainScreenContract.Intent.ClickedOnTo) },
                            onAmountChanged = { model.onIntent(MainScreenContract.Intent.ToAmountChanged(it)) },
                            onModifierClicked = if (!chooserFrom) modifierClicked else empty,
                            formatAmount = amountFormatter::format,
                        )
                    }
                    Box(
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.background,
                                shape = CircleShape,
                            ).padding(4.dp)
                    ) { SwitchSides.Widget { model.onIntent(MainScreenContract.Intent.SwitchSides) } }
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun CurrencyChooser(
        state: MainScreenContract.ViewState.Exchange,
        showBottomSheet: MutableState<Boolean>,
        model: MainScreenContract.Model,
        onDismissRequest: () -> Unit,
    ) {
        val sheetState = rememberModalBottomSheetState()
        if (showBottomSheet.value) {
            ModalBottomSheet(
                onDismissRequest = onDismissRequest,
                sheetState = sheetState,
                containerColor = MaterialTheme.colorScheme.background,
            ) {
                LazyColumn(
                    modifier = Modifier
                        .padding(16.dp)
                        .background(color = MaterialTheme.colorScheme.primaryContainer, shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp, bottomStart = 10.dp, bottomEnd = 10.dp))
                        .fillMaxWidth(),
                ) {
                    items(state.availableCurrencies) { data ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                        ) {
                            TextWithImage.Widget(
                                model = TextWithImage.WidgetModel(
                                    text = data.first.symbol,
                                    image = data.second,
                                    textColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                    withImageBackground = true,
                                )
                            )
                            RadioButton.Widget(
                                model = RadioButton.WidgetModel(state.selectedCurrency == data.first),
                                modifier = Modifier.align(Alignment.CenterEnd).size(24.dp),
                                onClicked = {
                                    showBottomSheet.value = false
                                    model.onIntent(MainScreenContract.Intent.CurrencySelected(data.first))
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}