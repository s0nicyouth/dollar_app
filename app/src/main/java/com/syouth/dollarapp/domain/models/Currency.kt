package com.syouth.dollarapp.domain.models

data class Currency(val symbol: String) {
    companion object {
        val USDC = Currency("USDc")
        val EURC = Currency("EURc")
        val MXN = Currency("MXN")
        val BRL = Currency("BRL")
        val ARS = Currency("ARS")
        val COP = Currency("COP")
    }
}
