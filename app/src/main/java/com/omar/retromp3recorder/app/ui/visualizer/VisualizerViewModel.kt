package com.omar.retromp3recorder.app.ui.visualizer

import androidx.lifecycle.ViewModel
import com.omar.retromp3recorder.app.App
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorderImpl
import com.omar.retromp3recorder.utils.domain.disposedBy
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

class VisualizerViewModel : ViewModel() {
    val state = BehaviorSubject.create<VisualizerView.State>()
    val input = PublishSubject.create<VisualizerView.Input>()

    @Inject
    lateinit var interactor: VisualizerInteractor

    @Inject
    lateinit var recorder: Mp3VoiceRecorderImpl
    private val compositeDisposable = CompositeDisposable()

    init {
        App.appComponent.getComponent().inject(this)
        input
            .compose(interactor.processIO())
            .compose(VisualizerOutputMapper.mapOutputToState())
            .distinctUntilChanged()
            .subscribe(state::onNext)
            .disposedBy(compositeDisposable)
    }

    override fun onCleared() {
        compositeDisposable.clear()
    }
}
