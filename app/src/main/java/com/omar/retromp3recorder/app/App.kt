package com.omar.retromp3recorder.app

import android.app.Application
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
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
}
