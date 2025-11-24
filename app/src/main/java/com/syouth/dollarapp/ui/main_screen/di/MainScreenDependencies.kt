package com.syouth.dollarapp.ui.main_screen.di

import com.syouth.dollarapp.domain.repositories.CurrenciesRepository
import com.syouth.dollarapp.domain.repositories.RatesRepository

interface MainScreenDependencies {
    val currenciesRepository: CurrenciesRepository
    val ratesRepository: RatesRepository
}