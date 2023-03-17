package com.omar.retromp3recorder.app.screens.main

import androidx.lifecycle.ViewModel
import com.omar.retromp3recorder.app.screens.main.MainViewOutputMapper.mapOutputToState
import com.omar.retromp3recorder.storage.repo.global.ToastRepo
import com.omar.retromp3recorder.utils.domain.disposedBy
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    interactor: MainViewInteractor,
    val toastRepo: ToastRepo
) : ViewModel() {
    val state = BehaviorSubject.create<MainViewContract.State>()
    val input = PublishSubject.create<MainViewContract.Input>()


    private val compositeDisposable = CompositeDisposable()

    init {
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
