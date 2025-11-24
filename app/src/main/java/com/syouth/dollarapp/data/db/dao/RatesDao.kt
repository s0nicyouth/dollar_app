package com.syouth.dollarapp.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.syouth.dollarapp.data.db.entities.RateEntity
import com.syouth.dollarapp.domain.models.Currency

@Dao
internal interface RatesDao {
    @Query("SELECT * FROM `da_rates` WHERE `to` IN (:currencies)")
    suspend fun query(currencies: List<Currency>): List<RateEntity>

    @Query("SELECT * FROM `da_rates`")
    fun queryAll(): List<RateEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(rates: List<RateEntity>)
}