package com.omar.retromp3recorder.app

import android.app.Application
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.omar.retromp3recorder.bl.system.StartupUC
import dagger.hilt.android.HiltAndroidApp
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {
    @Inject
    lateinit var startUC: StartupUC

    override fun onCreate() {
        super.onCreate()
        startUC.execute().subscribeOn(Schedulers.io()).subscribe()

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
