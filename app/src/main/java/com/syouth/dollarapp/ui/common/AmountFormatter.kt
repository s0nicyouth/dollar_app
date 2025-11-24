package com.syouth.dollarapp.ui.common

import android.icu.text.NumberFormat
import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.toJavaBigDecimal
import javax.inject.Inject

internal class AmountFormatter @Inject constructor(
    private val numberFormatter: NumberFormat,
) {
    fun format(input: String): String {
        if (input.isEmpty()) return input
        val number = runCatching { BigDecimal.parseString(input) }
        if (number.isFailure) return input
        val formatted = numberFormatter.format(number.getOrThrow().toJavaBigDecimal())
        return if (input.last() == '.') {
            "$formatted."
        } else formatted
    }
}