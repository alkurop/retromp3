package com.omar.retromp3recorder.app.ui.joined_progress

import androidx.lifecycle.ViewModel
import com.omar.retromp3recorder.app.App
import com.omar.retromp3recorder.dto.JoinedProgress
import com.omar.retromp3recorder.utils.disposedBy
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

class JoinedProgressViewModel : ViewModel() {
    val state = BehaviorSubject.create<JoinedProgress>()
    val input = PublishSubject.create<JoinedProgressView.In>()

    @Inject
    lateinit var interactor: JoinedProgressInteractor
    private val compositeDisposable = CompositeDisposable()

    init {
        App.appComponent.getComponent().inject(this)
        input
            .compose(interactor.processIO())
            .subscribe(state::onNext)
            .disposedBy(compositeDisposable)
    }

    override fun onCleared() {
        compositeDisposable.clear()
    }
}
