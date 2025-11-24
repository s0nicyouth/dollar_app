package com.syouth.dollarapp.ui.main_screen.di

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.syouth.dollarapp.domain.repositories.CurrenciesRepository
import com.syouth.dollarapp.domain.repositories.RatesRepository
import com.syouth.dollarapp.ui.main_screen.MainScreenApi
import com.syouth.dollarapp.ui.main_screen.MainScreenStateMapper
import com.syouth.dollarapp.ui.main_screen.MainScreenViewModel

object MainScreenApiCreator {
    fun create(
        dependencies: MainScreenDependencies,
        viewModelStoreOwner: ViewModelStoreOwner,
    ): MainScreenApi =
        DaggerMainScreenComponent
            .factory()
            .create(
                dependencies = dependencies,
                modelFactory = { mapper: MainScreenStateMapper, ratesRepository: RatesRepository, currenciesRepository: CurrenciesRepository ->
                    ViewModelProvider.create(
                        owner = viewModelStoreOwner,
                        factory = viewModelFactory { initializer { MainScreenViewModel(mapper, ratesRepository, currenciesRepository) } },
                    )[MainScreenViewModel::class]
                }
            )
}