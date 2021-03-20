package com.egoriku.radiotok

import android.app.Application
import com.egoriku.radiotok.koin.radioModule
import org.koin.core.context.startKoin

class RadioApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            modules(radioModule)
        }
    }
}