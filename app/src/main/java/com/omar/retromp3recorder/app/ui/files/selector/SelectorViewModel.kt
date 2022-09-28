package com.omar.retromp3recorder.app.ui.files.selector

import androidx.lifecycle.ViewModel
import com.omar.retromp3recorder.app.App
import com.omar.retromp3recorder.utils.disposedBy
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

class SelectorViewModel : ViewModel() {
    val state = BehaviorSubject.create<SelectorView.State>()
    val input = PublishSubject.create<SelectorView.Input>()

    @Inject
    lateinit var interactor: SelectorInteractor
    private val compositeDisposable = CompositeDisposable()

    init {
        App.appComponent.getComponent().inject(this)
        input
            .compose(interactor.processIO())
            .compose(SelectorOutputMapper.mapOutputToState())
            .subscribe(state::onNext)
            .disposedBy(compositeDisposable)
    }

    override fun onCleared() {
        compositeDisposable.clear()
    }
}
