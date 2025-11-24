package com.syouth.dollarapp.data.repositories.rates.mappers

import com.syouth.dollarapp.domain.models.Currency

internal interface StringToCurrencyMapper {
    fun map(value: String?): Currency? = value?.let(::Currency)
}