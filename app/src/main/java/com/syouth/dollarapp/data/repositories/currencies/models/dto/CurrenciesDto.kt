package com.syouth.dollarapp.data.repositories.currencies.models.dto

import kotlinx.serialization.Serializable

@Serializable
internal data class CurrenciesDto(val currencies: List<String>)
