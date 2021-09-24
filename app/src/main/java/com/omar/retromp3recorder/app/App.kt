package com.omar.retromp3recorder.app

import android.app.Application
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.omar.retromp3recorder.app.di.AndroidModule
import com.omar.retromp3recorder.app.di.AppComponent
import com.omar.retromp3recorder.app.di.DaggerAppComponent
import com.omar.retromp3recorder.app.ui.joined_progress.JoinedProgressMapper
import com.omar.retromp3recorder.bl.WakeLockUsecase
import com.omar.retromp3recorder.bl.audio.PlayerProgressMapper
import com.omar.retromp3recorder.bl.audio.RecordWavetableUC
import com.omar.retromp3recorder.bl.files.NewFileUpdater
import com.omar.retromp3recorder.bl.files.ScanDirFilesUC
import com.omar.retromp3recorder.bl.files.TakeLastFileDirScanUC
import com.omar.retromp3recorder.bl.settings.LoadRecorderSettingsUC
import timber.log.Timber
import javax.inject.Inject

class App : Application() {
    @Inject
    lateinit var loadRecorderSettingsUC: LoadRecorderSettingsUC

    @Inject
    lateinit var cleanUpSeekRepoUsecase: NewFileUpdater

    @Inject
    lateinit var playerProgressMapper: PlayerProgressMapper

    @Inject
    lateinit var takeLastFileWithScanDirScanUC: TakeLastFileDirScanUC

    @Inject
    lateinit var scanDirFilesUC: ScanDirFilesUC

    @Inject
    lateinit var wakelockUsecase: WakeLockUsecase

    @Inject
    lateinit var wavetableUC: RecordWavetableUC

    @Inject
    lateinit var joinPlayerProgressMapper: JoinedProgressMapper

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().androidModule(AndroidModule(this)).build()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true);
            Timber.plant(object : Timber.Tree() {
                override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
                    if (t != null) {
                        FirebaseCrashlytics.getInstance().recordException(t)
                    }
                }
            })
        }
        appComponent.inject(this)

        loadRecorderSettingsUC.execute().subscribe()
        takeLastFileWithScanDirScanUC.execute().subscribe()
        cleanUpSeekRepoUsecase.execute().subscribe()
        playerProgressMapper.execute().subscribe()
        wakelockUsecase.execute().subscribe()
        scanDirFilesUC.execute(true).subscribe()
        wavetableUC.execute().subscribe()
        joinPlayerProgressMapper.observe().subscribe()
    }

    companion object {
        @JvmStatic
        lateinit var appComponent: AppComponent
    }
}