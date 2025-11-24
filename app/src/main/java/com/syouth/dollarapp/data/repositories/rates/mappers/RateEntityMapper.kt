package com.syouth.dollarapp.data.repositories.rates.mappers

import com.syouth.dollarapp.data.db.entities.RateEntity
import com.syouth.dollarapp.domain.models.Rate
import com.syouth.kmapper.processor_annotations.Mapper

@Mapper
internal interface RateEntityMapper : StringToCurrencyMapper {
    fun map(e: RateEntity): Rate
    fun map(e: Rate): RateEntity
}