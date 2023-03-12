package com.omar.retromp3recorder.app.ui.main

import androidx.lifecycle.ViewModel
import com.omar.retromp3recorder.app.App
import com.omar.retromp3recorder.app.ui.main.MainViewOutputMapper.mapOutputToState
import com.omar.retromp3recorder.storage.repo.global.ToastRepo
import com.omar.retromp3recorder.utils.domain.disposedBy
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

class MainViewModel() : ViewModel() {
    val state = BehaviorSubject.create<MainView.State>()
    val input = PublishSubject.create<MainView.Input>()

    @Inject
    lateinit var interactor: MainViewInteractor

    @Inject
    lateinit var toastRepo: ToastRepo

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
