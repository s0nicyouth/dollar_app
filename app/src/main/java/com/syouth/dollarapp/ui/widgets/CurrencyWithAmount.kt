package com.syouth.dollarapp.ui.widgets

import android.icu.text.DecimalFormatSymbols
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.OutputTransformation
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.delete
import androidx.compose.foundation.text.input.insert
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.syouth.dollarapp.R
import com.syouth.dollarapp.domain.models.Currency
import com.syouth.dollarapp.ui.common.EditDistance
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged

internal typealias OnFocusReceived = () -> Unit
internal typealias OnAmountChanged = (BigDecimal) -> Unit
internal typealias OnModifierClicked = () -> Unit
internal typealias FormatAmount = (String) -> String

internal object CurrencyWithAmount {
    @Composable
    fun Widget(
        data: WidgetModel,
        onFocusReceived: OnFocusReceived = {},
        onAmountChanged: OnAmountChanged = { _ -> },
        onModifierClicked: OnModifierClicked = {},
        formatAmount: FormatAmount = { it },
    ) {
        val inputState = rememberTextFieldState(initialText = data.amount.toPlainString())
        var nonUserInput by remember { mutableStateOf(false) }
        Box(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(10.dp),
                ).padding(start = 8.dp, end = 8.dp, top = 16.dp, bottom = 16.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                TextWithImage.Widget(
                    TextWithImage.WidgetModel(
                        text = data.currency.symbol,
                        image = data.flag,
                    )
                )
                if (data.showModifier) {
                    Box(modifier = Modifier.clickable { onModifierClicked() }) {
                        Image.Widget(
                            modifier = Modifier.padding(start = 4.dp).size(8.dp),
                            model = Image.WidgetModel.VectorResource(R.drawable.arrow_down, tint = MaterialTheme.colorScheme.onPrimaryContainer),
                        )
                    }
                }
            }
            val inputValue = data.amount.toPlainString().toBigDecimal()
            val currentValue = runCatching { inputState.text.toString().toBigDecimal() }
            if (currentValue.isSuccess && inputValue != currentValue.getOrThrow()) {
                nonUserInput = true
                inputState.edit { replace(0, length, data.amount.toPlainString()) }
            }
            val interactionSource = remember { MutableInteractionSource() }
            val isFocused by interactionSource.collectIsFocusedAsState()
            LaunchedEffect(isFocused) { if (isFocused) onFocusReceived() }
            LaunchedEffect(inputState) {
                snapshotFlow { inputState.text.toString() }
                    .distinctUntilChanged()
                    .collectLatest {
                        if (!nonUserInput) {
                            val number = runCatching { it.toBigDecimal() }
                            if (number.isSuccess) {
                                onAmountChanged(number.getOrThrow())
                            }
                        } else {
                            nonUserInput = false
                        }
                    }
            }
            BasicTextField(
                state = inputState,
                modifier = Modifier.align(Alignment.CenterEnd),
                textStyle = TextStyle.Default.copy(fontSize = 16.sp),
                lineLimits = TextFieldLineLimits.SingleLine,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                interactionSource = interactionSource,
                outputTransformation = OutputTransformation {
                    val dotIndex = asCharSequence().indexOf('.')
                    if (dotIndex >= 0) {
                        replace(
                            dotIndex,
                            dotIndex + 1,
                            DecimalFormatSymbols.getInstance().decimalSeparator.toString(),
                        )
                    }
                    val ops = EditDistance.calculateEditOperations(toString(), formatAmount(toString()))
                    for (o in ops) {
                        when (o) {
                            is EditDistance.EditOperation.Delete -> delete(o.index, o.index + 1)
                            is EditDistance.EditOperation.Insert -> insert(o.index, o.char.toString())
                            is EditDistance.EditOperation.Replace -> replace(o.index, o.index + 1, o.newChar.toString())
                        }
                    }
                    insert(0, "${data.currencySymbol} ")
                },
                inputTransformation = {
                    val text = asCharSequence().toString()
                    val textWithReplacedDecimalSymbol = text.replace(DecimalFormatSymbols.getInstance().decimalSeparator, '.')
                    if (text.isEmpty()) {
                        return@BasicTextField
                    } else {
                        val number =
                            runCatching { BigDecimal.parseString(textWithReplacedDecimalSymbol) }
                        if (number.isFailure || number.getOrThrow().isNegative) revertAllChanges()
                    }
                }
            )
        }
    }

    data class WidgetModel(
        val currency: Currency,
        val flag: Image.WidgetModel,
        val amount: BigDecimal,
        val showModifier: Boolean,
        val currencySymbol: String,
    )

    private fun String.toBigDecimal(): BigDecimal =
        BigDecimal.parseString(this.replace(DecimalFormatSymbols.getInstance().decimalSeparator, '.'))
}