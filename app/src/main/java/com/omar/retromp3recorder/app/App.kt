package com.omar.retromp3recorder.app

import android.app.Application
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.omar.retromp3recorder.app.di.AndroidModule
import com.omar.retromp3recorder.app.di.AppComponent
import com.omar.retromp3recorder.app.di.DaggerAppComponent
import com.omar.retromp3recorder.bl.StartupUC
import timber.log.Timber
import javax.inject.Inject

class App : Application() {
    @Inject
    lateinit var startupUC: StartupUC

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
        appComponent.inject(this)

        startupUC.execute().subscribe()
    }

    companion object {
        @JvmStatic
        lateinit var appComponent: AppComponent
    }
}