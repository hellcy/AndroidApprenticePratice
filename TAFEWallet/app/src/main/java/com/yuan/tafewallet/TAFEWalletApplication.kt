package com.yuan.tafewallet

import android.app.Application
import com.yuan.tafewallet.dagger.AppComponent
import com.yuan.tafewallet.dagger.AppModule
import com.yuan.tafewallet.dagger.DaggerAppComponent

class TAFEWalletApplication : Application() {
    lateinit var mainComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        mainComponent = initDagger(this)
    }

    private fun initDagger(app: TAFEWalletApplication): AppComponent =
        DaggerAppComponent.builder()
            .appModule(AppModule(app))
            .build()
}