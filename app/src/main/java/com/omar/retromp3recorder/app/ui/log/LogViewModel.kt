package com.omar.retromp3recorder.app.ui.log

import androidx.lifecycle.ViewModel
import com.omar.retromp3recorder.app.App
import com.omar.retromp3recorder.utils.disposedBy
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

class LogViewModel : ViewModel() {
    val state = BehaviorSubject.create<LogView.State>()
    val input = PublishSubject.create<LogView.Input>()

    @Inject
    lateinit var interactor: LogInteractor
    private val compositeDisposable = CompositeDisposable()

    init {
        App.appComponent.inject(this)
        input
            .compose(interactor.processIO())
            .compose(LogOutputMapper.mapOutputToState())
            .subscribe(state::onNext)
            .disposedBy(compositeDisposable)
    }

    override fun onCleared() {
        compositeDisposable.clear()
    }
}
