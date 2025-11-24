package com.syouth.dollarapp.domain.models

import com.ionspin.kotlin.bignum.decimal.BigDecimal

data class Rate(
    val ask: BigDecimal,
    val bid: BigDecimal,
    val from: Currency,
    val to: Currency,
)
