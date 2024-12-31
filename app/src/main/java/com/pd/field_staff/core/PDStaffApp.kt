package com.pd.field_staff.core

import android.app.Application
import com.pd.field_staff.BuildConfig
import com.pd.field_staff.core.di.pdKoinModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import timber.log.Timber

class PDStaffApp: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@PDStaffApp)
            modules(listOf(pdKoinModule))
        }

        if(BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

}