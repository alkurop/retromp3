package com.omar.retromp3recorder.app.screens.settings.components.beat_rate

import androidx.lifecycle.ViewModel
import com.omar.retromp3recorder.app.App
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.utils.domain.disposedBy
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

class BitRateSettingsViewModel : ViewModel() {
    val state = BehaviorSubject.create<Mp3VoiceRecorder.BitRate>()
    val input = PublishSubject.create<Mp3VoiceRecorder.BitRate>()

    @Inject
    lateinit var interactor: BitRateSettingsInteractor
    private val compositeDisposable = CompositeDisposable()

    init {
        App.appComponent.inject(this)
        input
            .compose(interactor.processIO())
            .subscribe(state::onNext)
            .disposedBy(compositeDisposable)
    }

    override fun onCleared() {
        compositeDisposable.clear()
    }
}
