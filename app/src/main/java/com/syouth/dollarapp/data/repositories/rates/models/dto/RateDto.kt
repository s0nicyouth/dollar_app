package com.syouth.dollarapp.data.repositories.rates.models.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class RateDto(
    @SerialName("ask") val ask: String,
    @SerialName("bid") val bid: String,
    @SerialName("book") val book: String,
)
