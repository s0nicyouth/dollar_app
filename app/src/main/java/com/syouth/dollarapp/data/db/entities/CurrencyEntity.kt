package com.syouth.dollarapp.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("da_currencies")
internal data class CurrencyEntity(
    @PrimaryKey @ColumnInfo("symbol") val symbol: String,
)
