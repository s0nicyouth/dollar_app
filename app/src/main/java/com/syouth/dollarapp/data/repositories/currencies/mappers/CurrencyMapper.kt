package com.syouth.dollarapp.data.repositories.currencies.mappers

import com.syouth.dollarapp.data.db.entities.CurrencyEntity
import com.syouth.dollarapp.domain.models.Currency
import com.syouth.kmapper.processor_annotations.Mapper

@Mapper
internal interface CurrencyMapper {
    fun map(entity: CurrencyEntity): Currency
    fun map(c: Currency): CurrencyEntity
}