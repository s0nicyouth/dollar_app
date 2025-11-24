package com.syouth.dollarapp.data.repositories.rates.mappers

import com.syouth.dollarapp.data.repositories.rates.models.dto.RateDto
import com.syouth.dollarapp.domain.models.Currency
import com.syouth.dollarapp.domain.models.Rate
import com.syouth.kmapper.converters.BigDecimalTypeConverters
import com.syouth.kmapper.processor_annotations.Bind
import com.syouth.kmapper.processor_annotations.Mapper

@Mapper
internal interface RateDtoMapper : BigDecimalTypeConverters {
    fun map(d: RateDto, @Bind from: Currency, @Bind to: Currency): Rate

    companion object {
        fun bookToSides(book: String): Pair<Currency, Currency> {
            val parts = book.split("_")
            return Currency(parts[0].uppercase()) to Currency(parts[1].uppercase())
        }
    }
}