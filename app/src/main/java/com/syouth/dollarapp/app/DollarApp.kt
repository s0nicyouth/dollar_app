package com.syouth.dollarapp.app

import android.app.Application
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.request.crossfade
import com.syouth.dollarapp.app.di.application.ApplicationComponent
import com.syouth.dollarapp.app.di.application.DaggerApplicationComponent

internal class DollarApp : Application(), SingletonImageLoader.Factory {

    internal lateinit var applicationComponent: ApplicationComponent

    override fun newImageLoader(context: PlatformContext): ImageLoader {
        return ImageLoader.Builder(context)
            .crossfade(true)
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        applicationComponent = DaggerApplicationComponent.factory().create(this)
    }
}