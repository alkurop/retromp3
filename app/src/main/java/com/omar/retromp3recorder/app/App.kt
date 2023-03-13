package com.omar.retromp3recorder.app

import android.app.Application
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.omar.retromp3recorder.app.di.AndroidModule
import com.omar.retromp3recorder.app.di.AppComponent
import com.omar.retromp3recorder.app.di.DaggerAppComponent
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().androidModule(AndroidModule(this)).build()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
            Timber.plant(object : Timber.Tree() {
                override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
                    if (t != null) {
                        FirebaseCrashlytics.getInstance().recordException(t)
                    }
                }
            })
        }
    }

    companion object {
        @JvmStatic
        lateinit var appComponent: AppComponent
    }
}
