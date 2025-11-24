package com.syouth.dollarapp.app.di.application

import android.content.Context
import androidx.room.Room
import com.syouth.dollarapp.data.db.DollarAppDatabase
import com.syouth.dollarapp.data.repositories.currencies.CurrenciesRepositoryImpl
import com.syouth.dollarapp.data.repositories.currencies.mappers.CurrencyMapper
import com.syouth.dollarapp.data.repositories.currencies.mappers.CurrencyMapperImpl
import com.syouth.dollarapp.data.repositories.rates.RatesRepositoryImpl
import com.syouth.dollarapp.data.repositories.rates.mappers.RateDtoMapper
import com.syouth.dollarapp.data.repositories.rates.mappers.RateDtoMapperImpl
import com.syouth.dollarapp.data.repositories.rates.mappers.RateEntityMapper
import com.syouth.dollarapp.data.repositories.rates.mappers.RateEntityMapperImpl
import com.syouth.dollarapp.domain.repositories.CurrenciesRepository
import com.syouth.dollarapp.domain.repositories.RatesRepository
import com.syouth.dollarapp.ui.main_screen.di.MainScreenDependencies
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Component(
    modules = [
        ApplicationModule::class,
    ]
)
@Singleton
interface ApplicationComponent : MainScreenDependencies {

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context,
        ): ApplicationComponent
    }
}

@Module
internal interface ApplicationModule {

    @[Binds]
    fun bindCurrencyRepository(impl: CurrenciesRepositoryImpl): CurrenciesRepository

    @[Binds]
    fun bindRatesRepository(impl: RatesRepositoryImpl): RatesRepository

    companion object {
        @[Provides Singleton]
        fun provideHttpClient(): HttpClient {
            return HttpClient(OkHttp) {
                install(ContentNegotiation) {
                    json(
                        Json {
                            prettyPrint = true
                            isLenient = true
                            ignoreUnknownKeys = true
                        }
                    )
                }
                install(Logging) {
                    level = LogLevel.ALL
                }
            }
        }

        @[Provides]
        fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

        @[Provides Singleton]
        fun provideDatabase(context: Context): DollarAppDatabase = Room.databaseBuilder(
            context,
            DollarAppDatabase::class.java,
            "dollar_app_database",
        ).build()

        @[Provides]
        fun provideCurrencyMapper(): CurrencyMapper = CurrencyMapperImpl()

        @[Provides]
        fun provideRateEntityMapper(): RateEntityMapper = RateEntityMapperImpl()

        @[Provides]
        fun provideRateDtoMapper(): RateDtoMapper = RateDtoMapperImpl()
    }
}