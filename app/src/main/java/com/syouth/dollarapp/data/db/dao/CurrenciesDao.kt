package com.syouth.dollarapp.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.syouth.dollarapp.data.db.entities.CurrencyEntity

@Dao
internal interface CurrenciesDao {

    @Query("SELECT * FROM `da_currencies`")
    suspend fun queryAll(): List<CurrencyEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(currencies: List<CurrencyEntity>)
}