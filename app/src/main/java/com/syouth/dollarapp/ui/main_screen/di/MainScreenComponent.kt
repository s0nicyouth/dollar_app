package com.syouth.dollarapp.ui.main_screen.di

import android.icu.text.NumberFormat
import com.syouth.dollarapp.app.di.scopes.ScreenScope
import com.syouth.dollarapp.data.repositories.currencies.mappers.CurrencyMapper
import com.syouth.dollarapp.data.repositories.currencies.mappers.CurrencyMapperImpl
import com.syouth.dollarapp.domain.repositories.CurrenciesRepository
import com.syouth.dollarapp.domain.repositories.RatesRepository
import com.syouth.dollarapp.ui.common.AmountDecimalChooser
import com.syouth.dollarapp.ui.common.AmountFormatter
import com.syouth.dollarapp.ui.main_screen.MainScreenApi
import com.syouth.dollarapp.ui.main_screen.MainScreenContract
import com.syouth.dollarapp.ui.main_screen.MainScreenStateMapper
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides

internal typealias ModelFactory = (MainScreenStateMapper, RatesRepository, CurrenciesRepository) -> MainScreenContract.Model

@Component(
    modules = [
        MainScreenModule::class,
    ],
    dependencies = [
        MainScreenDependencies::class,
    ],
)
@ScreenScope
internal interface MainScreenComponent : MainScreenApi {

    val model: MainScreenContract.Model
    val amountFormatter: AmountFormatter

    @Component.Factory
    interface Factory {
        fun create(
            dependencies: MainScreenDependencies,
            @BindsInstance modelFactory: ModelFactory,
        ): MainScreenComponent
    }
}

@Module
internal interface MainScreenModule {
    companion object {
        @[Provides]
        fun provideCurrencyMapper(): CurrencyMapper = CurrencyMapperImpl()

        @[Provides]
        fun provideModel(
            modelFactory: ModelFactory,
            mainScreenStateMapper: MainScreenStateMapper,
            ratesRepository: RatesRepository,
            currenciesRepository: CurrenciesRepository,
        ): MainScreenContract.Model = modelFactory(mainScreenStateMapper, ratesRepository, currenciesRepository)

        @[Provides]
        fun provideNumberFormat(): NumberFormat = NumberFormat.getInstance()
    }
}