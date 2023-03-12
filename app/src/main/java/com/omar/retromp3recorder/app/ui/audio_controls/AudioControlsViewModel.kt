package com.omar.retromp3recorder.app.ui.audio_controls

import androidx.lifecycle.ViewModel
import com.omar.retromp3recorder.app.App
import com.omar.retromp3recorder.app.ui.audio_controls.AudioControlsOutputMapper.mapOutputToState
import com.omar.retromp3recorder.utils.domain.disposedBy
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

class AudioControlsViewModel : ViewModel() {

    val state = BehaviorSubject.create<AudioControlsView.State>()
    val input = PublishSubject.create<AudioControlsView.Input>()

    @Inject
    lateinit var interactor: AudioControlsInteractor

    private val compositeDisposable = CompositeDisposable()

    init {
        App.appComponent.getComponent().inject(this)
        input
            .compose(interactor.processIO())
            .compose(mapOutputToState())
            .subscribe(state::onNext)
            .disposedBy(compositeDisposable)
    }

    override fun onCleared() {
        compositeDisposable.clear()
    }

}
