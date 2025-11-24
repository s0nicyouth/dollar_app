package com.syouth.dollarapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.syouth.dollarapp.data.db.converters.Converters
import com.syouth.dollarapp.data.db.dao.CurrenciesDao
import com.syouth.dollarapp.data.db.dao.RatesDao
import com.syouth.dollarapp.data.db.entities.CurrencyEntity
import com.syouth.dollarapp.data.db.entities.RateEntity

@Database(
    entities = [
        CurrencyEntity::class,
        RateEntity::class,
    ],
    version = 1,
)
@TypeConverters(Converters::class)
internal abstract class DollarAppDatabase : RoomDatabase() {
    abstract fun currenciesDao(): CurrenciesDao
    abstract fun ratesDao(): RatesDao
}