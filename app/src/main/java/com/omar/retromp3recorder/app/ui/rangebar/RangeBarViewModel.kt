package com.omar.retromp3recorder.app.ui.rangebar

import androidx.lifecycle.ViewModel
import com.omar.retromp3recorder.app.App
import com.omar.retromp3recorder.app.ui.rangebar.RangeBarOutputMapper.mapOutputToState
import com.omar.retromp3recorder.utils.disposedBy
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

class RangeBarViewModel : ViewModel() {

    val state = BehaviorSubject.create<RangeBarView.State>()
    val input = PublishSubject.create<RangeBarView.Input>()

    private val compositeDisposable = CompositeDisposable()

    @Inject
    lateinit var interactor: RangeBarInteractor

    init {
        App.appComponent.inject(this)
        input
            .compose(interactor.processIO())
            .compose(mapOutputToState())
            .subscribe(state::onNext)
            .disposedBy(compositeDisposable)
    }

    fun onInput(action: RangeBarView.Input) {
        input.onNext(action)
    }

    override fun onCleared() {
        compositeDisposable.clear()
    }
}