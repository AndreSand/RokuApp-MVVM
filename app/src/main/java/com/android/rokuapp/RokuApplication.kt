package com.android.rokuapp

import android.app.Application
import com.android.rokuapp.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class RokuApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@RokuApplication)
            modules(appModule)
        }
    }
}
