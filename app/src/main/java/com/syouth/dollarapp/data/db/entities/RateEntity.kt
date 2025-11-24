package com.syouth.dollarapp.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.syouth.dollarapp.domain.models.Currency

@Entity(
    tableName = "da_rates",
    primaryKeys = ["from", "to"],
)
internal data class RateEntity(
    @ColumnInfo("ask") val ask: BigDecimal,
    @ColumnInfo("bid") val bid: BigDecimal,
    @ColumnInfo("from") val from: Currency,
    @ColumnInfo("to") val to: Currency,
)