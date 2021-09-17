package com.omar.retromp3recorder.app.ui.recorder_settings.audio_source

import androidx.lifecycle.ViewModel
import com.omar.retromp3recorder.app.App
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.utils.disposedBy
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

class AudioSourceViewModel : ViewModel() {
    val state = BehaviorSubject.create<Mp3VoiceRecorder.AudioSourcePref>()
    val input = PublishSubject.create<Mp3VoiceRecorder.AudioSourcePref>()

    @Inject
    lateinit var interactor: AudioSourceInteractor
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