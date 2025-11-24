package com.syouth.dollarapp.data.db.converters

import androidx.room.TypeConverter
import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.syouth.dollarapp.domain.models.Currency

internal class Converters {
    @TypeConverter
    fun fromBigDecimalToString(n: BigDecimal?): String? = n?.toPlainString()

    @TypeConverter
    fun fromStringToBigDecimal(n: String?): BigDecimal? = n?.let { BigDecimal.parseString(it) }

    @TypeConverter
    fun fromCurrencyToString(c: Currency?): String? = c?.symbol

    @TypeConverter
    fun fromStringToCurrency(s: String?): Currency? = s?.let(::Currency)
}