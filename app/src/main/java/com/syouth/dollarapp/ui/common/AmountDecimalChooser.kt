package com.syouth.dollarapp.ui.common

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.RoundingMode
import javax.inject.Inject

internal class AmountDecimalChooser @Inject constructor() {
    fun format(amount: BigDecimal): BigDecimal =
        amount.roundToDigitPositionAfterDecimalPoint(2, RoundingMode.ROUND_HALF_AWAY_FROM_ZERO)
}